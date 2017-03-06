package sk.matfyz.lcp;

import java.util.Objects;
import sk.matfyz.lcp.api.Message;
import sk.matfyz.lcp.api.MessageId;
import sk.matfyz.lcp.api.AgentId;

import java.util.Set;

public class AbstractMessage implements Message {

    private static final long serialVersionUID = 2056886824795062605L;

    MessageId id = null;
    AgentId sender = null;
    Set<AgentId> rcpts = null;
    String content;

    public AbstractMessage() {
    }

    public AbstractMessage(AgentId sender, MessageId messageId, Set<AgentId> rcpts, String content) {
        setSender(sender);
        setId(messageId);
        setRecepients(rcpts);
        setContent(content);
    }

    public AgentId getSender() {
        return sender;
    }

    public void setSender(AgentId sender) {
        this.sender = sender;
    }

    public Set<AgentId> getRecepients() {
        return rcpts;
    }

    public void addRecepient(AgentId rcpt) {
        rcpts.add(rcpt);
    }

    public void setRecepients(Set<AgentId> rcpts) {
        // TODO copy?
        this.rcpts = rcpts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the id
     */
    public MessageId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(MessageId id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Message) || !(obj.getClass().equals(this.getClass()))) {
            return false;
        }

        Message msg = (Message) obj;
        return msg.getSender().equals(getSender()) && (msg.getId() == getId());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.sender);
        return hash;
    }
}
