/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp.api;

import java.sql.Timestamp;
import java.util.Objects;

/**
 *
 * @author martin
 * @param <T>
 */
public class Timestamped<T> {

    private final int MAX_DIFF_SECS = 3;
    private final Timestamp time;

    private T value;

    public Timestamped(T value) {
        this.value = value;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public boolean isValid() {
        long timeValueSecs = time.getTime() / 1000;
        long timeNowSecs = (new Timestamp(System.currentTimeMillis())).getTime() / 1000;
        
        return (timeNowSecs - timeValueSecs) <= MAX_DIFF_SECS;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        Timestamped<?> other = (Timestamped<?>) obj;

        return Objects.equals(getValue(), other.getValue());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.getValue());
        return hash;
    }

    /**
     * @return the value
     */
    public T getValue() {
        return value;
    }
}
