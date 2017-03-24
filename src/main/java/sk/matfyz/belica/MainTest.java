/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import sk.matfyz.belica.messages.InitMessage;
import sk.matfyz.lcp.DefaultPlatform;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Platform;

/**
 *
 * @author martin
 */
public class MainTest {

    public static void main(String[] arg) {
        Platform platform = new DefaultPlatform();

//        LogicProgrammingAgent p1 = new LogicProgrammingAgent(platform, new AgentId("agent1"));
//        LogicProgrammingAgent p2 = new LogicProgrammingAgent(platform, new AgentId("agent2"));
//        LogicProgrammingAgent p3 = new LogicProgrammingAgent(platform, new AgentId("agent3"));
//
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
