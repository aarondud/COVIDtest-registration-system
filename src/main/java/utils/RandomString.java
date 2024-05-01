package utils;

import java.util.Random;

/**
 * Utility class for random string generation.
 */
public class RandomString {

    /**
     * Class creates a random string of set length
     *
     * @param length size of string
     * @return random string of size length
     */
    public static String main(int length) {
        String characters = "1234567890";

        StringBuilder sb = new StringBuilder();
        Random ran = new Random();

        for (int i = 0; i < length; i++) {

            int index = ran.nextInt(characters.length());
            Character ranChar = characters.charAt(index);
            sb.append(ranChar);
        }
        return sb.toString();
    }
}
