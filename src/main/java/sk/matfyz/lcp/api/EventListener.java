package sk.matfyz.lcp.api;

public interface EventListener<E extends Event> {

	public void onEvent(E e);

}
