package sk.matfyz.belica;

import java.util.Collections;
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
import sk.matfyz.belica.messages.InitMessage;
import sk.matfyz.lcp.DefaultPlatform;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Platform;

/**
 *
 * @author martin
 */
public class DependencyGraphBuildTest {

    @Test
    public void testDependencyGraphBuild() {
        Platform platform = new DefaultPlatform();

        LogicProgrammingAgent p1 = new LogicProgrammingAgent(platform, new AgentId("agent1"));
        LogicProgrammingAgent p2 = new LogicProgrammingAgent(platform, new AgentId("agent2"));
        LogicProgrammingAgent p3 = new LogicProgrammingAgent(platform, new AgentId("agent3"));

        p1.addRule(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent2:b")));
        p2.addRule(Rule.createRuleHead(new Constant("agent2:b")).addToBody(new Constant("agent3:c")));
        p3.addRule(Rule.createRuleHead(new Constant("agent3:c")));

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

        p1.sendMessage(new InitMessage(p1.getName(), p1.generateMessageId(), Collections.singleton(p1.getName())));

        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        Assert.assertEquals(0, p1.getAskedLiterals().size());
        Assert.assertEquals(p2.getAskedLiterals(), p2asked);
        Assert.assertEquals(p3.getAskedLiterals(), p3asked);
    }
}
