package autocompletetextfield;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CamelCaseMatchTest {

    private String youHong = "Cha, Yue-Hong";
    private String geoff = "O'Reilly, Geoff";

    @Test
    public void simpleOneLetterMatch(){
        assertTrue(CamelCaseMatch.test("c", youHong));
    }

    @Test
    public void simpleOneLetterNonMatch() {
        assertFalse(CamelCaseMatch.test("a", youHong));
    }

    @Test
    public void twoLettersFromSameWordMatch() {
        assertTrue(CamelCaseMatch.test("ch", youHong));
    }

    @Test
    public void nonMatchAfterSuccessfulMatches() {
        assertFalse(CamelCaseMatch.test("chg", youHong));
    }

    @Test
    public void midWordTestNewWord() {
        assertTrue(CamelCaseMatch.test("hoy", youHong));
    }

    @Test
    public void hyphenatedCountedAsWholeWord() {
        assertTrue(CamelCaseMatch.test("yueh", youHong));
    }

    @Test
    public void correctStackReevaluateAfterInitialMisMatch() {
        assertTrue(CamelCaseMatch.test("cho", youHong));
    }

    @Test
    public void newWordMatchedAfterWholeWordMatch() {
        assertTrue(CamelCaseMatch.test("hongy", youHong));
    }

    @Test
    public void oneLetterWordMatch() {
        assertTrue(CamelCaseMatch.test("or", geoff));
    }
}