package sk.matfyz.lcp.api;

import java.net.URL;

public interface MessageTransport {

	public void postMessage(Envelope env);
	public EventSource<EnvelopeReceivedEvent> getEnvelopeReceivedSource();
	
	public boolean accepts(String protocolName);

	public TransportAddress agentToTransport(AgentId agentId);
	public AgentId transportToAgent(TransportAddress transportAddress);
}
