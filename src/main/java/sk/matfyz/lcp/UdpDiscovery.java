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
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
    final int MAX_DATA_SIZE = 65507; // 65535 - 20(ip header) - 8(udp header)

    private Map<AgentInfo, Timestamp> localAgents = new ConcurrentHashMap<>(); 
    private Map<AgentInfo, Timestamp> externalAgents = new ConcurrentHashMap<>(); 

    DatagramSocket socket;
    byte[] buf = new byte[MAX_DATA_SIZE];
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
        Thread sendingThread = new Thread(() -> {
            while (true) {
                try {
                    broadcast();
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UdpDiscovery.class.getName()).log(Level.SEVERE, null, ex);
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
        // TODO kontrola, ci sa cela kolekcia do packetu zmesti
        try {
            byte[] msg = serializeAgents(localAgents.keySet());

            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(msg, msg.length, broadcastAddress, SOCKET_PORT);

            System.out.println(InetAddress.getLocalHost().getHostAddress() + " sent: " + localAgents);

            socket.send(packet);
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
                // ignore close exception
            }
        }
        return (Set<AgentInfo>) o;
    }

    public Map<AgentInfo, Timestamp> getLocalAgents() {
        return localAgents;
    }

    public Map<AgentInfo, Timestamp> getExternalAgents() {
        return externalAgents;
    }

    private void receiveData(DatagramPacket dp) {
        byte[] data = new byte[dp.getLength()];
        System.arraycopy(dp.getData(), 0, data, 0, dp.getLength());

        Collection<AgentInfo> recievedAgents = deserializeAgents(data);

        recievedAgents.forEach((ainfo) -> {
            externalAgents.put(ainfo, new Timestamp(System.currentTimeMillis()));
        });
        System.out.println("recieved from " + dp.getAddress() + ": " + externalAgents);
    }

    @Override
    public void registerLocalAgent(AgentInfo a) {
        localAgents.put(a, new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
