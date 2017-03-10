/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp.api;

import java.util.Objects;

/**
 *
 * @author martin
 */
public class MessageId {

    private final Long value;
    
    public MessageId(long value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public final Long getValue() {
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
        return Objects.equals(getValue(), other.getValue());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.value ^ (this.value >>> 32));
        return hash;
    }
    
    @Override
    public String toString() {
        return getValue().toString();
    }
}
