/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp;

import java.util.ArrayList;
import java.util.Collection;
import sk.matfyz.lcp.api.DirectoryService;
import sk.matfyz.lcp.api.Discovery;
import sk.matfyz.lcp.api.DiscoveryService;


public class DiscoveryServiceImpl implements DiscoveryService {
    
    Collection<Discovery> discoveryChannels = new ArrayList<>();

    @Override
    public void register(DirectoryService ds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deregister(DirectoryService ds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
