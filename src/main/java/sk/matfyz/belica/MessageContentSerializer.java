/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.MessageId;

/**
 *
 * @author martin
 */
public class MessageContentSerializer {

    public static final String CONTENT_DELIMITER = ";";

    public static String stringifyAgentId(AgentId agentId) {
        return agentId.toString();
    }

    public static String stringifyMessageId(MessageId msg) {
        return msg.toString();
    }

    public static String stringifyLiterals(Set<Literal> lits) {
        return "[" + lits.stream().map(lit -> lit.toString()).collect(Collectors.joining(",")) + "]";
    }

    public static Set<Literal> parseLiterals(String content) {
        if (content == null) {
            return null;
        }

        String trimmed = content.replaceAll("\\[|\\]", "");

        if (trimmed.isEmpty()){
            return new HashSet<>();
        }
        
        List<String> stringLits = Arrays.asList(trimmed.split(","));

        return stringLits
                .stream()
                .map((strlit -> new Constant(strlit)))
                .collect(Collectors.toSet());
    }

    public static MessageId parseMessageId(String content) {
        if (content == null) {
            return null;
        }

        return new MessageId(Long.parseLong(content));
    }

    public static AgentId parseAgentId(String content) {
        if (content == null) {
            return null;
        }

        String agentNameDelim = ".";
        String[] agentNames = content.split(agentNameDelim);
        
        if (agentNames.length == 0) {
            return new AgentId(content);
        }
        
        AgentId agent = new AgentId(agentNames[0]);
        for (int i = 1; i < agentNames.length; i++) {
            agent = new AgentId(agent, agentNames[i]);
        }
        return agent;
    }
}
