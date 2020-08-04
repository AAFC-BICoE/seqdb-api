package ca.gc.aafc.seqdb.api.util;

import java.util.regex.Pattern;

public class NumberLetterMappingUtils {
    /**
   * regex for any character not in alphabet and capitalized.
   */
  private static final Pattern NON_ALPHABET_PATTERN = Pattern.compile("[^A-Z]");

  /**
   * Generate the alphabetical value for a number, where the letters roll over after reaching Z.
   * 
   * Examples: 1 -&gt; A, 26 -&gt; Z, 27 -&gt; AA, 52 -&gt; AZ
   *
   * @param number the number you want alphabetized, only non-null, non-zero,
   *            positive whole numbers.
   * @return the letter
   * @throws IllegalArgumentException the illegal argument exception
   */
  public static String getLetter(Integer number) {
  
    if (number == null) {
      return null;
    }
  
    if (number <= 0) {
      throw new IllegalArgumentException(
          "Does not accept Integers less than zero. Your input : " + number.toString());
    }
    // The following equation calculates the number of letters the character array
    // should have.
    // The formula is for converting to log base 26, as the range each additional
    // character adds
    // is exponential. I.e Integer Range 1-26 returns buf[1], Range 27-702 returns
    // buf[2] and so on.
    char[] buf = new char[(int) Math.floor(Math.log(25 * (number + 1)) / Math.log(26))];
  
    // for each element in buf, populate it with the correct character
    for (int i = buf.length - 1; i >= 0; i--) {
      number--;
      buf[i] = (char) ('A' + number % 26); // using ASCII with A as the starting point.
      number /= 26;
    }
    return new String(buf);
  }

  /**
   * Gets the number from an alphabetized number, where the letters roll over after reaching Z.
   *
   * Examples: A -&gt; 1, Z -&gt; 26, AA -&gt; 27, AZ -&gt; 52
   *
   * @param letter the alphabetized number, A-Z alphabetical only
   * @return the number as an Integer
   */
  public static Integer getNumber(String letter) {
  
    // If nothing is given, nothing is given back.
    if (letter == null) {
      return null;
    }
  
    letter = letter.toUpperCase();
  
    if (NumberLetterMappingUtils.NON_ALPHABET_PATTERN.matcher(letter).find()) {
      throw new IllegalArgumentException("Alphabetical[A-Z] Inputs only. Your input : " + letter);
    }
  
    int length = letter.length();
  
    // Same math a getRowLetter but in reverse to get the number.
    if (length == 1) {
      return (int) letter.charAt(0) - 64; // charAt returns the ASCII number and the alphabets start
                                          // at 65
    } else if (length == 2) {
      return (int) (letter.charAt(0) - 64) * 26 + (letter.charAt(1) - 64);
    } else {
      throw new IllegalArgumentException(
          "Could not resolve the String to a number. Your input : " + letter);
    }
  }

}