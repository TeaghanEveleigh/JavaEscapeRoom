package nz.ac.auckland.se206;

import java.util.Arrays;
import java.util.Random;

public class Passcode {
    private static final Passcode instance = new Passcode();

    private final int firstNum;
    private final int secondNum;
    private final int thirdNum;
    private final int keyCode;

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
    }

    public static Passcode getInstance() {
        return instance;
    }

    public String getFirstNum() {
        return convertToOrdinal(firstNum);
    }
    public String getKeyCode(){
        return String.valueOf(keyCode);
    }

    public String getSecondNum() {
        return convertToOrdinal(secondNum);
    }

    public String getThirdNum() {
        return convertToOrdinal(thirdNum);
    }

    public String getFullNum() {
        return String.format("%02d%02d%02d", firstNum, secondNum, thirdNum);
    }

    private String convertToOrdinal(int number) {
        if (number >= 11 && number <= 13) {
            return number + "th";
        }
        switch (number % 10) {
            case 1:
                return number + "st";
            case 2:
                return number + "nd";
            case 3:
                return number + "rd";
            default:
                return number + "th";
        }
    }
}

