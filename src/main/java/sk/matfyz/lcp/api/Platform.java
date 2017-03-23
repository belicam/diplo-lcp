package sk.matfyz.lcp.api;

public interface Platform
{
	public DirectoryService getDirectoryService();
	public MessageTransportService getMessageTransportService();
	public LocalAgentCollection getLocalAgentCollection();
	public DiscoveryService getDiscoveryService();
}
