package autocompletetextfield;

import org.junit.Test;

import static org.junit.Assert.*;

public class CamelCaseMatchTest {

    @Test
    public void testAccuracy(){
        String object = "Cha, Yue-Hong";
        assertTrue(CamelCaseMatch.test("c", object));
        assertFalse(CamelCaseMatch.test("a", object));
        assertTrue(CamelCaseMatch.test("ch", object));
        assertFalse(CamelCaseMatch.test("ca", object));
        assertTrue(CamelCaseMatch.test("cho", object));
        assertTrue(CamelCaseMatch.test("hoy", object));
        assertTrue(CamelCaseMatch.test("hongy", object));
        assertTrue(CamelCaseMatch.test("or", "O'Reilly, Geoff"));
    }
}