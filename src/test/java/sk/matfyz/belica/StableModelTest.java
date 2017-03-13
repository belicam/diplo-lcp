package sk.matfyz.belica;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;
import sk.matfyz.belica.messages.InitMessage;
import sk.matfyz.lcp.DefaultPlatform;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Platform;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author martin
 */
public class StableModelTest {

    @Test
    public void testModel1() {
        Platform platform = new DefaultPlatform();

        LogicProgrammingAgent p1 = new LogicProgrammingAgent(platform, new AgentId("agent1"));
        LogicProgrammingAgent p2 = new LogicProgrammingAgent(platform, new AgentId("agent2"));
        LogicProgrammingAgent p3 = new LogicProgrammingAgent(platform, new AgentId("agent3"));

        p1.addRule(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent2:b")));
        p2.addRule(Rule.createRuleHead(new Constant("agent2:b")).addToBody(new Constant("agent3:c")));
        p3.addRule(Rule.createRuleHead(new Constant("agent3:c")));

        Set<Literal> p1model = new HashSet<>();
        p1model.add(new Constant("agent1:a"));
        p1model.add(new Constant("agent2:b"));

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

        Assert.assertEquals(p1.getSmallestModel(), p1model);
    }

    @Test
    public void testModel2() {
        Platform platform = new DefaultPlatform();

        LogicProgrammingAgent p1 = new LogicProgrammingAgent(platform, new AgentId("agent1"));
        LogicProgrammingAgent p2 = new LogicProgrammingAgent(platform, new AgentId("agent2"));
        LogicProgrammingAgent p3 = new LogicProgrammingAgent(platform, new AgentId("agent3"));

        p1.addRule(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent2:b")));
        p2.addRule(Rule.createRuleHead(new Constant("agent2:b")).addToBody(new Constant("agent3:c")));
        p3.addRule(Rule.createRuleHead(new Constant("agent3:c")).addToBody(new Constant("agent1:a")));

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

        Assert.assertEquals(p1.getSmallestModel().isEmpty(), true);
    }

    @Test
    public void testModel3() {
        Platform platform = new DefaultPlatform();

        LogicProgrammingAgent p1 = new LogicProgrammingAgent(platform, new AgentId("agent1"));
        LogicProgrammingAgent p2 = new LogicProgrammingAgent(platform, new AgentId("agent2"));
        LogicProgrammingAgent p3 = new LogicProgrammingAgent(platform, new AgentId("agent3"));
        LogicProgrammingAgent p4 = new LogicProgrammingAgent(platform, new AgentId("agent4"));

        p1.addRule(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent2:b")));
        p1.addRule(Rule.createRuleHead(new Constant("agent1:c")).addToBody(new Constant("agent3:d")));
        p2.addRule(Rule.createRuleHead(new Constant("agent2:b")).addToBody(new Constant("agent3:d")));
        p3.addRule(Rule.createRuleHead(new Constant("agent3:d")).addToBody(new Constant("agent4:e")));
        p4.addRule(Rule.createRuleHead(new Constant("agent4:e")));

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(p1);
        executor.execute(p2);
        executor.execute(p3);
        executor.execute(p4);
        executor.shutdown();

        p1.sendMessage(new InitMessage(p1.getName(), p1.generateMessageId(), Collections.singleton(p1.getName())));

        Set<Literal> p1model = new HashSet<>();
        p1model.add(new Constant("agent1:a"));
        p1model.add(new Constant("agent1:c"));
        p1model.add(new Constant("agent2:b"));
        p1model.add(new Constant("agent3:d"));

        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        Assert.assertEquals(p1.getSmallestModel(), p1model);
    }

    @Test
    public void testModel4() {
        Platform platform = new DefaultPlatform();

        LogicProgrammingAgent p1 = new LogicProgrammingAgent(platform, new AgentId("agent1"));
        LogicProgrammingAgent p2 = new LogicProgrammingAgent(platform, new AgentId("agent2"));
        LogicProgrammingAgent p3 = new LogicProgrammingAgent(platform, new AgentId("agent3"));

        p1.addRule(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent2:b")));
        p2.addRule(Rule.createRuleHead(new Constant("agent2:b")).addToBody(new Constant("agent3:c"), new Constant("agent1:a")));
        p2.addRule(Rule.createRuleHead(new Constant("agent3:c")).addToBody(new Constant("agent2:b")));

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

        Assert.assertEquals(p1.getSmallestModel().isEmpty(), true);
    }

    @Test
    public void testModel5() {
        Platform platform = new DefaultPlatform();

        LogicProgrammingAgent p1 = new LogicProgrammingAgent(platform, new AgentId("agent1"));
        LogicProgrammingAgent p2 = new LogicProgrammingAgent(platform, new AgentId("agent2"));
        LogicProgrammingAgent p3 = new LogicProgrammingAgent(platform, new AgentId("agent3"));

        p1.addRule(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent2:b"), new Constant("agent3:c")));
        p2.addRule(Rule.createRuleHead(new Constant("agent2:b")).addToBody(new Constant("agent1:a"), new Constant("agent3:c")));
        p2.addRule(Rule.createRuleHead(new Constant("agent3:c")).addToBody(new Constant("agent1:a"), new Constant("agent2:b")));

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

        Assert.assertEquals(p1.getSmallestModel().isEmpty(), true);
    }

    @Test
    public void testModel6() {
        Platform platform = new DefaultPlatform();

        LogicProgrammingAgent p1 = new LogicProgrammingAgent(platform, new AgentId("agent1"));
        LogicProgrammingAgent p2 = new LogicProgrammingAgent(platform, new AgentId("agent2"));
        LogicProgrammingAgent p3 = new LogicProgrammingAgent(platform, new AgentId("agent3"));

        p1.addRule(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent1:b")));
        p1.addRule(Rule.createRuleHead(new Constant("agent1:b")));
        p2.addRule(Rule.createRuleHead(new Constant("agent2:b")).addToBody(new Constant("agent3:c")));
        p3.addRule(Rule.createRuleHead(new Constant("agent3:c")));

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(p1);
        executor.execute(p2);
        executor.execute(p3);
        executor.shutdown();

        Set<Literal> p1model = new HashSet<>();
        p1model.add(new Constant("agent1:a"));
        p1model.add(new Constant("agent1:b"));

        p1.sendMessage(new InitMessage(p1.getName(), p1.generateMessageId(), Collections.singleton(p1.getName())));

        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        Assert.assertEquals(p1.getSmallestModel(), p1model);
    }

    @Test
    public void testModel7() {
        Platform platform = new DefaultPlatform();

        LogicProgrammingAgent p1 = new LogicProgrammingAgent(platform, new AgentId("agent1"));
        LogicProgrammingAgent p2 = new LogicProgrammingAgent(platform, new AgentId("agent2"));
        LogicProgrammingAgent p3 = new LogicProgrammingAgent(platform, new AgentId("agent3"));
        LogicProgrammingAgent p4 = new LogicProgrammingAgent(platform, new AgentId("agent4"));

        p1.addRule(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent1:b")));
        p1.addRule(Rule.createRuleHead(new Constant("agent1:b")).addToBody(new Constant("agent2:a")));
        p2.addRule(Rule.createRuleHead(new Constant("agent2:a")));
        p3.addRule(Rule.createRuleHead(new Constant("agent3:c")).addToBody(new Constant("agent4:d")));
        p4.addRule(Rule.createRuleHead(new Constant("agent4:d")));

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(p1);
        executor.execute(p2);
        executor.execute(p3);
        executor.execute(p4);
        executor.shutdown();

        Set<Literal> p1model = new HashSet<>();
        p1model.add(new Constant("agent1:a"));
        p1model.add(new Constant("agent1:b"));
        p1model.add(new Constant("agent2:a"));

        p1.sendMessage(new InitMessage(p1.getName(), p1.generateMessageId(), Collections.singleton(p1.getName())));

        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        Assert.assertEquals(p1.getSmallestModel(), p1model);
    }
}
