package sk.matfyz.lcp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sk.matfyz.lcp.api.Agent;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.DirectoryService;
import sk.matfyz.lcp.api.Envelope;
import sk.matfyz.lcp.api.LocalAgentCollection;
import sk.matfyz.lcp.api.Message;
import sk.matfyz.lcp.api.MessageReceivedEvent;
import sk.matfyz.lcp.api.MessageTransport;
import sk.matfyz.lcp.api.MessageTransportService;
import sk.matfyz.lcp.api.Platform;

/**
 *
 * @author shanki
 */
public class MessageTransportServiceImpl implements MessageTransportService {
	
	private Platform platform;
	private Collection<MessageTransport> transports = new ArrayList<MessageTransport>();
	
	public MessageTransportServiceImpl(Platform platform) {
		if (platform == null) {
			throw new NullPointerException("Platform cannot be null");
		}

		this.platform = platform;
	}
	
	private void sendMessageTo(Message msg, AgentId receiver) {
		LocalAgentCollection local = platform.getLocalAgentCollection();
		
		Agent agent = local.isLocal(receiver);
		
		if (agent != null) {
			sendLocal(msg, agent);
		} else {
			sendRemote(msg, receiver);
		}

	}
	
	private void sendLocal(Message message, Agent receiver) {
		receiver.getMessageReceivedSource().postEvent(new MessageReceivedEvent(message));
	}
	
	private void sendRemote(Message message, AgentId receiver) {
		DirectoryService ds = platform.getDirectoryService();
		
		for (URL address : ds.lookup(receiver).getTransportAddresses()) {
			String protocol = address.getProtocol();
			
			for (MessageTransport mt : transports) {
				if (mt.accepts(protocol)) {
					Envelope envelope = new EnvelopeImpl(message, address);
					mt.postMessage(envelope);
					return;
				}
			}			
		}		
	}
	

	@Override
	public void sendMessage(Message msg) {
		for (AgentId receiverId : msg.getRecepients()) {
			sendMessageTo(msg, receiverId);
		}		
	}

	@Override
	public void registerTransport(MessageTransport mt) {
		transports.add(mt);
	}

	@Override
	public void deregisterTransport(MessageTransport mt) {
		transports.remove(mt);
	}

	@Override
	public List<URL> addressForAgent(AgentId agentId) {
		List<URL> addresses = new ArrayList<URL>();
		
		for (MessageTransport mt : transports) {
			addresses.add(mt.agentToTransport(agentId));
		}
		
		return addresses;
	}
	
}