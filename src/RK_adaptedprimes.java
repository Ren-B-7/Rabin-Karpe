import java.lang.StrictMath;
import java.math.*;
import java.util.Random;

// Using linkedhashmap so the order is kept from insertion while having benefits of normal hashmap
import java.util.LinkedHashMap;

public class RK_adaptedprimes {
    // Ordered according to its frequency in the english language
    private final static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    // Using LinkedHashMap for benefit of O(1) searching and ordered insertion
    private static LinkedHashMap<Integer, Double> value_map;

    private static double hashpattern; // pattern hash value
    private static int M; // pattern length
    private static double differ_rm;
    private static double epsilon;

    /**
     * The main function that initializes the prime_map, sets the pattern and M,
     * updates the map with the pattern, calculates the pattern hash, and prints
     * the result of pattern recognition on the second argument.
     *
     * @param args the command line arguments, where args[0] is the pattern and
     *             args[1] is the text to search for the pattern
     */
    public static void main(String[] args) {
        value_map = new LinkedHashMap<>();

        M = args[0].length();
        for (char c : alphabet.toCharArray()) {
            value_map.put((int) c, StrictMath.pow(c, 1.0 / (M * 1.0)));
        }
        epsilon = 0.000000000001 * StrictMath.pow(10, M);
        hashpattern = hashcalc(args[0]);

        System.out.println(patternrecognition(args[1].toLowerCase()));
    }

    /**
     * Calculates the hash value for the given pattern using the adapted primes
     * method.
     * Combined with the update map method, this method is used to update the
     * prime_map and calculate the hash value
     *
     * @param pattern the input pattern for which the hash value is calculated
     * @return the calculated hash value as a BigInteger
     */
    private static double hashcalc(String pattern) {
        // Set the local and global differ_rm values to 1
        Double value = differ_rm = 1.0;
        for (int counter = 0; counter < M; counter++) {
            // Get the int corrosponding to the char
            int c = pattern.charAt(counter);
            // If it is not found in the map we add its ascii with a unique number
            if (!value_map.containsKey(c))
                value_map.put(c, StrictMath.pow(c, 1.0 / (M * 1.0)));

            // Get the corrosponding value
            double p = value_map.get(c);
            // Calculate the unique hash value
            value = value * (power(p, counter + 1));
            // Set differ_rm to the product of the previous differ_rm and p
            differ_rm = differ_rm * (p);
        }
        // Update the value_map with the remaining characters, if there are any
        // unchecked characters
        for (char c : pattern.substring(Math.min(M, pattern.length())).toCharArray()) {
            value_map.put((int) c, StrictMath.pow(c, 1.0 / (M * 1.0)));
        }
        return value;
    }

    /**
     * Recognizes a pattern in a given text using the adapted primes method.
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
        for (int i = M; i < text.length(); i++) {
            // Remove 1 of each prime number, since at start we have (Prime ^ Index) of each
            // index [0-M-1]
            txtHash *= chars[i] / differ_rm;

            // Check for match, done before other changes are made
            // since changes wont be necesary then
            if ((txtHash > hashpattern)
                    ? (txtHash - hashpattern < epsilon)
                    : (hashpattern - txtHash < epsilon)) {
                return true;
            }
            // Adds leading digit to the differ_rm long item
            differ_rm *= value_map.get((int) chars[i]) / value_map.get((int) chars[i - M]);
        }
        return false; // no match found
    }

    /**
     * Calculates the power of a base number raised to an exponent.
     *
     * @param base     the base number
     * @param exponent the exponent to raise the base to
     * @return the result of the base raised to the exponent
     */
    public static double power(double base, int exponent) {
        double b = 1.0;
        for (int i = 0; i < exponent; i++) {
            b *= base;
        }
        return b;
    }
    // Per loop iteration
    // Personal algorithm
    // 0 declarations
    // 3 assigns
    // 15 accesses
    // 7 math calculations
    // 2 String operation
    // 2 compare
    // 2 map accesses

    // Deprecated functions

    /**
     * @deprecated
     *             Generates a random prime number that is not already in the
     *             prime_map.
     *
     * @return a long value representing a random prime number
     */
    private static double randomprimeDeprecated() {
        int prime = 2;
        while (value_map.containsValue(prime * 1.0)) {
            // Using bitlength of 10 since there are 200 prime numbers within bitlength of
            // 10
            prime = BigInteger.probablePrime(10, new Random()).intValue();
            if (prime < 15)
                continue;
        }
        return StrictMath.pow(prime, 1.0 / M);
    }

    /**
     * @Deprecated
     *             Performs pattern recognition on the given text using the adapted
     *             primes method.
     *
     * @param text the input text to search for the pattern
     * @return true if the pattern is found at the beginning of the text, false
     *         otherwise
     */
    private static boolean patternrecognitionDeprecated(String text) {
        // Ensures that all characters in the substring and the string are found, else
        // we will encounter an error
        if (text.length() < M)
            return false;

        // Long hpattern = hashpattern.longValue();
        Long hpattern = 1L;
        Long txtHash = hashcalcDeprecated(text, 0);
        // Search for hash match in text.
        if (hpattern.equals(txtHash))
            return true; // Match at beginning.

        Long adapt;
        // Long differ_rmLONG = differ_rm.longValue();
        Long differ_rmLONG = 1L;
        for (int i = M; i < text.length(); i++) {
            // Remove 1 of each prime number, since at start we have (Prime ^ Index) of each
            // index [0-M-1]
            adapt = value_map.get(text.charAt(i)).longValue();
            // txtHash = txtHash.divide(differ_rm).multiply(adapt.pow(M));
            txtHash = (long) (Long.divideUnsigned(txtHash, differ_rmLONG) * power(txtHash.doubleValue(), M));

            // Check for match, done before other changes are made since
            if (hpattern.equals(txtHash))
                return true;
            // Adds leading digit to the differ_rm long item
            differ_rmLONG = Long.divideUnsigned(differ_rmLONG, value_map.get(text.charAt(i - M)).longValue()) * adapt;
        }
        return false; // no match found
    }

    /**
     * @deprecated
     *             Updates the prime_map by adding a random prime number for each
     *             character in the given text if the character is not already
     *             present in the map.
     *
     * @param text the input text to update the prime_map
     */
    private static void updatemap(String text) {
        for (Character c : text.toCharArray()) {
            if (!value_map.containsKey(c)) {
                value_map.put((int) c, randomprime());
            }
        }
    }

    /**
     * @deprecated
     *             Calculates the hash value for the given pattern using the adapted
     *             primes method.
     *
     * @param pattern the input pattern for which the hash value is calculated
     * @return the calculated hash value as a Long
     */
    private static Long hashcalcDeprecated(String pattern, int m) {
        Long differ_rmLONG;
        Long prime = differ_rmLONG = 1L;
        for (int counter = 0; counter < M; counter++) {
            Long p = value_map.get(pattern.charAt(counter)).longValue();
            // if (!prime_map.containsKey(c)) prime_map.put(p, randomprime()); -- LONG
            // instead of BigInteger
            prime = prime * ((long) power(p.doubleValue(), counter + 1));
            differ_rmLONG = differ_rmLONG * p;
        }

        for (char c : pattern.substring(Math.min(M, pattern.length())).toCharArray()) {
            // If it is not found in the map we add it with a unique prime number
            if (!value_map.containsKey(c))
                value_map.put((int) c, randomprime());
        }
        return prime;
    }

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

            // 0 declaration
            // 2 assigns
            // 17 accesses
            // 11 Math calculations
            // 2 String operations
            // 1 compare
        }
        return N;
    }
}
