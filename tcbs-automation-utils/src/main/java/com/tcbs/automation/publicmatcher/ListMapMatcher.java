package com.tcbs.automation.publicmatcher;

import com.tcbs.automation.tools.CompareUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.tcbs.automation.publicmatcher.OptionalMatchers.isPresent;
import static com.tcbs.automation.publicmatcher.Verifier.verify;

public class ListMapMatcher {

  public static Matcher<List<HashMap<String, Object>>> sameAs(List<HashMap<String, Object>> expectedObjects, Class objMatcherClass, String... keys) {
    return new TypeSafeMatcher<List<HashMap<String, Object>>>() {
      Description mismatchDescriber = new StringDescription();
      private boolean result = true;

      boolean getResult() {
        return this.result;
      }

      void setResult(boolean r) {
        this.result &= r;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected boolean matchesSafely(List<HashMap<String, Object>> actualObjects) {
        String key1 = keys.length > 0 ? keys[0] : null;
        String key2 = keys.length > 1 ? keys[1] : null;
        String key3 = keys.length > 2 ? keys[2] : null;

        expectedObjects.forEach(expectObj -> {
          Optional<HashMap<String, Object>> optActualObj = assignOpt(actualObjects, expectObj, key1, key2, key3);
          setResult(verify(optActualObj, isPresent(), mismatchDescriber));
          optActualObj.ifPresent(subActualObj -> {
            try {
              setResult(verify(
                subActualObj,
                (Matcher<HashMap<String, Object>>) objMatcherClass
                  .getMethod("sameAs", HashMap.class)
                  .invoke(objMatcherClass.newInstance(), expectObj),
                mismatchDescriber)
              );
            } catch (Exception e) {
              setResult(false);
              mismatchDescriber.appendText(e.getMessage() + "\n");
            }
          });
        });
        return getResult();
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("should be the same as expected result");
      }

      @Override
      protected void describeMismatchSafely(List<HashMap<String, Object>> item, Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }


    };
  }

  public static Optional<HashMap<String, Object>> assignOpt(List<HashMap<String, Object>> actualObjects
    , HashMap<String, Object> expectObj, String... keys) {
    for (HashMap<String, Object> actualObj : actualObjects) {
      if (CompareUtils.compareMap(actualObj, expectObj, keys)) {
        return Optional.of(actualObj);
      }
    }
    return Optional.empty();
  }

  public static String missingKeys(HashMap<String, Object> expected, String... keys) {
    StringBuilder sb = new StringBuilder();
    String delimiter = "";
    for (String key : keys) {
      sb.append(delimiter);
      sb.append(String.format(" %s : %s ", key, expected.get(key)));
      delimiter = "|";
    }
    return sb.toString();
  }
}
