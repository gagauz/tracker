package com.gagauz.tracker.utils;

import java.util.List;
import java.util.Random;

public class RandomUtils {
    private static Random random = new Random(System.currentTimeMillis());

    private RandomUtils() {
    }

    public static <T> List<T> getRandomSubList(List<T> input, int subsetSize) {
        Random r = new Random();
        int inputSize = input.size();
        subsetSize = inputSize > subsetSize ? subsetSize : inputSize;
        for (int i = 0; i < subsetSize; i++) {
            int indexToSwap = i + r.nextInt(inputSize - i);
            T temp = input.get(i);
            input.set(i, input.get(indexToSwap));
            input.set(indexToSwap, temp);
        }
        return inputSize > subsetSize ? input.subList(0, subsetSize) : input;
    }

    public static int getRandomInt(int n) {
        return random.nextInt(n);
    }
}
