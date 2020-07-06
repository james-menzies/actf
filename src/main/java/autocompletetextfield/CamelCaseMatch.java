package autocompletetextfield;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CamelCaseMatch {

    private final String input;
    private final String object;
    private Deque<Option> options;
    private boolean stringParsed = false;
    Pattern nonWordChars;

    // TODO: 6/7/20 cater for hyphens, apostrophes
    private CamelCaseMatch(String input, String object) {
        this.input = input.toLowerCase();
        this.object = object;
        options = new ArrayDeque<>();
        nonWordChars = Pattern.compile("[^\\w]+");
    }

    public static boolean test(String input, String object) {

        if (input.equals("")) return true;

        return new CamelCaseMatch(input, object).matches();
    }

    private boolean matches() {

        Option firstLetter = new Option();
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

        private Option() {

            currentIndexOfInput = 0;
            availableWords = initializeAvailableWords();
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

        private List<String> initializeAvailableWords() {
            return Stream.of(nonWordChars.split(object))
                    .map(String::toLowerCase)
                    .collect(Collectors.toUnmodifiableList());
        }


        public List<Option> explore() {

            List<Option> nextBranch = new ArrayList<>();

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

        public boolean completePath() {
            return matchesCurrentWord() || getNewWordMatches().size() > 0;
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

        // TODO: 3/7/20 fix one letter word bug (again) 
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

        public boolean isLast() {
            return currentIndexOfInput == input.length() - 1;
        }
    }
}

