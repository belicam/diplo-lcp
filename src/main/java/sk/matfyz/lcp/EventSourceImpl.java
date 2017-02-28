package sk.matfyz.lcp;

import java.util.HashSet;
import java.util.Set;

import sk.matfyz.lcp.api.Event;
import sk.matfyz.lcp.api.EventListener;
import sk.matfyz.lcp.api.EventSource;

public class EventSourceImpl<E extends Event> implements EventSource<E> {

	private final Set<EventListener<? super E>> listeners = new HashSet<EventListener<? super E>>();

	@Override
	public void addListener(EventListener<? super E> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(EventListener<? super E> listener) {
		listeners.remove(listener);
	}

	@Override
	public void postEvent(E event) {
		for (EventListener<? super E> listener: listeners) {
			listener.onEvent(event);
		}
	}

}
