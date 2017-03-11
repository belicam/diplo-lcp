package sk.matfyz.belica;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

        Rule r = new Rule();
        r.setHead(new Constant("agent1:a"));
        r.addToBody(new Constant("agent2:b"));

        p1.addRule(r);

        r = new Rule();
        r.setHead(new Constant("agent2:b"));
        r.addToBody(new Constant("agent3:c"));

        p2.addRule(r);

        r = new Rule();
        r.setHead(new Constant("agent3:c"));

        p3.addRule(r);

        p1.sendMessage(new InitMessage(p1.getName(), p1.generateMessageId(), Collections.singleton(p1.getName())));

        Map<Literal, Set<AgentId>> p2asked = new HashMap<>();
        p2asked.put(new Constant("agent2:b"), new HashSet<>());
        p2asked.get(new Constant("agent2:b")).add(new AgentId("agent1"));

        Map<Literal, Set<AgentId>> p3asked = new HashMap<>();
        p3asked.put(new Constant("agent3:c"), new HashSet<>());
        p3asked.get(new Constant("agent3:c")).add(new AgentId("agent2"));

        Assert.assertEquals(0, p1.getAskedLiterals().size());
        Assert.assertEquals(p2.getAskedLiterals(), p2asked);
        Assert.assertEquals(p3.getAskedLiterals(), p3asked);
    }
}
