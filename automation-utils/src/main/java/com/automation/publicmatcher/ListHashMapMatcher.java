package com.automation.publicmatcher;

import com.automation.tools.CompareUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.automation.publicmatcher.OptionalMatchers.isPresent;
import static com.automation.publicmatcher.Verifier.verify;

public class ListHashMapMatcher {

  public static Matcher<List<HashMap<String, Object>>> sameAs(List<HashMap<String, Object>> expectedObjects, Class objMatcherClass, String... keys) {
    return new TypeSafeMatcher<List<HashMap<String, Object>>>() {
      final Description mismatchDescriber = new StringDescription();
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
        String key4 = keys.length > 3 ? keys[3] : null;
        if (actualObjects.isEmpty() && expectedObjects.isEmpty()) {
          setResult(true);
        }

        if (expectedObjects.size() != actualObjects.size()) {
          setResult(false);
          mismatchDescriber.appendText(String.format("%s list size: expected [%s] but was [%s].\n",
            expectedObjects.isEmpty() ? "" : expectedObjects.get(0).getClass().getSimpleName(),
            expectedObjects.size(),
            actualObjects.size()
          ));
        } else {
          expectedObjects.forEach(expectObj -> {
            Optional<HashMap<String, Object>> optActualObj = Optional.empty();
            for (HashMap<String, Object> actualObj : actualObjects) {
              if (
                CompareUtils.equalsWithNulls(actualObj.get(key1), expectObj.get(key1))
                  && CompareUtils.equalsWithNulls(actualObj.get(key2), expectObj.get(key2))
                  && CompareUtils.equalsWithNulls(actualObj.get(key3), expectObj.get(key3))
                  && CompareUtils.equalsWithNulls(actualObj.get(key4), expectObj.get(key4))) {
                optActualObj = Optional.of(actualObj);
                break;
              }
            }
            if (!optActualObj.isPresent()) {
              mismatchDescriber.appendText("idInDB: " + expectObj + " Was missing in actual result\n");
            }

            setResult(Verifier.verify(optActualObj, isPresent(), mismatchDescriber));
            optActualObj.ifPresent(subActualObj -> {
              try {
                setResult(Verifier.verify(
                  subActualObj,
                  (Matcher<HashMap<String, Object>>) objMatcherClass
                    .getMethod("sameAs", HashMap.class)
                    .invoke(objMatcherClass.newInstance(), expectObj),
                  mismatchDescriber)
                );
              } catch (Exception e) {
                setResult(false);
                mismatchDescriber.appendText(e.getMessage() + "\n");
                e.printStackTrace();
              }
            });
          });
        }
        return getResult();
      }

      @Override
      public void describeTo(Description description) {
        if (expectedObjects.size() > 0) {
          description.appendText("should be the same as " + expectedObjects.get(0).getClass().getSimpleName());
        } else {
          description.appendText("should be the same as expected result");
        }
      }

      @Override
      protected void describeMismatchSafely(List<HashMap<String, Object>> item, Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }
    };
  }
}
