/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import sk.matfyz.belica.messages.InitMessage;
import sk.matfyz.lcp.DefaultPlatform;
import sk.matfyz.lcp.api.Agent;
import sk.matfyz.lcp.api.Platform;

/**
 *
 * @author martin
 */
public class MainTest {

    public static void sendInit(LogicProgrammingAgent agent) {
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
            agent.start();
            agent.start();
        }).start();

    }

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("args[] is empty! Missing required argument for path to file with distributed program and argument specifying if is initial.");
        }

        String pth = args[0];
        boolean isInitial = Boolean.parseBoolean(args[1]);
        
        try {
            Platform platform = new DefaultPlatform();
            Stream<String> programsStream = Files.lines(Paths.get(pth));

            List<LogicProgrammingAgent> agents = ProgramParser.parseStream(programsStream, platform);

            ExecutorService executor = Executors.newCachedThreadPool();
            agents.forEach(a -> executor.execute(a));
            executor.shutdown();

            if (isInitial) {
                sendInit(agents.get(0));
            }

        } catch (IOException ex) {
            Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
