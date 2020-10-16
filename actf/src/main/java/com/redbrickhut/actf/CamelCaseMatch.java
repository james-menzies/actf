package com.redbrickhut.actf;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class CamelCaseMatch {

    private final String input;
    private final String object;
    private Deque<Option> options;
    private boolean stringParsed = false;
    private List<String> availableWords;
    private static final Pattern DEFAULT_DELIMITER = Pattern.compile("[^\\w]+");

    private CamelCaseMatch(String input, String object, List<String> availableWords) {
        this.input = input.toLowerCase();
        this.object = object;
        this.availableWords = availableWords;
        options = new ArrayDeque<>();
    }

    private static List<String> createAvailableWords(Pattern delimiter, String object) {

        Stream<String> availableWords = Stream.of(delimiter.split(object));

        return availableWords
                .map(String::toLowerCase)
                .collect(Collectors.toUnmodifiableList());
    }

    private static Pattern createCustomDelimiter (char[] customWordChars) {

        String regex = "[^\\w%s]+";

        StringBuilder sb = new StringBuilder();
        for (char ch : customWordChars) {
            sb.append(ch);
        }

        String insert = sb.toString();
        return Pattern.compile(String.format(regex, insert));
    }

    private static boolean customNonWordCharsRequired( String object, char... customWordChars) {

        for (char ch : customWordChars) {
            if (object.indexOf(ch) != -1) {
                return true;
            }
        }
        return false;
    }

    static boolean test(String input, String object, char... customWordChars) {

        if (input.equals("")) return true;

        boolean altWordsRequired = customWordChars.length > 0 &&
                customNonWordCharsRequired(object, customWordChars);

        List<String> defaultAvailableWords = createAvailableWords(DEFAULT_DELIMITER, object);
        boolean defaultMatches = new CamelCaseMatch(input, object, defaultAvailableWords).matches();
        if (defaultMatches) {
            return true;
        } else if (altWordsRequired) {
            Pattern customDelimiter = createCustomDelimiter(customWordChars);
            List<String> altAvailableWords = createAvailableWords(customDelimiter, object);
            return new CamelCaseMatch(input, object, altAvailableWords).matches();
        }
        else return false;
    }

    private boolean matches() {

        Option firstLetter = new Option(availableWords);
        options.add(firstLetter);

        while (!stringParsed && !options.isEmpty()) {

            Option currentOption = options.pop();

            if (currentOption.isLast()) {
                stringParsed = currentOption.completePath();
            }
            else {
                List<Option> nextBranch = currentOption.explore();
                if (!nextBranch.isEmpty()) {
                    options.addAll(nextBranch);
                }
            }
        }

        return stringParsed;
    }

    private  class Option {

        final private int currentIndexOfInput;
        final private char currentInputChar;

        final private List<String> availableWords;

        final private Optional<String> currentWord;
        final private int currentIndexOfCurrentWord;
        final private Optional<Character> currentWordChar;

        private Option(List<String> availableWords) {

            currentIndexOfInput = 0;
            this.availableWords = availableWords;
            currentIndexOfCurrentWord = 0;
            currentWord = Optional.empty();
            currentWordChar = Optional.empty();
            currentInputChar = input.charAt(currentIndexOfInput);
        }

        private Option(int currentIndexOfInput, List<String> availableWords,
                       int currentIndexOfCurrentWord, Optional<String> currentWord) {

            this.currentIndexOfCurrentWord = currentIndexOfCurrentWord;
            this.currentIndexOfInput = currentIndexOfInput;
            this.currentWord = currentWord;
            this.availableWords = availableWords;
            currentInputChar = input.charAt(currentIndexOfInput);
            currentWordChar = currentWord.map

                    (s -> s.charAt(currentIndexOfCurrentWord));
            }

        private List<Option> explore() {

            List<Option> nextBranch = new ArrayList<>();

            if (currentInputChar == ' ') {
                nextBranch.add(getNewOptionDestroyCurrentWord(availableWords));
            }

            if (matchesCurrentWord()) {

                boolean endOfCurrentWordReached =
                        currentIndexOfCurrentWord == currentWord.get().length() - 1;

                nextBranch.add(endOfCurrentWordReached?
                        getNewOptionDestroyCurrentWord(availableWords):
                        getNewOptionPreserveCurrentWord());
            }

            getNewWordMatches().stream()
                    .forEach( word -> {
                        if (word.length() > 1){
                            nextBranch.add(getNewOptionReplaceCurrentWord(word));
                        }
                        else {
                            List<String> newAvailableWords = getNewAvailableWords(availableWords, word);
                            nextBranch.add(getNewOptionDestroyCurrentWord(newAvailableWords));
                        }
                    });

            return nextBranch;
        }

        private boolean completePath() {
            return currentInputChar == ' ' ||
                    matchesCurrentWord() ||
                    getNewWordMatches().size() > 0;
        }

        private boolean matchesCurrentWord() {
            return currentWordChar.isPresent() &&
                    currentWordChar.get() == currentInputChar;
        }

        private List<String> getNewWordMatches() {

            return availableWords.stream()
                    .filter( word -> currentInputChar == word.charAt(0))
                    .collect(Collectors.toList());
        }

        private Option getNewOptionPreserveCurrentWord() {

            int newCurrentIndexOfCurrentWord = currentIndexOfCurrentWord + 1;
            int newCurrentIndexOfInput = currentIndexOfInput + 1;

            return new Option(newCurrentIndexOfInput, availableWords,
                    newCurrentIndexOfCurrentWord, currentWord);
        }

        private Option getNewOptionDestroyCurrentWord(List<String> newAvailableWords) {

            Optional<String> newCurrentWord = Optional.empty();
            int newCurrentIndexOfInput = currentIndexOfInput + 1;
            int newCurrentIndexOfCurrentWord = 0;

            return new Option(newCurrentIndexOfInput, newAvailableWords,
                    newCurrentIndexOfCurrentWord, newCurrentWord);
        }

        private Option getNewOptionReplaceCurrentWord(String newWord) {

            Optional<String> newCurrentWord = Optional.of(newWord);
            int newCurrentIndexOfInput = currentIndexOfInput + 1;
            int newCurrentIndexOfCurrentWord = 1;
            List<String> newAvailableWords = getNewAvailableWords(availableWords, newWord);

            return new Option(newCurrentIndexOfInput, newAvailableWords,
                    newCurrentIndexOfCurrentWord, newCurrentWord);
        }

        //creates a new copy of the list without the specified word
        private List<String> getNewAvailableWords(List<String> availableWords, String word) {
            List<String> newAvailableWords =
                    availableWords.stream()
                            .filter(s -> !s.equals(word))
                            .collect(Collectors.toList());

            return Collections.unmodifiableList(newAvailableWords);
        }

        private boolean isLast() {
            return currentIndexOfInput == input.length() - 1;
        }
    }
}

