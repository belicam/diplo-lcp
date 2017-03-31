/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.AgentInfo;
import sk.matfyz.lcp.api.Discovery;
import sk.matfyz.lcp.api.DiscoveryService;
import sk.matfyz.lcp.api.TransportAddress;

/**
 *
 * @author martin
 */
public class UdpDiscovery implements Discovery {

    private DiscoveryService ds;

    final String PROTOCOL = "udp";
    final int SOCKET_PORT = 8888;

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
            socket = new DatagramSocket(SOCKET_PORT);
            socket.setBroadcast(true);
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
                    receiveData(dp);
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
            byte[] msg = serializeAgents(agentsList);
            
            while (msg.length > MAX_DATA_SIZE) {
                agentsList.remove(agentsList.size() - 1);
                msg = serializeAgents(agentsList);
            }

            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(msg, msg.length, broadcastAddress, SOCKET_PORT);

            System.out.println(InetAddress.getLocalHost().getHostAddress() + " sent: " + agentsList.size());

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

    private byte[] serializeAgents(Collection<AgentInfo> agents) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;

        byte[] result = null;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(agents);
            out.flush();
            result = bos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {

            }
        }
        return result;
    }

    private Collection<AgentInfo> deserializeAgents(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return (Collection<AgentInfo>) o;
    }

    public Set<AgentInfo> getLocalAgents() {
        return localAgents;
    }

    private void receiveData(DatagramPacket dp) {
        byte[] data = new byte[dp.getLength()];
        System.arraycopy(dp.getData(), 0, data, 0, dp.getLength());

        Collection<AgentInfo> recievedAgents = deserializeAgents(data);

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
