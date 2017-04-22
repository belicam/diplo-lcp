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
import sk.matfyz.belica.messages.ActivationMessage;
import sk.matfyz.belica.messages.ContextEndedMessage;
import sk.matfyz.belica.messages.DependencyGraphBuiltMessage;
import sk.matfyz.belica.messages.FireRequestMessage;
import sk.matfyz.belica.messages.GetRequestMessage;
import sk.matfyz.belica.messages.InitMessage;
import sk.matfyz.belica.messages.MessageWithContext;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.LcpUtils;
import sk.matfyz.lcp.api.Message;

/**
 *
 * @author martin
 */
public class Context {

    private final LogicProgrammingAgent owner;
    private final ContextId contextId;

    private List<Rule> rules;

    private AgentId initialProgramLabel = null;
    private boolean participationConfirmed = false;

    private final Set<AgentId> participatedPrograms = new HashSet<>();
    private final Set<Literal> smallestModel = new HashSet<>();
    private final Map<Literal, Set<AgentId>> askedLiterals = new HashMap<>();

    private Phase phase;

    public Context(ContextId contextId, LogicProgrammingAgent owner) {
        this.contextId = contextId;
        this.owner = owner;
        cloneRules(owner.getRules());
    }

    public void sendMessage(Message msg) {
        getOwner().sendMessage(msg);
    }

    public void processMessage(Message message) {
        if (message != null) {
            if (message instanceof InitMessage) {
                processInit();
            } else if (message instanceof ActivationMessage) {
                processActivation();
            } else if (message instanceof DependencyGraphBuiltMessage) {
                processDependencyGraphBuilt();
            } else if (message instanceof ContextEndedMessage) {
                processContextEnded(message);
            } else {
                if (this.phase == null) {
                    this.phase = new PhaseOne(this);
                }
                this.phase.handleMessage(message);
            }
        }
    }

    private void cloneRules(List<Rule> rules) {
        byte[] serialized = LcpUtils.serialize(rules);
        this.setRules((ArrayList<Rule>) (LcpUtils.deserialize(serialized)));
    }

    private void processActivation() {
        this.phase = new PhaseTwo(this);

        AgentId label = getOwner().getName();
        sendMessage(new FireRequestMessage(label, getOwner().generateMessageId(), getContextId(), Collections.singleton(label), new HashSet<>()));
    }

    private void processDependencyGraphBuilt() {
        AgentId label = getOwner().getName();
        sendMessage(new ActivationMessage(label, getOwner().generateMessageId(), getContextId(), participatedPrograms));
    }

    private void processInit() {
        AgentId label = getOwner().getName();
        this.setInitialProgramLabel(label);
        sendMessage(new GetRequestMessage(label, getOwner().generateMessageId(), getContextId(), Collections.singleton(label), new HashSet<>(), label));
    }
    
    private void processContextEnded(Object message) {
        ContextId ctxId = ((MessageWithContext) message).getContextId();

        getOwner().getContexts().remove(ctxId);
    }

    public boolean isInitialProgram() {
        return this.getInitialProgramLabel().equals(getOwner().getName());
    }

    /**
     * @return the participatedPrograms
     */
    public Set<AgentId> getParticipatedPrograms() {
        return participatedPrograms;
    }

    /**
     * @return the smallestModel
     */
    public Set<Literal> getSmallestModel() {
        return smallestModel;
    }

    /**
     * @return the askedLiterals
     */
    public Map<Literal, Set<AgentId>> getAskedLiterals() {
        return askedLiterals;
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
     * @return the owner
     */
    public LogicProgrammingAgent getOwner() {
        return owner;
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
     * @return the contextId
     */
    public ContextId getContextId() {
        return contextId;
    }

}
