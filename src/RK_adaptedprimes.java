import java.lang.StrictMath;

// Using linkedhashmap so the order is kept from insertion while having benefits of normal hashmap
import java.util.HashMap;

public class RK_adaptedprimes {
    // Using HashMap for benefit of O(1) searching
    private static HashMap<Character, Double> value_map;

    private static double hashpattern; // pattern hash value
    private static int M; // pattern length
    private static double differ_rm;
    private static double epsilon;

    /**
     * @param args the command line arguments, where
     *             args[0] is the pattern and
     *             args[1] is the text to search for the pattern
     */
    public static void main(String[] args) {
        value_map = new HashMap<>();

        M = args[0].length();

        epsilon = 0.000000000001 * StrictMath.pow(10, M);
        hashpattern = hashcalc(args[0]);

        System.out.println(patternrecognition(args[1].toLowerCase()));
    }

    /**
     * Calculates the hash value for the given pattern using the values_map and
     * their
     * corresponding chars. It updates the value_map if the values dont exist and
     * returns the double of the hash value
     *
     * @param pattern the input pattern for which the hash value is calculated
     * @return the calculated hash value as a double
     */
    private static double hashcalc(String pattern) {
        // Set the local and global values to 1
        double value = differ_rm = 1.0;
        char c;
        double p;
        for (int counter = 0; counter < M; counter++) {
            // Get the int corrosponding to the char
            c = pattern.charAt(counter);
            // If it is not found in the map we add its ascii with a unique number
            if (!value_map.containsKey(c))
                value_map.put(c, StrictMath.pow(c, 1.0 / (M * 1.0)));

            // Get the corrosponding value
            p = value_map.get(c);
            // Calculate the unique hash value
            value = value * (StrictMath.pow(p, counter + 1));
            // Set differ_rm to the product of the previous differ_rm and p
            differ_rm = differ_rm * (p);
        }
        // Update the value_map with the remaining characters, if there are any
        // unchecked characters

        // We can replace this code by putting the if statement into the math loop,
        // lower the amount of preprocessing, but increasing the operations in the main
        // for loop
        for (char ch : pattern.substring(Math.min(M, pattern.length())).toCharArray()) {
            if (!value_map.containsKey(ch)) {
                value_map.put(ch, StrictMath.pow(ch, 1.0 / (M * 1.0)));
            }
        }
        return value;
    }

    /**
     * Recognizes a pattern in a given text using the adapted math of rabin-karpe.
     *
     * @param text the input text to search for the pattern
     * @return true if the pattern is found at the beginning of the text, false
     *         otherwise
     */
    private static boolean patternrecognition(String text) {
        // Ensures that all characters in the substring and the string are found, else
        // we will encounter an error
        if (text.length() < M)
            return false;

        double txtHash = hashcalc(text);
        // Search for hash match in text.
        if (hashpattern == txtHash)
            return true; // Match at beginning.

        // double adapt;
        char[] chars = text.toCharArray();
        // If we decide to lower preprocessing we can add commented code
        // char current;
        for (int i = M; i < chars.length; i++) {
            /*
             * current = chars[i];
             * if (!value_map.containsKey(current)) {
             * value_map.put(current, StrictMath.pow(current, 1.0 / (M * 1.0)));
             * }
             */

            // Remove 1 of each prime number, since at start we have (Prime ^ Index) of each
            // index [0-M-1]
            txtHash *= chars[i] / differ_rm;

            // If we decide to lower the preprocessing we can use commented code instead of
            // whats above this
            // txtHash *= current / differ_rm;

            // Check for match, done before other changes are made
            // since changes wont be necesary then
            if ((txtHash > hashpattern)
                    ? (txtHash - hashpattern < epsilon)
                    : (hashpattern - txtHash < epsilon)) {
                return true;
            }
            // Adds leading digit to the differ_rm long item
            differ_rm *= value_map.get(chars[i]) / value_map.get(chars[i - M]);
            // If we decide to lower the preprocessing we can use commented code instead of
            // whats above this
            // differ_rm *= value_map.get(current) / value_map.get((int) chars[i - M]);

        }
        return false; // no match found
    }

    // Per loop iteration
    // Personal algorithm
    // Excluding the compare statement
    // 2 assigns
    // 10 accesses (total, not including assigns)
    // 5 math operations
    // 3 array accesses
    // 2 map accesses

    // Rabin-Karpe algorithm from algs4
    private static int algs4rabinkarpe() {
        // Rabin-Karpe algorithm.
        Long txtHash = 2L;
        Long patHash = 2L;
        String txt = "";
        int R = 0;
        int Q = 0;
        int N = 0;
        int RM = 0;
        for (int i = M; i < N; i++) {
            // Remove leading digit, add trailing digit, check for match.
            txtHash = (txtHash + Q - RM * txt.charAt(i - M) % Q) % Q;
            txtHash = (txtHash * R + txt.charAt(i)) % Q;

            if ((patHash == txtHash))
                return i - M + 1;

            // Not including the compare if statement
            // 2 assigns
            // 13 accesses (total not including return or assigns)
            // 9 Math calculations (not including return)
            // 2 String operations
        }
        return N;
    }
}
