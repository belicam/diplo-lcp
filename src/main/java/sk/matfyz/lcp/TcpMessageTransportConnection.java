/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import sk.matfyz.lcp.api.Envelope;
import sk.matfyz.lcp.api.LcpUtils;

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
//        while (acceptMessage());
        acceptMessage();

        try {
            socket.close();
            messageTransport.disconnect(this);
        } catch (IOException ex) {
            Logger.getLogger(TcpMessageTransportConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean acceptMessage() {
//        System.out.println("sk.matfyz.lcp.TcpMessageTransportConnection.acceptMessage()");
        try {
            byte[] messageSize = new byte[4];
            int i = 0; // how many bytes did we read so far
            do {
                int j = input.read(messageSize, i, messageSize.length - i);
                if (j > 0) {
                    i += j;
                } else {
                    break;
                }
            } while (i < messageSize.length);

            byte[] messageBytes = new byte[LcpUtils.bytesToInt(messageSize)];
            i = 0; // how many bytes did we read so far
            do {
                int j = input.read(messageBytes, i, messageBytes.length - i);
                if (j > 0) {
                    i += j;
                } else {
                    break;
                }
            } while (i < messageBytes.length);

            Envelope env = (EnvelopeImpl) LcpUtils.deserialize(messageBytes);
            messageTransport.getEnvelopeReceivedSource().postEvent(new EnvelopeReceivedEventImpl(env));
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
