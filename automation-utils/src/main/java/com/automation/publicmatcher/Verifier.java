package com.automation.publicmatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class Verifier {
  public static <T> boolean verify(T a, Matcher<? super T> matcher, Description mismatchDesc) {
    boolean result = verify(a, matcher);
    if (!result) {
      matcher.describeMismatch(a, mismatchDesc);
    }
    return result;
  }

  public static <T> boolean verify(T a, Matcher<? super T> matcher) {
    boolean result = true;
    if (!matcher.matches(a)) {
      result = false;
    }
    return result;
  }
}
