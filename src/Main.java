import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * THESE ARE THE MAPPING RULES:
 *
 *
 * E | JNQ | RWX | DSY | FT | AM | CIV | BKU | LOP | GHZ
 * e | jnq | rwx | dsy | ft | am | civ | bku | lop | ghz
 * 0 | 1   | 2   | 3   | 4  | 5  | 6   | 7   | 8   | 9
 *
 *
 */

/**
 * @author Henric Rosengren Evenlind
 */
public class Main {

    /*
     * Words to match against when no arguments are given.
     * */
    private static final String[] words = {
            "an",
            "blau",
            "Bo",
            "Boot",
            "bos",
            "da",
            "Fee",
            "fern",
            "Fest",
            "fort",
            "je",
            "jemand",
            "mir",
            "Mix",
            "Mixer",
            "Name",
            "neu",
            "od",
            "Ort",
            "so",
            "Tor",
            "Torf",
            "Wasser"
    };

    /*
     * Phonenumbers to use when no arguments are given.
     * */
    private static final String[] numbers = {
            "112",
            "562482",
            "4824",
            "07216084067",
            "107835",
            "10789135",
            "381482",
            "04824"
    };

    private static Map<String, List<String>> word_list;

    private static void addNumbersAndWordsToMap(@NotNull final Map<String, List<String>> numbers_and_words,
                                                final String number, final String word) {
        if (numbers_and_words.containsKey(number)) {
            numbers_and_words.get(number).add(word);
        } else {
            List<String> list = new LinkedList<>();
            list.add(word);
            numbers_and_words.put(number, list);
        }
    }

    public static void main(String[] args) throws IOException {
        final Map<String, List<String>> numbers_and_words = new HashMap<>();

        if (args.length == 0) {
            for (String word : words) {
                addNumbersAndWordsToMap(numbers_and_words, wordToNumber(word), word);
            }

            word_list =
                    Collections.unmodifiableMap(numbers_and_words);

            for (String number : numbers) {
                getMatchesForNumber(trimNumber(number),
                        trimNumber(number), "", false);
            }

        } else {
            String dict = args[0];
            String num = args[1];

            BufferedReader file = new BufferedReader(new FileReader(dict));
            String word;

            while ((word = file.readLine()) != null) {
                addNumbersAndWordsToMap(numbers_and_words, wordToNumber(word), word);
            }

            file.close();

            word_list = Collections.unmodifiableMap(numbers_and_words);

            file = new BufferedReader(new FileReader(num));
            String number;

            while ((number = file.readLine()) != null) {
                getMatchesForNumber(trimNumber(number),
                        trimNumber(number), "", false);
            }

            file.close();

        }
    }

    /*
     *
     * Map a character to a digit according to the mapping rules.
     * */
    @Contract(pure = true)
    private static int characterToDigit(char character) {
        switch (character) {
            case 'e':
            case 'E':
                return 0;
            case 'j':
            case 'J':
            case 'n':
            case 'N':
            case 'q':
            case 'Q':
                return 1;
            case 'r':
            case 'R':
            case 'w':
            case 'W':
            case 'x':
            case 'X':
                return 2;
            case 'd':
            case 'D':
            case 's':
            case 'S':
            case 'y':
            case 'Y':
                return 3;
            case 'f':
            case 'F':
            case 't':
            case 'T':
                return 4;
            case 'a':
            case 'A':
            case 'm':
            case 'M':
                return 5;
            case 'c':
            case 'C':
            case 'i':
            case 'I':
            case 'v':
            case 'V':
                return 6;
            case 'b':
            case 'B':
            case 'k':
            case 'K':
            case 'u':
            case 'U':
                return 7;
            case 'l':
            case 'L':
            case 'o':
            case 'O':
            case 'p':
            case 'P':
                return 8;
            case 'g':
            case 'G':
            case 'h':
            case 'H':
            case 'z':
            case 'Z':
                return 9;
            default:
                return -1;
        }
    }

    @NotNull
    private static String wordToNumber(String word) {
        StringBuilder number = new StringBuilder();

        for (char character : word.toCharArray()) {
            number.append(characterToDigit(character));
        }
        return number.toString();
    }

    @NotNull
    private static String trimNumber(String number) {
        return number.replaceAll(" ", "").replaceAll("-", "");
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private static void getMatchesForNumber(@NotNull final String number,
                                            final String original_number, final String match,
                                            boolean trailing_digit) {

        if (number.isEmpty()) {
            if (!match.isEmpty()) {
                System.out.println(original_number + ": " + match);
            }
        } else {
            boolean found_word = false;

            for (int i = 1; i <= number.length(); i++) {
                List<String> matches = word_list.get(number.substring(0, i));

                if (matches != null) {
                    found_word = true;
                    for (Iterator iter = matches.iterator(); iter.hasNext(); ) {
                        getMatchesForNumber(number.substring(i),
                                original_number, match + iter.next() + " ", false);
                    }
                }
            }
            if (!found_word && !trailing_digit) {
                getMatchesForNumber(number.substring(1),
                        original_number, match + number.substring(0, 1) + " ", true);
            }
        }
    }
}