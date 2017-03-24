/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp;

import java.io.IOException;
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

    private Collection<AgentInfo> localAgents = new ArrayList<>(); // ku kazdemu agentovi treba timestamp
    private Collection<AgentInfo> externalAgents = new ArrayList<>(); // ku kazdemu agentovi treba timestamp

    DatagramSocket socket;
    byte[] buf = new byte[1000];
    DatagramPacket dp = new DatagramPacket(buf, buf.length);

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
        System.out.println("sk.matfyz.lcp.UdpDiscovery.run()");
//        listen na porte => pri zachyteni packetu ukladat agentov do externalAgents
        Thread sendingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        broadcast();
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        sendingThread.start();

        while (true) {
            try {              
                socket.receive(dp);
                receiveData(dp);
            } catch (IOException ex) {
                Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void broadcast() {
        try {
//            vytvorit packet obsahujuci kolekciu localAgents => broadcastnut
            String msg = "hi";

            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, broadcastAddress, SOCKET_PORT);
            
            System.out.println(InetAddress.getLocalHost().getHostAddress() + " sent: " + msg);
            
            socket.send(packet);
        } catch (UnknownHostException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Collection<AgentInfo> getLocalAgents() {
        return localAgents;
    }

    public Collection<AgentInfo> getExternalAgents() {
        return externalAgents;
    }
    
    private void receiveData(DatagramPacket dp) {
        System.out.println("recieved from " + dp.getAddress() + ": " + new String(dp.getData(), 0 , dp.getLength()));
    }

}
