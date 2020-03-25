package autocompletetextfield;

import org.junit.Test;

import static org.junit.Assert.*;

public class SmartStringSearchTest {

    @Test
    public void testAccuracy(){
        String object = "Cha, Yue-Hong";


//        assertTrue(SmartStringSearch.test("c", object));
//        assertFalse(SmartStringSearch.test("a", object));
//        assertTrue(SmartStringSearch.test("ch", object));
//        assertFalse(SmartStringSearch.test("ca", object));
//        assertTrue(SmartStringSearch.test("cho", object));
//        assertTrue(SmartStringSearch.test("hoy", object));
//        assertTrue(SmartStringSearch.test("hongy", object));
//
        assertTrue(SmartStringSearch.test("or", "O'Reilly, Geoff"));


    }

}