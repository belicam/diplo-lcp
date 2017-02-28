package sk.matfyz.lcp.api;

import java.net.URL;
import java.util.List;

public interface MessageTransportService {

	public void sendMessage(Message msg);

	public void registerTransport(MessageTransport mt);
	public void deregisterTransport(MessageTransport mt);

	public List<URL> addressForAgent(AgentId agentId);
}
