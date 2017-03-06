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
import java.util.List;
import java.util.Map;
import java.util.Set;
import sk.matfyz.belica.messages.*;
import sk.matfyz.lcp.AbstractAgent;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.EventListener;
import sk.matfyz.lcp.api.Message;
import sk.matfyz.lcp.api.MessageReceivedEvent;
import sk.matfyz.lcp.api.Platform;

/**
 *
 * @author martin
 */
public class LogicProgrammingAgent extends AbstractAgent implements EventListener<MessageReceivedEvent> {

    private AgentId initialProgramLabel = null;
    private boolean participationConfirmed = false;

    private List<Rule> rules = new ArrayList<>();

    private final Set<AgentId> participatedPrograms = new HashSet<>();
    private final Set<Literal> smallestModel = new HashSet<>();
    private final Map<Literal, Set<AgentId>> askedLiterals = new HashMap<>();

    private Phase phase;

    public LogicProgrammingAgent(Platform platform) {
        super(platform, new AgentId());
    }

    public LogicProgrammingAgent(Platform platform, AgentId name) {
        super(platform, name);
    }

    @Override
    public void onEvent(MessageReceivedEvent e) {
        processMessage(e.getMessage());
    }

    private void processMessage(Message message) {
        if (message != null) {
            if (message instanceof InitMessage) {
                processInit();
            } else if (message instanceof ActivationMessage) {
                processActivation();
            } else if (message instanceof DependencyGraphBuiltMessage) {
                processDependencyGraphBuilt();
            } else {
                if (this.phase == null) {
                    this.phase = new PhaseOne(this);
                }
                this.phase.handleMessage(message);
            }
        }
    }

    private void processActivation() {
        this.phase = new PhaseTwo(this);

        AgentId label = getName();
        sendMessage(new FireRequestMessage(label, generateMessageId(), Collections.singleton(label), new HashSet<>()));
    }

    private void processDependencyGraphBuilt() {
        AgentId label = getName();
        sendMessage(new ActivationMessage(label, generateMessageId(), participatedPrograms));
    }

    private void processInit() {
        AgentId label = getName();
        this.setInitialProgramLabel(label);
        sendMessage(new GetRequestMessage(label, generateMessageId(), Collections.singleton(label), label, new ArrayList<>()));
    }

    public boolean isInitialProgram() {
        return this.initialProgramLabel.equals(getName());
    }

    public void addRule(Rule r) {
        this.rules.add(r);
    }

    /**
     * @return the rules
     */
    public List<Rule> getRules() {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    /**
     * @return the participatedPrograms
     */
    public Set<AgentId> getParticipatedPrograms() {
        return participatedPrograms;
    }

    /**
     * @return the askedLiterals
     */
    public Map<Literal, Set<AgentId>> getAskedLiterals() {
        return askedLiterals;
    }

    /**
     * @return the smallestModel
     */
    public Set<Literal> getSmallestModel() {
        return smallestModel;
    }

    /**
     * @return the initialProgramLabel
     */
    public AgentId getInitialProgramLabel() {
        return initialProgramLabel;
    }

    /**
     * @param initialProgramLabel the initialProgramLabel to set
     */
    public void setInitialProgramLabel(AgentId initialProgramLabel) {
        this.initialProgramLabel = initialProgramLabel;
    }

    /**
     * @return the participationConfirmed
     */
    public boolean isParticipationConfirmed() {
        return participationConfirmed;
    }

    /**
     * @param participationConfirmed the participationConfirmed to set
     */
    public void setParticipationConfirmed(boolean participationConfirmed) {
        this.participationConfirmed = participationConfirmed;
    }

}
