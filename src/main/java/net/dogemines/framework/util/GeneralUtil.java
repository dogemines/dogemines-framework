package net.dogemines.framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class GeneralUtil {
    public static <T> List<T[]> splitArray(T[] array, int rowSize) {
        List<T[]> result = new ArrayList<>();
        int length = array.length;

        for (int i = 0; i < length; i += rowSize) {
            int end = Math.min(length, i + rowSize);
            result.add(Arrays.copyOfRange(array, i, end));
        }

        return result;
    }

    public static <T> T getRandomElement(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(array.length); // 0 to fruits.length-1

        return array[randomIndex];
    }
}
