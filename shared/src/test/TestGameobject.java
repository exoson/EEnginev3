package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Collection;
import main.Gameobject;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.*;
/**
 *
 * @author Lime
 */
@RunWith(Parameterized.class)
public class TestGameobject {
    
    public String str;
    
    @Parameters
    public static Gameobject gameObject() {
        return new Gameobject();
    }
    
    @Test
    public void testObjectInit() {
        
    }
}
