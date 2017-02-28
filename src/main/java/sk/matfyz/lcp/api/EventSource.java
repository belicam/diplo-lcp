package sk.matfyz.lcp.api;

public interface EventSource<E extends Event> {

	public void addListener(EventListener<? super E> listener);
	public void removeListener(EventListener<? super E> listener);
    public void postEvent(E event);

}
