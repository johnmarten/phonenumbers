import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * E | JNQ | RWX | DSY | FT | AM | CIV | BKU | LOP | GHZ
 * e | jnq | rwx | dsy | ft | am | civ | bku | lop | ghz
 * 0 | 1   | 2   | 3   | 4  | 5  | 6   | 7   | 8   | 9
 */

/**
 * @author Henric Rosengren Evenlind
 */
public class Main {

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

    private static void addNumbersAndWords(@NotNull final Map<String, List<String>> numbers_and_words, final String number, final String word) {
        if (numbers_and_words.containsKey(number)) {
            numbers_and_words.get(number).add(word);
        } else {
            List<String> list = new LinkedList<>();
            list.add(word);
            numbers_and_words.put(number, list);
        }
    }

    public static void main(@NotNull String[] args) throws IOException {
        final Map<String, List<String>> numbers_and_words = new HashMap<>();

        if (args.length == 0) {
            for (String word : words) {
                addNumbersAndWords(numbers_and_words, word_to_number(word), word);
            }

            word_list =
                    Collections.unmodifiableMap(numbers_and_words);

            for (String number : numbers) {
                getMatchesForNumber(trim_number(number),
                        trim_number(number), "", false);
            }

        } else {
            String dict = args[0];
            String num = args[1];

            if (dict != null && num != null) {
                BufferedReader file = new BufferedReader(new FileReader(dict));
                String word;

                while ((word = file.readLine()) != null) {
                    addNumbersAndWords(numbers_and_words, word_to_number(word), word);
                }

                file.close();

                word_list =
                        Collections.unmodifiableMap(numbers_and_words);

                file = new BufferedReader(new FileReader(num));
                String number;

                while ((number = file.readLine()) != null) {
                    getMatchesForNumber(trim_number(number),
                            trim_number(number), "", false);
                }

                file.close();
            }

        }
    }

    @Contract(pure = true)
    private static int character_to_digit(char character) {
        switch (character) {
            case 'e':
                return 0;
            case 'E':
                return 0;
            case 'j':
                return 1;
            case 'J':
                return 1;
            case 'n':
                return 1;
            case 'N':
                return 1;
            case 'q':
                return 1;
            case 'Q':
                return 1;
            case 'r':
                return 2;
            case 'R':
                return 2;
            case 'w':
                return 2;
            case 'W':
                return 2;
            case 'x':
                return 2;
            case 'X':
                return 2;
            case 'd':
                return 3;
            case 'D':
                return 3;
            case 's':
                return 3;
            case 'S':
                return 3;
            case 'y':
                return 3;
            case 'Y':
                return 3;
            case 'f':
                return 4;
            case 'F':
                return 4;
            case 't':
                return 4;
            case 'T':
                return 4;
            case 'a':
                return 5;
            case 'A':
                return 5;
            case 'm':
                return 5;
            case 'M':
                return 5;
            case 'c':
                return 6;
            case 'C':
                return 6;
            case 'i':
                return 6;
            case 'I':
                return 6;
            case 'v':
                return 6;
            case 'V':
                return 6;
            case 'b':
                return 7;
            case 'B':
                return 7;
            case 'k':
                return 7;
            case 'K':
                return 7;
            case 'u':
                return 7;
            case 'U':
                return 7;
            case 'l':
                return 8;
            case 'L':
                return 8;
            case 'o':
                return 8;
            case 'O':
                return 8;
            case 'p':
                return 8;
            case 'P':
                return 8;
            case 'g':
                return 9;
            case 'G':
                return 9;
            case 'h':
                return 9;
            case 'H':
                return 9;
            case 'z':
                return 9;
            case 'Z':
                return 9;
            default:
                return -1;
        }
    }

    @NotNull
    private static String word_to_number(String word) {
        StringBuilder number = new StringBuilder();

        for (char character : word.toCharArray()) {
            number.append(character_to_digit(character));
        }
        return number.toString();
    }

    @NotNull
    private static String trim_number(String number) {
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