package com.automation.publicmatcher;

import com.automation.abilities.SoftAsserts;
import com.automation.tools.ConvertUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.*;

import static com.automation.publicmatcher.Verifier.verify;

public class HashMapMatcher {
  public static Matcher<HashMap<String, Object>> mapEquals(HashMap<String, Object> expected) {
    Description mismatchDescriber = new StringDescription();
    return new TypeSafeMatcher<HashMap<String, Object>>() {
      @Override
      protected boolean matchesSafely(HashMap<String, Object> actual) {
        ConvertUtils convertUtils = new ConvertUtils();
        HashMap<String, String> actualStrMap = convertUtils.hashMapObjTohashMapStr(actual);
        HashMap<String, String> expectedStrMap = convertUtils.hashMapObjTohashMapStr(expected);
        Set<String> actualKeys = new HashSet<>(actualStrMap.keySet());
        Set<String> expectedKeys = new HashSet<>(expectedStrMap.keySet());
        Set<String> commonKeys = new HashSet<>();

        actualKeys.forEach(key -> {
          if (expectedKeys.contains(key)) {
            commonKeys.add(key);
          }
        });

        mismatchDescriber.appendText("\n");
        SoftAsserts sa = new SoftAsserts();

        actualKeys.removeAll(commonKeys);
        sa.assertThat(actualKeys.isEmpty())
          .withFailMessage("Actual map contains extra field(s): " + actualKeys)
          .isTrue();

        expectedKeys.removeAll(commonKeys);
        sa.assertThat(expectedKeys.isEmpty())
          .withFailMessage("Expected map contains extra field(s): " + expectedKeys)
          .isTrue();

        commonKeys.forEach(key -> sa.assertThat(actualStrMap.get(key))
          .withFailMessage("\tFound different values:\n\t\tactual = {%s: %s}, expected = {%s: %s}", key, actualStrMap.get(key), key, expectedStrMap.get(key))
          .isEqualTo(expectedStrMap.get(key)));

        return sa.assertAll(mismatchDescriber);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("HashMaps are equal");
      }

      @Override
      public void describeMismatchSafely(HashMap<String, Object> actual, Description description) {
        description.appendText(mismatchDescriber.toString());
      }
    };
  }

  public static Matcher<HashMap<String, Object>> has(String property, Matcher<? super Object> matcher) {
    return new TypeSafeMatcher<HashMap<String, Object>>() {
      Description mismatchDescriber = new StringDescription();

      @Override
      protected boolean matchesSafely(HashMap<String, Object> actual) {
        Object value = featureValueOf(actual);
        return Verifier.verify(value, matcher, mismatchDescriber);
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
