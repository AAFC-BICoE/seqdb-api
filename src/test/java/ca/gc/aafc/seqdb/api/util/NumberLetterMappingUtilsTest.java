package ca.gc.aafc.seqdb.api.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Unit tests for NumberLetterMappingUtils class.
 */
public class NumberLetterMappingUtilsTest {

  @Test
  public void getLetter_OnValidIndex_ReturnMatchingLetter() {
    assertEquals("A", NumberLetterMappingUtils.getLetter(1)); // This function should return
    assertEquals("B", NumberLetterMappingUtils.getLetter(2)); // The letter representation of
    assertEquals("C", NumberLetterMappingUtils.getLetter(3)); // The number given. Where the
    assertEquals("Z", NumberLetterMappingUtils.getLetter(26)); // Order is sequential
    assertEquals("After Z, AA should follow", "AA", NumberLetterMappingUtils.getLetter(27)); // (A = 1, B = 2, C= 3).
    assertEquals("The sequential pattern should be the same after adding a digit", "AB",
        NumberLetterMappingUtils.getLetter(28));
    assertEquals("Beginning of triple digits at Int 703", "AAA", NumberLetterMappingUtils.getLetter(703));

    // method should not break even when provided
    // an impossible number of wells.
    // Implementation doesn't matter can be null
    assertNotNull(NumberLetterMappingUtils.getLetter(123456969));
  }

  @Test
  public void getLetter_OnInvalidIndex_ReturnNull() {
    assertNull(NumberLetterMappingUtils.getLetter(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getLetter_OnInvalidIndex_ThrowException() {
    NumberLetterMappingUtils.getLetter(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getLetter_OnInvalidIndex2_ThrowException() {
    NumberLetterMappingUtils.getLetter(-1);
  }

  @Test
  public void getNumber_OnValidLetter_ReturnMatchingInteger() {
    assertEquals(1, NumberLetterMappingUtils.getNumber("A").intValue()); // checking that the equalities are correct
    assertEquals(27, NumberLetterMappingUtils.getNumber("AA").intValue()); // between numbers and letters
    assertEquals(26, NumberLetterMappingUtils.getNumber("Z").intValue());
    assertEquals("getRowKey should not be case sensitive", 2, NumberLetterMappingUtils.getNumber("b").intValue());
  }

  @Test
  public void getNumber_OnInvalidLetter_ReturnNull() {
    assertNull(NumberLetterMappingUtils.getNumber(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumber_OnInvalidLetter_ThrowException() {
    NumberLetterMappingUtils.getNumber("AAAAA"); // invalid since only up to 2 letters can be processed
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumber_OnInvalidLetter2_ThrowException() {
    NumberLetterMappingUtils.getNumber("-A"); // Alphabetical inputs only
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumber_OnInvalidLetter3_ThrowException() {
    NumberLetterMappingUtils.getNumber("é"); // Alphabetical inputs only
  }

}
