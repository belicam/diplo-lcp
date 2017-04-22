package sk.matfyz.belica;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import sk.matfyz.belica.messages.ContextEndedMessage;
import sk.matfyz.belica.messages.MessageWithContext;
import sk.matfyz.lcp.DefaultPlatform;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Message;
import sk.matfyz.lcp.api.Platform;

/**
 *
 * @author martin
 */
public class DependencyGraphBuildTest {

    public class TestLogicProgrammingAgent extends LogicProgrammingAgent {

        public TestLogicProgrammingAgent(Platform platform, AgentId id) {
            super(platform, id);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Message msg = getMessages().take();
                    ContextId msgCtxId = ((MessageWithContext) msg).getContextId();
                    Context found = getContexts().get(msgCtxId);

                    if (found == null) {
                        Context newCtx = new Context(msgCtxId, this);
                        getContexts().put(msgCtxId, newCtx);
                    }

                    // do not remove context on context end
                    if (!(msg instanceof ContextEndedMessage)) {
                        getContexts().get(msgCtxId).processMessage(msg);
                    }
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(LogicProgrammingAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Test
    public void testDependencyGraphBuild() {
        Platform platform = new DefaultPlatform();

        LogicProgrammingAgent p1 = new TestLogicProgrammingAgent(platform, new AgentId("agent1"));
        LogicProgrammingAgent p2 = new TestLogicProgrammingAgent(platform, new AgentId("agent2"));
        LogicProgrammingAgent p3 = new TestLogicProgrammingAgent(platform, new AgentId("agent3"));

        p1.getRules().add(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent2:b")));
        p2.getRules().add(Rule.createRuleHead(new Constant("agent2:b")).addToBody(new Constant("agent3:c")));
        p3.getRules().add(Rule.createRuleHead(new Constant("agent3:c")));

        Map<Literal, Set<AgentId>> p2asked = new HashMap<>();
        p2asked.put(new Constant("agent2:b"), new HashSet<>());
        p2asked.get(new Constant("agent2:b")).add(new AgentId("agent1"));

        Map<Literal, Set<AgentId>> p3asked = new HashMap<>();
        p3asked.put(new Constant("agent3:c"), new HashSet<>());
        p3asked.get(new Constant("agent3:c")).add(new AgentId("agent2"));

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(p1);
        executor.execute(p2);
        executor.execute(p3);
        executor.shutdown();

        ContextId contextId = p1.start();

        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        Assert.assertEquals(0, p1.getContexts().get(contextId).getAskedLiterals().size());
        Assert.assertEquals(p2.getContexts().get(contextId).getAskedLiterals(), p2asked);
        Assert.assertEquals(p3.getContexts().get(contextId).getAskedLiterals(), p3asked);
    }
}
