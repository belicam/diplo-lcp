/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.util.ArrayList;
import java.util.List;
import sk.matfyz.lcp.DefaultPlatform;
import sk.matfyz.lcp.api.Agent;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Platform;

/**
 *
 * @author martin
 */
public class MainTest {

    public static void main(String[] arg) {
        Platform platform = new DefaultPlatform();

        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            agents.add(new LogicProgrammingAgent(platform, new AgentId("agent" + i)));
        }

//        p1.addRule(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent2:b")));
//        p2.addRule(Rule.createRuleHead(new Constant("agent2:b")).addToBody(new Constant("agent3:c")));
//        p3.addRule(Rule.createRuleHead(new Constant("agent3:c")));
//
//        ExecutorService executor = Executors.newCachedThreadPool();
//        executor.execute(p1);
//        executor.execute(p2);
//        executor.execute(p3);
//        executor.shutdown();
//
//        p1.sendMessage(new InitMessage(p1.getName(), p1.generateMessageId(), Collections.singleton(p1.getName())));
//
//        try {
//            executor.awaitTermination(1, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//        }
    }
}
