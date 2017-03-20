/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.matfyz.lcp.api.AgentInfo;
import sk.matfyz.lcp.api.Discovery;

/**
 *
 * @author martin
 */
public class UdpDiscovery implements Discovery {

    final int SOCKET_PORT = 8888;

    Collection<AgentInfo> localAgents = new ArrayList<>(); // ku kazdemu agentovi treba timestamp
    Collection<AgentInfo> externalAgents = new ArrayList<>(); // ku kazdemu agentovi treba timestamp

    DatagramSocket socket;

    public UdpDiscovery() {
        try {
            socket = new DatagramSocket(SOCKET_PORT);
            socket.setBroadcast(true);
        } catch (SocketException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        listen na porte => pri zachyteni packetu ukladat agentov do externalAgents
    }

    @Override
    public void broadcast() {
        try {
//            vytvorit packet obsahujuci kolekciu localAgents => broadcastnut

            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
//            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, SOCKET_PORT, broadcastAddress);
//            socket.send(packet);
        } catch (UnknownHostException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
