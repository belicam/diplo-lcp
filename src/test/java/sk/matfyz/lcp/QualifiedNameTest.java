package sk.matfyz.lcp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sk.matfyz.lcp.api.AgentId;

/**
 *
 * @author shanki
 */
public class QualifiedNameTest {

    @Test
    public void testGetName() {
        AgentId instance = new AgentId(AgentId.ROOT, "name");
        
        assertEquals("name", instance.getName());
    }

    @Test
    public void testGetParent() {
        AgentId parent = new AgentId(AgentId.ROOT, "parent");
        AgentId instance = new AgentId(parent, "name");
        
        assertEquals(parent, instance.getParent());
    }

    @Test
    public void testToString() {
        AgentId instance = new AgentId(new AgentId(AgentId.ROOT, "level2"), "level1");
        
        assertEquals("level1.level2", instance.toString());
    }
    
    @Test
    public void testSingleToString() {
        AgentId instance = new AgentId(AgentId.ROOT, "level1");
        
        assertEquals("level1", instance.toString());
    }    

    @Test
    public void testValueOf() {
        AgentId instance = new AgentId(new AgentId(AgentId.ROOT, "level2"), "level1");
        
        assertEquals(instance, AgentId.valueOf("level1.level2"));
    }

    @Test
    public void testValueOfOneLevel() {
        AgentId instance = new AgentId(AgentId.ROOT, "level1");
        
        assertEquals(instance, AgentId.valueOf("level1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmpty() {
        
        AgentId.valueOf("");
    }

}