/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp;

import java.util.ArrayList;
import java.util.Collection;
import sk.matfyz.lcp.api.AgentInfo;
import sk.matfyz.lcp.api.Discovery;

/**
 *
 * @author martin
 */
public class UdpDiscovery implements Discovery {
    Collection<AgentInfo> localAgents = new ArrayList<>(); // ku kazdemu agentovi treba timestamp
    Collection<AgentInfo> externalAgents = new ArrayList<>(); // ku kazdemu agentovi treba timestamp

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        listen na porte => pri zachyteni packetu ukladat agentov do externalAgents
    }

    @Override
    public void broadcast() {
//        vytvorit packet obsahujuci kolekciu localAgents => broadcastnut
    }
}
