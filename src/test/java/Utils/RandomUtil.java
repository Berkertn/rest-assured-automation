package Utils;

import java.util.Random;

public class RandomUtil {
    public static int getRandomNumber(int maxNumber) {
        Random random = new Random();
        // Generate a random integer between 0 and maxNumber
        int randomNumber = random.nextInt(maxNumber);

        return randomNumber;
    }
}
