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
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.AgentInfo;
import sk.matfyz.lcp.api.Discovery;
import sk.matfyz.lcp.api.DiscoveryService;
import sk.matfyz.lcp.api.LcpUtils;
import sk.matfyz.lcp.api.TransportAddress;

/**
 *
 * @author martin
 */
public class UdpDiscovery implements Discovery {

    private DiscoveryService ds;

    final String PROTOCOL = "tcp";
    int SOCKET_PORT;

    final int MAX_DATA_SIZE = 65507; // 65535 - 20(ip header) - 8(udp header)

    private Set<AgentInfo> localAgents = new CopyOnWriteArraySet<>();

    private DatagramSocket socket;
    private byte[] buf = new byte[MAX_DATA_SIZE];
    private DatagramPacket dp = new DatagramPacket(buf, buf.length);

    private Thread sendingThread;
    private Thread receivingThread;

    public UdpDiscovery(DiscoveryService ds) {
        this.ds = ds;

        try {
            socket = new DatagramSocket(0);
            socket.setBroadcast(true);
            SOCKET_PORT = socket.getLocalPort();
        } catch (SocketException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        }

        setupThreads();
    }

    public UdpDiscovery(DiscoveryService ds, int port) {
        this.ds = ds;

        try {
            socket = new DatagramSocket(port);
            socket.setBroadcast(true);
            SOCKET_PORT = port;
        } catch (SocketException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        }

        setupThreads();
    }

    private void setupThreads() {
        sendingThread = new Thread(() -> {
            while (true) {
                try {
                    broadcast(localAgents);
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        receivingThread = new Thread(() -> {
            while (true) {
                try {
                    socket.receive(dp);
//                    if excludes registering own broadcasted packets
//                    if (!dp.getAddress().getHostAddress().equals(InetAddress.getLocalHost().getHostAddress())) {
                        receiveData(dp);
//                    }
                } catch (IOException ex) {
                    Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    @Override
    public void start() {
        sendingThread.start();
        receivingThread.start();
    }

    @Override
    public void stop() {
        sendingThread.interrupt();
        receivingThread.interrupt();
    }

    @Override
    public void broadcast(Collection<AgentInfo> agentsToBroadcast) {
        try {
            List<AgentInfo> agentsList = new ArrayList<>(agentsToBroadcast);
            byte[] msg = LcpUtils.serialize(agentsList);

            while (msg.length > MAX_DATA_SIZE) {
                agentsList.remove(agentsList.size() - 1);
                msg = LcpUtils.serialize(agentsList);
            }

            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(msg, msg.length, broadcastAddress, SOCKET_PORT);

//            System.out.println("UdpDiscovery " + InetAddress.getLocalHost().getHostAddress() + " sent: " + agentsList.size());
            socket.send(packet);

            List<AgentInfo> remainingAgents = agentsToBroadcast
                    .stream()
                    .filter((ainfo) -> !agentsList.contains(ainfo))
                    .collect(Collectors.toList());

            if (!remainingAgents.isEmpty()) {
                broadcast(remainingAgents);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Set<AgentInfo> getLocalAgents() {
        return localAgents;
    }

    private void receiveData(DatagramPacket dp) {
        byte[] data = new byte[dp.getLength()];
        System.arraycopy(dp.getData(), 0, data, 0, dp.getLength());

        Collection<AgentInfo> recievedAgents = (Collection<AgentInfo>) LcpUtils.deserialize(data);

        recievedAgents.forEach((ainfo) -> {
            ds.registerExternalAgent(ainfo);
        });
    }

    @Override
    public void registerLocalAgent(AgentInfo a) {
        localAgents.add(a);
    }

    @Override
    public TransportAddress getTransportAddress() {
        try {
            return new TransportAddress(PROTOCOL, InetAddress.getLocalHost().getHostAddress(), SOCKET_PORT);
        } catch (UnknownHostException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void deregisterLocalAgent(AgentId agentId) {
        AgentInfo agentInfo = new AgentInfo(agentId);
        localAgents.remove(agentInfo);
    }

}
