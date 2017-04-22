/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import sk.matfyz.belica.messages.*;
import sk.matfyz.belica.solver.TreeSolver;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Message;

/**
 *
 * @author martin
 */
public class PhaseTwo implements Phase {

    private final Context context;
    private final TreeSolver solver;
    private final ActiveMessages activeMessages;

    private final Map<AgentId, Boolean> participatedFiringEnded = new HashMap<>();

    public PhaseTwo(Context context) {
        this.context = context;
        this.solver = new TreeSolver((ArrayList<Rule>) context.getRules());
        this.activeMessages = new ActiveMessages();

        if (context.isInitialProgram()) {
            participatedFiringEnded.clear();
            context.getParticipatedPrograms().forEach((programLabel) -> participatedFiringEnded.put(programLabel, Boolean.FALSE));
        }

    }

    @Override
    public void handleMessage(Message message) {
        if (message instanceof FireRequestMessage) {
            processFireRequest(message);
        } else if (message instanceof FireResponseMessage) {
            processFireResponse(message);
        } else if (message instanceof FiringEndedMessage) {
            processFiringEnded(message);
        }
    }

    @Override
    public void sendMessage(Message message) {
        context.sendMessage(message);
    }

    private void processFiringEnded(Object message) {
        AgentId sender = ((FiringEndedMessage) message).getSender();

        if (!participatedFiringEnded.get(sender)) {
            participatedFiringEnded.put(sender, Boolean.TRUE);

            // test  ci skoncili vsetci
            if (!participatedFiringEnded.containsValue(Boolean.FALSE)) {
                context.getOwner().processResult(context.getSmallestModel());
                
                sendMessage(new ContextEndedMessage(
                        context.getOwner().getName(),
                        context.getOwner().generateMessageId(),
                        context.getContextId(),
                        context.getParticipatedPrograms()
                ));
            }
        }
    }

    private void processFireRequest(Object message) {
        FireRequestMessage requestMessage = (FireRequestMessage) message;
        Set<Literal> obtainedLiterals = requestMessage.getLits();
        AgentId sender = requestMessage.getSender();

        context.getSmallestModel().addAll(obtainedLiterals);
        fire(requestMessage);

        // ziadne nove message z tejto `requestMessage` nevznikli, tak rovno odpovedam 
        if (activeMessages.messageHasNoChildren(requestMessage)) {
            sendMessage(new FireResponseMessage(
                    context.getOwner().getName(),
                    context.getOwner().generateMessageId(),
                    context.getContextId(),
                    Collections.singleton(sender),
                    requestMessage.getId()
            ));
        }
    }

    private void processFireResponse(Object message) {
        FireResponseMessage responseMessage = (FireResponseMessage) message;
        AgentId senderLabel = responseMessage.getSender();

//        response som si poslal sam sebe
        if (senderLabel.equals(context.getOwner().getName())) {
            sendMessage(new FiringEndedMessage(
                    context.getOwner().getName(),
                    context.getOwner().generateMessageId(),
                    context.getContextId(),
                    Collections.singleton(context.getInitialProgramLabel())
            ));
        } else {
//        vymazem v mape request message, na ktoru prisla odpoved | poslem response ak po vymazani je prazdne pole
            Map<AgentId, Object> resolvedMessages = activeMessages.resolveChildMessage(senderLabel, responseMessage.getReferenceId());

            resolvedMessages.entrySet().forEach(resolved -> {
                sendMessage(new FireResponseMessage(
                        context.getOwner().getName(),
                        context.getOwner().generateMessageId(),
                        context.getContextId(),
                        Collections.singleton(resolved.getKey()),
                        ((Message) resolved.getValue()).getId()
                ));
            });
        }
    }

    private void fire(Object parentRequestMessage) {
        Set<Literal> newDerived = solver.findSmallestModel(context.getSmallestModel());
        newDerived.removeAll(context.getSmallestModel());

//        System.out.println("core.Program.fire()#" + label + " newDerived: " + newDerived);
        if (!newDerived.isEmpty()) {
            Map<AgentId, Set<Literal>> literalsToSend = new HashMap<>();
            newDerived.forEach(lit -> {
                if (context.getAskedLiterals().containsKey(lit)) {
                    context.getAskedLiterals().get(lit).forEach(prog -> {
                        if (!literalsToSend.containsKey(prog)) {
                            literalsToSend.put(prog, new HashSet<>());
                        }
                        literalsToSend.get(prog).add(lit);
                    });
                }
            });

            literalsToSend.entrySet().stream().forEach((entry) -> {
                Message childMessage = new FireRequestMessage(
                        context.getOwner().getName(),
                        context.getOwner().generateMessageId(),
                        context.getContextId(),
                        Collections.singleton(entry.getKey()),
                        entry.getValue()
                );

                activeMessages.addChildMessage(parentRequestMessage, entry.getKey(), childMessage);
                sendMessage(childMessage);
            });
            context.getSmallestModel().addAll(newDerived);
        }
    }
}
