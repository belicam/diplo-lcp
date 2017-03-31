/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Envelope;
import sk.matfyz.lcp.api.EnvelopeReceivedEvent;
import sk.matfyz.lcp.api.EventSource;
import sk.matfyz.lcp.api.MessageTransport;
import sk.matfyz.lcp.api.TransportAddress;

/**
 *
 * @author martin
 */
public class TcpMessageTransport implements MessageTransport {

    final List<String> ACCEPTED_PROTOCOLS = Arrays.asList("tcp", "udp");
    ServerSocket server;

    @Override
    public void postMessage(Envelope env) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EventSource<EnvelopeReceivedEvent> getEnvelopeReceivedSource() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
