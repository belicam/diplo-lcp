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
public interface Phase {
    public void handleMessage(Object message);
    public void sendMessage(AgentId receiverLabel, Object message);
}
