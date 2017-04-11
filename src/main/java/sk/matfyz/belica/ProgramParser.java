/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import sk.matfyz.lcp.api.Agent;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Platform;

/**
 *
 * @author martin
 */
public class ProgramParser {

    public static List<LogicProgrammingAgent> parseStream(Stream<String> lineStream, Platform platform) {
        System.out.println("Parsing programs.");
        List<LogicProgrammingAgent> programs = new ArrayList<>();

        lineStream.forEach((String line) -> {
            line = line.trim().replaceAll(" ", "");
            if (!line.isEmpty()) {
                if (line.charAt(0) == '#') {
                    // create instance of new program
                    String programName = line.substring(1);
                    LogicProgrammingAgent newprogram = new LogicProgrammingAgent(platform, new AgentId(programName));

                    programs.add(newprogram);
                } else {
                    String[] splitted = line.split(":-", 2);
                    Rule r = new Rule();

                    if (!splitted[0].isEmpty()) {
                        r.setHead(new Constant(splitted[0]));
                    }
                    if (!splitted[1].isEmpty()) {
                        String[] bodySplitted = splitted[1].split(",");
                        for (String bodyLit : bodySplitted) {
                            r.addToBody(new Constant(bodyLit));
                        }
                    }

                    if (!programs.isEmpty()) {
                        programs.get(programs.size() - 1).getRules().add(r);
                    }
                }
            }
        });

        System.out.println("Programs parsed.");
        return programs;
    }
}
