package sk.matfyz.lcp.api;

import java.net.URL;

public interface Discovery extends Runnable {
    void broadcast();
    void registerLocalAgent(AgentInfo a);
    URL getURL();
}
