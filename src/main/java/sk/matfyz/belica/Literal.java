/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import sk.matfyz.lcp.api.AgentId;

/**
 *
 * @author martin
 */
public abstract class Literal implements Comparable<Literal> {
    protected String value;
    private AgentId programLabel;
    
        /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
        this.setProgramLabel(value.split(":")[0]);
    }

    /**
     * @return the programLabel
     */
    public AgentId getProgramLabel() {
        return programLabel;
    }

    /**
     * @param programLabel the programLabel to set
     */
    public void setProgramLabel(String programLabel) {
        this.programLabel = new AgentId(programLabel);
    }

}
