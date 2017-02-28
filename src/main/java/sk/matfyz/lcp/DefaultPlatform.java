package sk.matfyz.lcp;

import sk.matfyz.lcp.api.Platform;
import sk.matfyz.lcp.api.DirectoryService;
import sk.matfyz.lcp.api.MessageTransportService;
import sk.matfyz.lcp.api.LocalAgentCollection;

public class DefaultPlatform implements Platform
{
	DirectoryService ds = new DirectoryServiceImpl();
	MessageTransportService mts = new MessageTransportServiceImpl(this);
	LocalAgentCollection lac = new LocalAgentCollectionImpl();

	public DefaultPlatform()
	{
	}

	public DirectoryService getDirectoryService()
	{
		return ds;
	}

	public MessageTransportService getMessageTransportService()
	{
		return mts;
	}

	public LocalAgentCollection getLocalAgentCollection()
	{
		return lac;
	}
}
