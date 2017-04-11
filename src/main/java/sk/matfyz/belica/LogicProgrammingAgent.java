/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.matfyz.belica.messages.InitMessage;
import sk.matfyz.belica.messages.MessageWithContext;
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
public class LogicProgrammingAgent extends AbstractAgent implements EventListener<MessageReceivedEvent>, Runnable {

    private List<Rule> rules = new ArrayList<>();

    private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    private final Map<ContextId, Context> contexts = new HashMap<>();
    private long lastContextId = 0;

    public LogicProgrammingAgent(Platform platform) {
        super(platform, new AgentId());
        getMessageReceivedSource().addListener(this);
    }

    public LogicProgrammingAgent(Platform platform, AgentId name) {
        super(platform, name);
        getMessageReceivedSource().addListener(this);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = getMessages().take();
                ContextId msgCtxId = ((MessageWithContext) msg).getContextId();
                Context found = contexts.get(msgCtxId);
                
                if (found == null) {
                    Context newCtx = new Context(msgCtxId, this);
                    contexts.put(msgCtxId, newCtx);
                }
                
                contexts.get(msgCtxId).processMessage(msg);
            } catch (InterruptedException ex) {
                Logger.getLogger(LogicProgrammingAgent.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public void onEvent(MessageReceivedEvent e) {
        System.out.println("received: " + e.getMessage());
        getMessages().add(e.getMessage());
    }

    public ContextId start() {
        ContextId newCtxId = new ContextId(getName(), lastContextId++);
        Context newCtx = new Context(newCtxId, this);

        contexts.put(newCtxId, newCtx);

        sendMessage(new InitMessage(getName(), generateMessageId(), newCtxId, Collections.singleton(getName())));

        return newCtxId;
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
     * @return the contexts
     */
    public Map<ContextId, Context> getContexts() {
        return contexts;
    }

    /**
     * @return the messages
     */
    public BlockingQueue<Message> getMessages() {
        return messages;
    }

}
