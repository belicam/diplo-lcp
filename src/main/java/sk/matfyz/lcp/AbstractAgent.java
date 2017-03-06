package sk.matfyz.lcp;

import sk.matfyz.lcp.api.Agent;
import sk.matfyz.lcp.api.AgentId;
import sk.matfyz.lcp.api.EventSource;
import sk.matfyz.lcp.api.Message;
import sk.matfyz.lcp.api.MessageId;
import sk.matfyz.lcp.api.MessageReceivedEvent;
import sk.matfyz.lcp.api.Platform;

public class AbstractAgent implements Agent {

    private Platform platform;
    private AgentId name;
    private EventSource<MessageReceivedEvent> messageReceivedEventSource = new EventSourceImpl<MessageReceivedEvent>();

    private long lastMessageId;

    public AbstractAgent(Platform platform) {
        this(platform, new AgentId());
    }

    public AbstractAgent(Platform platform, AgentId name) {
        if (platform == null) {
            throw new NullPointerException();
        }

        if (name == null) {
            throw new NullPointerException();
        }

        this.platform = platform;
        this.name = name;

        platform.getLocalAgentCollection().register(this);
    }

    public AgentId getName() {
        return name;
    }

    public void sendMessage(Message msg) {
        platform.getMessageTransportService().sendMessage(msg);
    }

    public EventSource<MessageReceivedEvent> getMessageReceivedSource() {
        return messageReceivedEventSource;
    }

    /**
     * @return the lastMessageId
     */
    public long getLastMessageId() {
        return lastMessageId;
    }

    public MessageId generateMessageId() {
        return new MessageId(this.lastMessageId++);
    }

}
