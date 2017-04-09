/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Envelope;
import sk.matfyz.lcp.api.EnvelopeReceivedEvent;
import sk.matfyz.lcp.api.EventListener;
import sk.matfyz.lcp.api.EventSource;
import sk.matfyz.lcp.api.Message;
import sk.matfyz.lcp.api.MessageTransport;
import sk.matfyz.lcp.api.MessageTransportService;
import sk.matfyz.lcp.api.TransportAddress;
import sk.matfyz.lcp.api.LcpUtils;

/**
 *
 * @author martin
 */
public class TcpMessageTransport implements MessageTransport, EventListener<EnvelopeReceivedEvent> {

    final List<String> ACCEPTED_PROTOCOLS = Arrays.asList("tcp");
    private int port;

    private EventSource<EnvelopeReceivedEvent> envelopeReceivedEventSource = new EventSourceImpl<>();

    private ServerSocket serverSocket;

    private Thread listeningThread;
    private List<TcpMessageTransportConnection> connections = new CopyOnWriteArrayList<>();

    private MessageTransportService mts;

    public TcpMessageTransport(MessageTransportService mts) {
        this.mts = mts;
        getEnvelopeReceivedSource().addListener(this);
        
        try {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
        } catch (IOException ex) {
            Logger.getLogger(TcpMessageTransport.class.getName()).log(Level.SEVERE, null, ex);
        }

        setUp();
    }

    public TcpMessageTransport(MessageTransportService mts, int port) {
        this.mts = mts;
        getEnvelopeReceivedSource().addListener(this);

        try {
            serverSocket = new ServerSocket(port);
            this.port = port;
        } catch (IOException ex) {
            Logger.getLogger(TcpMessageTransport.class.getName()).log(Level.SEVERE, null, ex);
        }

        setUp();
    }

    public void start() {
        listeningThread.start();
    }

    public void stop() {
        try {
            listeningThread.interrupt();
            connections.forEach(t -> t.interrupt());
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(TcpMessageTransport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setUp() {
        listeningThread = new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    acceptConnection(socket);
                } catch (IOException ex) {
                    Logger.getLogger(TcpMessageTransport.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void acceptConnection(Socket socket) {
        TcpMessageTransportConnection newconnection = new TcpMessageTransportConnection(this, socket);

        connections.add(newconnection);
        newconnection.start();
    }

    public void disconnect(TcpMessageTransportConnection connection) {
        connection.interrupt();
        connections.remove(connection);
    }

    @Override
    public void postMessage(Envelope env) {
        byte[] envelopeBytes = LcpUtils.serialize(env);
        byte[] envelopeLength = LcpUtils.intToBytes(envelopeBytes.length);

        for (TransportAddress ta : env.getRecipients()) {
            try (Socket socket = new Socket(ta.getHost(), ta.getPort())) {
                socket.getOutputStream().write(envelopeLength, 0, envelopeLength.length);
                socket.getOutputStream().write(envelopeBytes, 0, envelopeBytes.length);
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(TcpMessageTransport.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    @Override
    public EventSource<EnvelopeReceivedEvent> getEnvelopeReceivedSource() {
        return envelopeReceivedEventSource;
    }

    @Override
    public boolean accepts(String protocolName) {
        return ACCEPTED_PROTOCOLS.contains(protocolName);
    }

    @Override
    public TransportAddress agentToTransport(AgentId agentId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AgentId transportToAgent(TransportAddress transportAddress) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onEvent(EnvelopeReceivedEvent e) {
        Message msg = e.getEnvelope().getMessage();
        mts.sendMessage(msg, true);
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }
}
