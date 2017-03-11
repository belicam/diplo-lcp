package sk.matfyz.belica;


import java.util.Collections;
import java.util.HashSet;
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
    public void testModel() {
        Platform platform = new DefaultPlatform();
        
        LogicProgrammingAgent p1 = new LogicProgrammingAgent(platform , new AgentId("agent1"));
        LogicProgrammingAgent p2 = new LogicProgrammingAgent(platform , new AgentId("agent2"));
        LogicProgrammingAgent p3 = new LogicProgrammingAgent(platform , new AgentId("agent3"));

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
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }

        Assert.assertEquals(p1.getSmallestModel(), p1model);
    }

}
