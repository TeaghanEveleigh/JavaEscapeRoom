package nz.ac.auckland.se206;

import java.util.Arrays;
import java.util.Random;

/**
 * This class is used to generate a random passcode for the safe. Each time the game is run, a new
 * passcode is generated. Uses Singleton pattern.
 */
public class Passcode {

  private static Passcode instance = new Passcode();
  private final int firstNum;
  private final int secondNum;
  private final int thirdNum;
  private final int keyCode;

  /**
   * This method is used to get the instance of the Passcode singleton.
   *
   * @return The instance of the Passcode singleton.
   */
  public static Passcode getInstance() {
    return instance;
  }

  /** This constructor generates a new passcode and ensures the Singleton pattern is used. */
  private Passcode() {
    Random rand = new Random();

    // Generate random centuries from 1 to 20
    int[] nums = new int[3];
    nums[0] = rand.nextInt(20) + 1;
    nums[1] = rand.nextInt(20) + 1;
    nums[2] = rand.nextInt(20) + 1;
    keyCode = rand.nextInt(90000) + 10000;

    // Sort the random centuries in ascending order
    Arrays.sort(nums);

    firstNum = nums[0];
    secondNum = nums[1];
    thirdNum = nums[2];

    System.out.println("Passcode: " + getFullNum());
  }

  /**
   * This method is used to get the first number of the passcode.
   *
   * @return The first number of the passcode.
   */
  public String getFirstNum() {
    return convertToOrdinal(firstNum);
  }

  /**
   * This method is used to get the key code of the passcode.
   *
   * @return The key code of the passcode.
   */
  public String getKeyCode() {
    return String.valueOf(keyCode);
  }

  /**
   * This method is used to get the second number of the passcode.
   *
   * @return The second number of the passcode.
   */
  public String getSecondNum() {
    return convertToOrdinal(secondNum);
  }

  /**
   * This method is used to get the third number of the passcode.
   *
   * @return The third number of the passcode.
   */
  public String getThirdNum() {
    return convertToOrdinal(thirdNum);
  }

  /**
   * This method is used to get the full passcode.
   *
   * @return The full passcode.
   */
  public String getFullNum() {
    return String.format("%02d%02d%02d", firstNum, secondNum, thirdNum);
  }

  /**
   * This method converts a number to its ordinal form.
   *
   * @param number The number to convert.
   * @return The ordinal form of the number.
   */
  private String convertToOrdinal(int number) {
    if (number >= 11 && number <= 13) {
      return number + "th";
    }
    switch (number % 10) {
      case 1: // If the number ends in 1, return st
        return number + "st";
      case 2: // If the number ends in 2, return nd
        return number + "nd";
      case 3: // If the number ends in 3, return rd
        return number + "rd";
      default: // Otherwise, return th
        return number + "th";
    }
  }

  public static void resetPasscode() {
    instance = new Passcode();
  }
}
