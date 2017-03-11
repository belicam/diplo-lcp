/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import sk.matfyz.belica.messages.*;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Message;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class PhaseOne implements Phase {

    private final LogicProgrammingAgent program;
    private final ActiveMessages activeMessages;

    private Map<AgentId, Object> resolvedParent = new HashMap<>();
    private boolean rulesChecked;

    public PhaseOne(LogicProgrammingAgent program) {
        this.program = program;
        this.activeMessages = new ActiveMessages();
        this.rulesChecked = false;
    }

    @Override
    public void sendMessage(Message message) {
        program.sendMessage(message);
    }

    @Override
    public void handleMessage(Message message) {
        if (message instanceof GetRequestMessage) {
            processGetRequest(message);
        } else if (message instanceof GetResponseMessage) {
            processGetResponse(message);
        } else if (message instanceof NotifyParticipationRequestMessage) {
            processNotifyParticipationRequest(message);
        } else if (message instanceof NotifyParticipationResponseMessage) {
            processNotifyParticipationResponse(message);
        }
    }

    private void processGetRequest(Object message) {
//        System.out.println("phases.PhaseOne.processGetRequest()");
        GetRequestMessage request = (GetRequestMessage) message;
        AgentId from = request.getSender();

        String[] splitContent = request.getContent().split(MessageContentSerializer.CONTENT_DELIMITER);

        Set<Literal> requestedLits = MessageContentSerializer.parseLiterals(splitContent[0]);
        AgentId initialSender = MessageContentSerializer.parseAgentId(splitContent[1]);

        program.setInitialProgramLabel(initialSender);

        requestedLits.forEach(lit -> {
            if (!program.getAskedLiterals().containsKey(lit)) {
                program.getAskedLiterals().put(lit, new HashSet<>());
            }
            program.getAskedLiterals().get(lit).add(from);
        });

        if (rulesChecked) {
            sendMessage(new GetResponseMessage(program.getName(), program.generateMessageId(), Collections.singleton(from), request.getId()));
        } else {
            checkRules(message, program.getInitialProgramLabel());

            /* prehodene poradie poslania notifyParticipation a kontoly na prazdne activeMessages (poslanie getresponse) */
            if (activeMessages.noMessages()) {
                sendMessage(new GetResponseMessage(program.getName(), program.generateMessageId(), Collections.singleton(from), request.getId()));
            }

            sendMessage(new NotifyParticipationRequestMessage(program.getName(), program.generateMessageId(), Collections.singleton(program.getInitialProgramLabel())));
        }
    }

    private void processGetResponse(Object message) {
        GetResponseMessage msg = (GetResponseMessage) message;
        AgentId from = msg.getSender();
        MessageId refId = MessageContentSerializer.parseMessageId(msg.getContent());

        resolvedParent = activeMessages.resolveChildMessage(from, refId);
        checkGetResponses();
    }

    private void processNotifyParticipationRequest(Object message) {
        AgentId senderLabel = ((NotifyParticipationRequestMessage) message).getSender();

        program.getParticipatedPrograms().add(senderLabel);
        sendMessage(new NotifyParticipationResponseMessage(program.getName(), program.generateMessageId(), Collections.singleton(senderLabel)));
    }

    private void processNotifyParticipationResponse(Object message) {
        program.setParticipationConfirmed(true);
        checkGetResponses();
    }

    private void checkRules(Object parentMessage, AgentId initialSender) {
        Map<AgentId, Set<Literal>> externals = new HashMap<>();

        program.getRules().forEach(rule -> {
            rule.getBody().stream().forEach(lit -> {
                AgentId litRef = lit.getProgramLabel();
                if (!litRef.equals(program.getName())) {
                    if (!externals.containsKey(litRef)) {
                        externals.put(litRef, new HashSet<>());
                    }
                    externals.get(litRef).add(lit);
                }
            });
        });

        externals.keySet().forEach(key -> {
            Message childMessage = new GetRequestMessage(
                    program.getName(),
                    program.generateMessageId(),
                    Collections.singleton(key),
                    externals.get(key), initialSender
            );

            activeMessages.addChildMessage(parentMessage, key, childMessage);
            program.sendMessage(childMessage);
        });
        rulesChecked = true;
    }

    private void checkGetResponses() {
        if (program.isParticipationConfirmed() && activeMessages.noMessages()) {
            if (program.isInitialProgram() && resolvedParent.isEmpty()) {
                sendMessage(new DependencyGraphBuiltMessage(program.getName(), program.generateMessageId(), Collections.singleton(program.getName())));
            } else {
                resolvedParent.entrySet().forEach((parent) -> {
                    MessageId refId = ((Message) parent.getValue()).getId();
                    Message msg = new GetResponseMessage(
                            program.getName(),
                            program.generateMessageId(),
                            Collections.singleton(parent.getKey()),
                            refId
                    );
                    sendMessage(msg);
                });
                resolvedParent.clear();
            }
        }
    }
}
