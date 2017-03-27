/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp.api;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class TransportAddress implements Serializable {

    private String protocol;
    private String host;
    private int port;

    public TransportAddress(String protocol, String host, int port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public TransportAddress(String hostAddress) {
        String[] parts = hostAddress.split(":");

        if (parts.length != 3) {
            try {
                throw new Exception("Input address incorrect. Correct format: \"protocol:host:port\".");
            } catch (Exception ex) {
                Logger.getLogger(TransportAddress.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.protocol = parts[0];
        this.host = parts[1];
        this.port = Integer.parseInt(parts[2]);
    }

    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        TransportAddress other = (TransportAddress) obj;
        
        return Objects.equals(getProtocol(), other.getProtocol()) 
                && Objects.equals(getHost(), other.getHost()) 
                && (getPort() == other.getPort());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.protocol);
        hash = 97 * hash + Objects.hashCode(this.host);
        hash = 97 * hash + this.port;
        return hash;
    }
    
    @Override
    public String toString() {
        return protocol + ":" + host + ":" + port;
    }

}
