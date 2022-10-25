package com.automation.tools;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.Precision;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomFactory {

  /**
   * Generate random double value in range
   *
   * @param min:          Min value in range
   * @param max:          Max value in range
   * @param decimalPlace: Number of digits after decimal place
   * @return
   */
  public static Double randomDouble(Double min, Double max, int decimalPlace) {
    Double random = new RandomDataGenerator().nextUniform(min, max);
    return Precision.round(random, decimalPlace);
  }

  public static int randomInt(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }

  public static long randomLong(long min, long max) {
    return ThreadLocalRandom.current().nextLong(min, max);
  }

  public static String randomFromListString(String input) {
    List<String> inputList = Arrays.asList(input.split(","));
    Integer randomElement = randomInt(0, inputList.size() - 1);
    return inputList.get(randomElement);
  }

}
