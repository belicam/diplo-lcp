package sk.matfyz.lcp.api;

import java.io.Serializable;
import java.net.URL;
import java.util.Set;

public interface Envelope extends Serializable {
	
	public Message getMessage();
	public Set<URL> getRecipients();
	
}
