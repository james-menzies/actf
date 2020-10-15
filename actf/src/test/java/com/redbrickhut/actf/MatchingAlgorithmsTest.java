package com.redbrickhut.actf;

import org.junit.Test;

import static org.junit.Assert.*;
import static com.redbrickhut.actf.MatchingAlgorithms.*;


public class MatchingAlgorithmsTest {

    @Test
    public void substringEmptyString() {
        assertTrue(SUB_STRING_MATCH.test("", "hello"));
    }

    @Test
    public void substringMatchesFromStart() {
        assertTrue(SUB_STRING_MATCH.test("he", "hello"));
    }

    @Test
    public void substringMatchesFromMiddle() {
        assertTrue(SUB_STRING_MATCH.test("ell", "hello"));
    }

    @Test
    public void substringWholeStringMatches() {
        assertTrue(SUB_STRING_MATCH.test("hello", "hello"));
    }

    @Test
    public void substringCaseInsensitive() {
        assertTrue(SUB_STRING_MATCH.test("ell", "HELLO"));
    }

    @Test
    public void substringSampleFalse1() {
        assertFalse(SUB_STRING_MATCH.test("kek", "hello"));
    }

    @Test
    public void substringPartialMatchFails() {
        assertFalse(SUB_STRING_MATCH.test("elo", "Hello"));
    }

    @Test
    public void exactEmptyString() {
        assertTrue(EXACT_MATCH.test("", "hello"));
    }

    @Test
    public void exactStandardTrue() {
        assertTrue(EXACT_MATCH.test("He", "Hello"));
    }

    @Test
    public void exactCaseInsensitive() {
        assertTrue(EXACT_MATCH.test("he", "Hello"));
    }

    @Test
    public void exactStandardFalse() {
        assertFalse(EXACT_MATCH.test("kdk", "hello"));
    }

    @Test
    public void exactMidStringFalse() {
        assertFalse(EXACT_MATCH.test("ell", "Hello"));
    }
}
