/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.matfyz.belica.messages.InitMessage;
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

        List<LogicProgrammingAgent> agents = new ArrayList<>();
//        for (int i = 0; i < 1; i++) {
//            agents.add(new LogicProgrammingAgent(platform, new AgentId("agent" + (i+1)*5)));
//        }
        agents.add(new LogicProgrammingAgent(platform, new AgentId("agent2")));

//        agents.get(0).addRule(Rule.createRuleHead(new Constant("agent1:a")).addToBody(new Constant("agent2:b")));
        agents.get(0).addRule(Rule.createRuleHead(new Constant("agent2:b")).addToBody(new Constant("agent3:c")));
//        agents.get(2).addRule(Rule.createRuleHead(new Constant("agent3:c")));
//
        ExecutorService executor = Executors.newCachedThreadPool();
        agents.forEach(a -> executor.execute(a));
//        executor.execute(p1);
//        executor.execute(p2);
//        executor.execute(p3);
//        executor.shutdown();
//

        new Thread(() -> {
            int i = 0;
            while (i < 3) {
                try {
                    i++;
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            agents.get(0).sendMessage(new InitMessage(agents.get(0).getName(), agents.get(0).generateMessageId(), Collections.singleton(agents.get(0).getName())));
        }).start();
//
//        try {
//            executor.awaitTermination(1, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//        }
    }
}
