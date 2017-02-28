package sk.matfyz.lcp;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.EventListener;
import sk.matfyz.lcp.api.Message;
import sk.matfyz.lcp.api.MessageReceivedEvent;
import sk.matfyz.lcp.api.Platform;

public class LocalAgentTest {

/*
	@Test
	public void testOne()
	{
		DefaultPlatform platform = new DefaultPlatform();
		AbstractAgent a = new AbstractAgent(platform, new AgentId("prvy"));
	}
*/

	public class AgentSender extends AbstractAgent
	{
		public AgentSender(Platform platform, AgentId id)
		{
			super(platform, id);
		}

		public void act(AgentId rec, String what) {
			Message msg = new AbstractMessage(
				getName(),
				Collections.singleton(rec),
				what
			);
			
			sendMessage(msg);
		}
	}

	public class AgentReceiver extends AbstractAgent implements EventListener<MessageReceivedEvent>
	{
		public String received = null;

		public AgentReceiver(Platform platform, AgentId id)
		{
			super(platform, id);
			
			getMessageReceivedSource().addListener(this);
		}

		public void onEvent(MessageReceivedEvent ev)
		{
			received = ev.getMessage().getContent();
			System.out.println("Received: " + ev.getMessage().getContent());
		}
	}

	@Test
	public void testTwo()
	{
		DefaultPlatform platform = new DefaultPlatform();
		AgentSender sender = new AgentSender(platform, new AgentId("sender"));
		AgentReceiver receiver = new AgentReceiver(platform, new AgentId("receiver"));

		assertEquals(null, receiver.received);
		sender.act(
			receiver.getName(),
			"hi"
		);
		assertEquals("hi", receiver.received);
	}
}
