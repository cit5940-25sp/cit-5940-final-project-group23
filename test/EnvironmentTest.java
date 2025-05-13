import org.junit.Test;
import static org.junit.Assert.*;

public class EnvironmentTest {
    @Test
    public void test1() {
        Environment env = new Environment();
        env.declare("a", 1);
        env.declare("b", 2);
        env.assign("a", 3);
        assertEquals(3, env.lookup("a"));
        env.exitScope();
        env.enterScope();
        env.declare("a", 10);
        assertEquals(10, env.lookup("a"));
    }

    @Test
    public void test2() {
        Environment env = new Environment();
        env.declare("a", 1);
        assertThrows(Exception.class, () -> env.declare("a", 2));
        env.exitScope();
        assertThrows(Exception.class, env::exitScope);
        assertThrows(Exception.class, () -> env.declare("a", 2));
        assertThrows(Exception.class, () -> env.assign("a", 2));
        env.enterScope();
        env.declare("a", 10);
        assertThrows(Exception.class, () -> env.assign("aa", 3));
    }
}
