package com.tcbs.automation.publicmatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.tcbs.automation.publicmatcher.Verifier.verify;

public class NumberMatcher {
  public static Matcher<HashMap<String, Object>> hasDoubleValue(String property, Matcher<Double> matcher) {
    return new TypeSafeMatcher<HashMap<String, Object>>() {
      Description mismatchDescriber = new StringDescription();

      @Override
      protected boolean matchesSafely(HashMap<String, Object> actual) {
        Double value = featureValueOf(actual) == null ? null : Double.parseDouble(featureValueOf(actual).toString());
        return verify(value, matcher, mismatchDescriber);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText(String.format("\"%s\" expected ", property)).appendDescriptionOf(matcher);
      }

      @SuppressWarnings("unchecked")
      Object featureValueOf(HashMap<String, Object> actual) {
        String[] subProperty = property.split("\\.");
        Object data = new HashMap<>(actual);
        for (String s : subProperty) {
          if (data instanceof HashMap) {
            data = ((HashMap<String, Object>) data).get(s);
          } else if (data instanceof ArrayList) {
            if (s.matches("^\\{\\w*=\\w*\\}$")) {
              String[] strArr = s.substring(1, s.length() - 1).split("=");
              String subKey = strArr[0];
              String subValue = strArr[1];
              data = ((ArrayList<HashMap<String, Object>>) Objects.requireNonNull(data)).stream().filter(obj ->
                obj.get(subKey).toString().equals(subValue)).findFirst().orElse(null);
            } else {
              data = ((ArrayList<Object>) data).get(Integer.parseInt(s));
            }
          }
        }
        return data;
      }


      @Override
      protected void describeMismatchSafely(HashMap<String, Object> theMismatchItem, Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }
    };
  }
}
