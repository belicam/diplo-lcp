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

    private final Context context;
    private final ActiveMessages activeMessages;

    private Map<AgentId, Object> resolvedParent = new HashMap<>();
    private boolean rulesChecked;

    public PhaseOne(Context program) {
        this.context = program;
        this.activeMessages = new ActiveMessages();
        this.rulesChecked = false;
    }

    @Override
    public void sendMessage(Message message) {
        context.sendMessage(message);
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

        context.setInitialProgramLabel(request.getInitialSender());

        request.getLits().forEach(lit -> {
            if (!context.getAskedLiterals().containsKey(lit)) {
                context.getAskedLiterals().put(lit, new HashSet<>());
            }
            context.getAskedLiterals().get(lit).add(from);
        });

        if (rulesChecked) {
            sendMessage(new GetResponseMessage(
                    context.getOwner().getName(),
                    context.getOwner().generateMessageId(),
                    context.getContextId(),
                    Collections.singleton(from),
                    request.getId())
            );
        } else {
            checkRules(message, context.getInitialProgramLabel());

            if (activeMessages.noMessages() && context.isParticipationConfirmed()) {
                sendMessage(new GetResponseMessage(
                        context.getOwner().getName(),
                        context.getOwner().generateMessageId(),
                        context.getContextId(),
                        Collections.singleton(from),
                        request.getId())
                );
            } else {
                resolvedParent.put(from, message);
            }

            sendMessage(new NotifyParticipationRequestMessage(
                    context.getOwner().getName(),
                    context.getOwner().generateMessageId(),
                    context.getContextId(),
                    Collections.singleton(context.getInitialProgramLabel()))
            );
        }
    }

    private void processGetResponse(Object message) {
        AgentId from = ((GetResponseMessage) message).getSender();

        resolvedParent = activeMessages.resolveChildMessage(from, ((GetResponseMessage) message).getReferenceId());
        checkGetResponses();
    }

    private void processNotifyParticipationRequest(Object message) {
        AgentId senderLabel = ((NotifyParticipationRequestMessage) message).getSender();

        context.getParticipatedPrograms().add(senderLabel);

        Message msg = new NotifyParticipationResponseMessage(
                context.getOwner().getName(),
                context.getOwner().generateMessageId(),
                context.getContextId(),
                Collections.singleton(senderLabel)
        );

        sendMessage(msg);
    }

    private void processNotifyParticipationResponse(Object message) {
        context.setParticipationConfirmed(true);
        checkGetResponses();
    }

    private void checkRules(Object parentMessage, AgentId initialSender) {
        Map<AgentId, Set<Literal>> externals = new HashMap<>();

        context.getRules().forEach(rule -> {
            rule.getBody().stream().forEach(lit -> {
                AgentId litRef = lit.getProgramLabel();
                if (!litRef.equals(context.getOwner().getName())) {
                    if (!externals.containsKey(litRef)) {
                        externals.put(litRef, new HashSet<>());
                    }
                    externals.get(litRef).add(lit);
                }
            });
        });

        externals.keySet().forEach(key -> {
            Message childMessage = new GetRequestMessage(
                    context.getOwner().getName(),
                    context.getOwner().generateMessageId(),
                    context.getContextId(),
                    Collections.singleton(key),
                    externals.get(key), initialSender
            );

            activeMessages.addChildMessage(parentMessage, key, childMessage);
            context.sendMessage(childMessage);
        });
        rulesChecked = true;
    }

    private void checkGetResponses() {
        if (context.isParticipationConfirmed() && activeMessages.noMessages()) {
            if (context.isInitialProgram() && resolvedParent.isEmpty()) {
                Message msg = new DependencyGraphBuiltMessage(
                        context.getOwner().getName(),
                        context.getOwner().generateMessageId(),
                        context.getContextId(),
                        Collections.singleton(context.getOwner().getName())
                );

                sendMessage(msg);
            } else {
                resolvedParent.entrySet().forEach((parent) -> {
                    MessageId refId = ((Message) parent.getValue()).getId();
                    Message msg = new GetResponseMessage(
                            context.getOwner().getName(),
                            context.getOwner().generateMessageId(),
                            context.getContextId(),
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
