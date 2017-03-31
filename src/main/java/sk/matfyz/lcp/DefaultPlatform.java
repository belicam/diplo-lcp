package sk.matfyz.lcp;

import sk.matfyz.lcp.api.Platform;
import sk.matfyz.lcp.api.DirectoryService;
import sk.matfyz.lcp.api.Discovery;
import sk.matfyz.lcp.api.DiscoveryService;
import sk.matfyz.lcp.api.MessageTransportService;
import sk.matfyz.lcp.api.LocalAgentCollection;
import sk.matfyz.lcp.api.MessageTransport;

public class DefaultPlatform implements Platform {

    DirectoryService ds = new DirectoryServiceImpl();
    MessageTransportService mts = new MessageTransportServiceImpl(this);
    LocalAgentCollection lac = new LocalAgentCollectionImpl();
    DiscoveryService diss = new DiscoveryServiceImpl();

    public DefaultPlatform() {
        Discovery udpChannel = new UdpDiscovery(diss);
        MessageTransport tcpTransport = new TcpMessageTransport();
        
        mts.registerTransport(tcpTransport);
        diss.registerDiscovery(udpChannel);

        lac.register(diss);
        ds.registerDS(diss);
        
        udpChannel.start();
    }

    public DirectoryService getDirectoryService() {
        return ds;
    }

    public MessageTransportService getMessageTransportService() {
        return mts;
    }

    public LocalAgentCollection getLocalAgentCollection() {
        return lac;
    }

    public DiscoveryService getDiscoveryService() {
        return diss;
    }
}
