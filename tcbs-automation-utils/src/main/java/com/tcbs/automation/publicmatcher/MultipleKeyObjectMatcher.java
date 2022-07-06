package com.tcbs.automation.publicmatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.tcbs.automation.publicmatcher.OptionalMatchers.isPresent;
import static com.tcbs.automation.publicmatcher.Verifier.verify;

public class MultipleKeyObjectMatcher {

  public static Matcher<List<HashMap<String, Object>>> sameAs(List<?> expectedObjects, Class objMatcherClass,
                                                              String... keys) {
    return new TypeSafeMatcher<List<HashMap<String, Object>>>() {
      Description mismatchDescriber = new StringDescription();
      private boolean result = true;

      boolean getResult() {
        return this.result;
      }

      void setResult(boolean r) {
        this.result &= r;
      }


      @Override
      protected boolean matchesSafely(List<HashMap<String, Object>> actualObjects) {
        if (expectedObjects.size() != actualObjects.size()) {
          setResult(false);
          mismatchDescriber.appendText(String.format("%s list size: expected [%s] but was [%s].\n",
            getClassName(expectedObjects),
            expectedObjects.size(),
            actualObjects.size()
          ));
        } else {
          expectedObjects.forEach(expectObj -> compareListMapWithObject(actualObjects, expectObj));
        }
        return getResult();
      }

      @SuppressWarnings("unchecked")
      void compareListMapWithObject(List<HashMap<String, Object>> actualObjects, Object expectObj) {
        Optional<HashMap<String, Object>> optActualObj = Optional.empty();
        try {
          optActualObj = assignOpt(actualObjects, expectObj, keys);
        } catch (Exception e) {
          setResult(false);
          mismatchDescriber.appendText(e.getMessage() + "\n");
        }
        if (!optActualObj.isPresent()) {
          mismatchDescriber.appendText("idInDB: " + expectObj + " Was missing in actual result\n");
        }
        setResult(verify(optActualObj, isPresent(), mismatchDescriber));
        optActualObj.ifPresent(subActualObj -> {
          try {
            setResult(verify(subActualObj, (Matcher<HashMap<String, Object>>) objMatcherClass
              .getMethod("sameAs", expectObj.getClass()).invoke(objMatcherClass.newInstance(), expectObj), mismatchDescriber));
          } catch (Exception e) {
            setResult(false);
            mismatchDescriber.appendText(e.getMessage() + "\n");
          }
        });
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("should be the same as " + getClassName(expectedObjects));
      }

      @Override
      protected void describeMismatchSafely(List<HashMap<String, Object>> item, Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }

    };
  }

  static Optional<HashMap<String, Object>> assignOpt(List<HashMap<String, Object>> actualObjects,
                                                     Object expectObj, String... keys) throws Exception {
    for (HashMap<String, Object> actualObj : actualObjects) {
      if (compare(actualObj, expectObj, keys)) {
        return Optional.of(actualObj);
      }
    }
    return Optional.empty();
  }

  static boolean compare(HashMap<String, Object> actualObj, Object expectObj, String... keys) throws Exception {
    String expectValue;
    for (String str : keys) {
      try {
        expectValue = String.valueOf(expectObj.getClass().getMethod(fieldToGetMethod(str)).invoke(expectObj));
      } catch (Exception e) {
        throw e;
      }
      if (!String.valueOf(actualObj.get(str)).equals(expectValue)) {
        return false;
      }
    }
    return true;
  }

  static String fieldToGetMethod(String field) {
    return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
  }

  static String getClassName(List<?> expectedObjects) {
    return expectedObjects.isEmpty() ? "" : expectedObjects.get(0).getClass().getSimpleName();
  }

}
