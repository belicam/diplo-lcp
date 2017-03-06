/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.belica;

import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.Message;

/**
 *
 * @author martin
 */
public interface Phase {
    public void handleMessage(Message message);
    public void sendMessage(Message message);
}
