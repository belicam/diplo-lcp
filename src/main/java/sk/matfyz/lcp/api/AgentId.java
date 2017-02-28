package sk.matfyz.lcp.api;

import java.util.Objects;

public class AgentId {
	
	public final static AgentId ROOT = null;
	private final static char DELIMITER = '.';

	private static int count = 0;
	
	private final AgentId parent;
	private final String name;

	public AgentId() {
		this("agent" + (count++));
	}
	
	public AgentId(String name) {
		this(ROOT, name);
	}

	public AgentId(AgentId parent, String name) {
		if (name == null) {
			throw new NullPointerException();
		}
		
		if (!isJavaIdentifier(name)) {
			throw new IllegalArgumentException("Name is not a valid identifier");
		}

		this.parent = parent;
		this.name = name;
	}

	public final AgentId getParent() {
		return parent;
	}

	public final String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		AgentId that = this;

		while (that.parent != ROOT) {
			sb.append(that.name);
			sb.append(DELIMITER);

			that = that.parent;
		}

		sb.append(that.name);

		return sb.toString();
	}

	public static AgentId valueOf(String string) {
		int index = string.indexOf(DELIMITER);
		
		if (index >= 0) {
			String name = string.substring(0, index);
			String parent = string.substring(index + 1);

			return new AgentId(valueOf(parent), name);
		} else {
			return new AgentId(string);
		}
	}



	@Override
	public int hashCode() {
		int hash = 7;
		
		hash = 67 * hash + Objects.hashCode(this.parent);
		hash = 67 * hash + Objects.hashCode(this.name);
		
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final AgentId other = (AgentId) obj;
		
		if (!Objects.equals(this.parent, other.parent)) {
			return false;
		}
		
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		
		return true;
	}
	
	private static final boolean isJavaIdentifier(String str) {
		int length = str.length();
		
		if (length == 0) {
			return false;
		}
		
		int codePoint = str.codePointAt(0);
		
		if (!Character.isJavaIdentifierStart(codePoint)) {
			return false;
		}
		
		for (int index = Character.charCount(codePoint); index < length; index += Character.charCount(codePoint)) {
			codePoint = str.codePointAt(index);
			
			if (!Character.isJavaIdentifierPart(codePoint)) {
				return false;
			}
		}
		
		return true;
	}

}
