package sk.matfyz.lcp;

import sk.matfyz.lcp.api.Platform;
import sk.matfyz.lcp.api.DirectoryService;
import sk.matfyz.lcp.api.Discovery;
import sk.matfyz.lcp.api.DiscoveryService;
import sk.matfyz.lcp.api.MessageTransportService;
import sk.matfyz.lcp.api.LocalAgentCollection;

public class DefaultPlatform implements Platform {

    DirectoryService ds = new DirectoryServiceImpl();
    MessageTransportService mts = new MessageTransportServiceImpl(this);
    LocalAgentCollection lac = new LocalAgentCollectionImpl();
    DiscoveryService diss = new DiscoveryServiceImpl();

    public DefaultPlatform() {
        Discovery udpChannel = new UdpDiscovery(diss, 8888);
        TcpMessageTransport tcpTransport = new TcpMessageTransport(mts, 8888);
        
        mts.registerTransport(tcpTransport);
        diss.registerDiscovery(udpChannel);

        lac.register(diss);
        ds.registerDS(diss);
        
        tcpTransport.start();
        udpChannel.start();
    }

    @Override
    public DirectoryService getDirectoryService() {
        return ds;
    }

    @Override
    public MessageTransportService getMessageTransportService() {
        return mts;
    }

    @Override
    public LocalAgentCollection getLocalAgentCollection() {
        return lac;
    }

    @Override
    public DiscoveryService getDiscoveryService() {
        return diss;
    }
}
