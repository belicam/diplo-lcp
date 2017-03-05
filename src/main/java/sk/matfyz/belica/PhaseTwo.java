/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import sk.matfyz.belica.messages.*;
import sk.matfyz.belica.solver.TreeSolver;
import sk.matfyz.lcp.api.AgentId;

/**
 *
 * @author martin
 */
public class PhaseTwo implements Phase {

    private final LogicProgrammingAgent program;
    private final TreeSolver solver;
    private final ActiveMessages activeMessages;

    private final Map<AgentId, Boolean> participatedFiringEnded = new HashMap<>();

    public PhaseTwo(LogicProgrammingAgent program) {
        this.program = program;
        this.solver = new TreeSolver((ArrayList<Rule>) program.getRules());
        this.activeMessages = new ActiveMessages();

        if (program.isInitialProgram()) {
            participatedFiringEnded.clear();
            program.getParticipatedPrograms().forEach((programLabel) -> participatedFiringEnded.put(programLabel, Boolean.FALSE));
        }

    }

    @Override
    public void handleMessage(Object message) {
        if (message instanceof FireRequestMessage) {
            processFireRequest(message);
        } else if (message instanceof FireResponseMessage) {
            processFireResponse(message);
        } else if (message instanceof FiringEndedMessage) {
            processFiringEnded(message);
        }
    }

    @Override
    public void sendMessage(AgentId receiverLabel, Object message) {
        program.sendMessage(receiverLabel, message);
    }

    private void processFiringEnded(Object message) {
        AgentId sender = ((FiringEndedMessage) message).getSenderLabel();

        if (!participatedFiringEnded.get(sender)) {
            participatedFiringEnded.put(sender, Boolean.TRUE);
            
            // test  ci skoncili vsetci
            if (!participatedFiringEnded.containsValue(Boolean.FALSE)) {
                program.broadcastMessage(new StopMessage());
            }
        }
    }

    private void processFireRequest(Object message) {
        FireRequestMessage requestMessage = (FireRequestMessage) message;
        Set<Literal> obtainedLiterals = requestMessage.getLits();
        AgentId sender = requestMessage.getSenderLabel();
        
        program.getSmallestModel().addAll(obtainedLiterals);
        fire(requestMessage);

        // ziadne nove message z tejto `requestMessage` nevznikli, tak rovno odpovedam 
        if (activeMessages.messageHasNoChildren(requestMessage)) {
            sendMessage(sender, new FireResponseMessage(program.generateMessageId(), program.getName(), requestMessage.getId()));
        }
    }

    private void processFireResponse(Object message) {
        FireResponseMessage responseMessage = (FireResponseMessage) message;
        AgentId senderLabel = responseMessage.getSenderLabel();

//        response som si poslal sam sebe
        if (senderLabel.equals(program.getName())) {            
            sendMessage(program.getInitialProgramLabel(), new FiringEndedMessage(program.getName()));
        } else {
//        vymazem v mape request message, na ktoru prisla odpoved | poslem response ak po vymazani je prazdne pole
            activeMessages.resolveChildMessage(senderLabel, responseMessage.getReferenceId()).entrySet().forEach(resolved -> {
                sendMessage(resolved.getKey(), new FireResponseMessage(program.generateMessageId(), program.getName(), ((Message) resolved.getValue()).getId()));
            });
        }
    }

    private void fire(Object parentRequestMessage) {
        Set<Literal> newDerived = solver.findSmallestModel(program.getSmallestModel());
        newDerived.removeAll(program.getSmallestModel());

//        System.out.println("core.Program.fire()#" + label + " newDerived: " + newDerived);
        if (!newDerived.isEmpty()) {
            Map<AgentId, Set<Literal>> literalsToSend = new HashMap<>();
            newDerived.forEach(lit -> {
                if (program.getAskedLiterals().containsKey(lit)) {
                    program.getAskedLiterals().get(lit).forEach(prog -> {
                        if (!literalsToSend.containsKey(prog)) {
                            literalsToSend.put(prog, new HashSet<>());
                        }
                        literalsToSend.get(prog).add(lit);
                    });
                }
            });

            literalsToSend.entrySet().stream().forEach((entry) -> {
                Message childMessage = new FireRequestMessage(program.generateMessageId(), program.getName(), entry.getValue());
                activeMessages.addChildMessage(parentRequestMessage, entry.getKey(), childMessage);
                sendMessage(entry.getKey(), childMessage);
            });
            program.getSmallestModel().addAll(newDerived);
        }
    }
}
