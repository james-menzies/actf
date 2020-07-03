package autocompletetextfield;

import org.junit.Test;

import static org.junit.Assert.*;

public class CamelCaseMatchTest {

    private String yueHong = "Cha, Yue-Hong";
    private String geoff = "O'Reilly, Geoff";

    @Test
    public void simpleOneLetterMatch(){
        assertTrue(CamelCaseMatch.test("c", yueHong));
    }

    @Test
    public void simpleOneLetterNonMatch() {
        assertFalse(CamelCaseMatch.test("a", yueHong));
    }

    @Test
    public void twoLettersFromSameWordMatch() {
        assertTrue(CamelCaseMatch.test("ch", yueHong));
    }

    @Test
    public void nonMatchAfterSuccessfulMatches() {
        assertFalse(CamelCaseMatch.test("chg", yueHong));
    }

    @Test
    public void midWordTestNewWord() {
        assertTrue(CamelCaseMatch.test("hoy", yueHong));
    }

    @Test
    public void hyphenatedCountedAsWholeWord() {
        assertTrue(CamelCaseMatch.test("yueh", yueHong));
    }

    @Test
    public void correctStackReevaluateAfterInitialMisMatch() {
        assertTrue(CamelCaseMatch.test("cho", yueHong));
    }

    @Test
    public void newWordMatchedAfterWholeWordMatch() {
        assertTrue(CamelCaseMatch.test("hongy", yueHong));
    }

    @Test
    public void oneLetterWordMatch() {
        assertTrue(CamelCaseMatch.test("or", geoff));
    }

    @Test
    public void matchAcceptsHyphenatedWord() {
        assertTrue(CamelCaseMatch.test("yue-hon", yueHong));
    }

    @Test
    public void matchAcceptsApostrophe() {
        assertTrue(CamelCaseMatch.test("o're", geoff));
    }
}