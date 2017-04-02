package sk.matfyz.lcp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.matfyz.lcp.api.Envelope;
import sk.matfyz.lcp.api.Message;
import sk.matfyz.lcp.api.TransportAddress;

/**
 *
 * @author shanki
 */
public class EnvelopeImpl implements Envelope {

	private static final long serialVersionUID = 8610242798938182193L;
	
	private final Message message;
	private final Set<TransportAddress> recipients = new HashSet<TransportAddress>();
	
	public EnvelopeImpl(Message message, TransportAddress...receipients) {
		if (message == null) {
			throw new NullPointerException();
		}
		
		List<TransportAddress> list = Arrays.asList(receipients);
		
		if (list.contains(null)) {
			throw new NullPointerException();
		}
		
		this.message = message;
		this.recipients.addAll(list);
	}

	@Override
	public final Message getMessage() {
		return message;
	}

	@Override
	public final Set<TransportAddress> getRecipients() {
		return recipients;
	}

}