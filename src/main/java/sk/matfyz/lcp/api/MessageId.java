/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp.api;

/**
 *
 * @author martin
 */
public class MessageId {

    private final long value;
    
    public MessageId(long value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public final long getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object msgId) {
        if (msgId == null) {
            return false;
        }
        
        if (getClass() != msgId.getClass()) {
            return false;
        }
        
        final MessageId other = (MessageId) msgId;
        return getValue() == other.getValue();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.value ^ (this.value >>> 32));
        return hash;
    }
}
