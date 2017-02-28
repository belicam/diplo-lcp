package sk.matfyz.lcp;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.matfyz.lcp.api.Envelope;
import sk.matfyz.lcp.api.Message;

/**
 *
 * @author shanki
 */
public class EnvelopeImpl implements Envelope {

	private static final long serialVersionUID = 8610242798938182193L;
	
	private final Message message;
	private final Set<URL> recipients = new HashSet<URL>();
	
	public EnvelopeImpl(Message message, URL...receipients) {
		if (message == null) {
			throw new NullPointerException();
		}
		
		List<URL> list = Arrays.asList(receipients);
		
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
	public final Set<URL> getRecipients() {
		return recipients;
	}

}