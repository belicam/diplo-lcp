/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.matfyz.lcp.api.AgentInfo;
import sk.matfyz.lcp.api.Envelope;
import sk.matfyz.lcp.api.EnvelopeReceivedEvent;
import sk.matfyz.lcp.api.MessageReceivedEvent;

/**
 *
 * @author martin
 */
public class TcpMessageTransportConnection extends Thread {

    TcpMessageTransport messageTransport;
    Socket socket;

    OutputStream output;
    InputStream input;

    public TcpMessageTransportConnection(TcpMessageTransport mt, Socket socket) {
        this.messageTransport = mt;
        this.socket = socket;

        try {
            output = socket.getOutputStream();
            input = socket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(TcpMessageTransportConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (acceptMessage());

        try {
            socket.close();
            messageTransport.disconnect(this);
        } catch (IOException ex) {
            Logger.getLogger(TcpMessageTransportConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean acceptMessage() {
        try {
            int messageSize = input.read() + (input.read() << 8);
            // todo precita zaporne cislo
            byte[] messageBytes = new byte[messageSize];
            int i = 0; // how many bytes did we read so far
            do {
                int j = input.read(messageBytes, i, messageBytes.length - i);
                if (j > 0) {
                    i += j;
                } else {
                    break;
                }
            } while (i < messageBytes.length);
            
            Envelope env = bytesToEnvelope(messageBytes);
            messageTransport.getEnvelopeReceivedSource().postEvent(new EnvelopeReceivedEventImpl(env));
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    private Envelope bytesToEnvelope(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
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
        return (EnvelopeImpl) o;

    }
}
