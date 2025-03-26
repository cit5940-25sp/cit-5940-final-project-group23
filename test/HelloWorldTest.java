import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.beans.Transient;

public class HelloWorldTest {

    @Test
    public void testSayHello() {
        assertEquals("Hello, World!", HelloWorld.sayHello());
    }
    @Test 
    public void testNothing(){
        assertEquals(1,1);
    }
}
