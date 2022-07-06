package com.tcbs.automation.publicmatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashMap;
import java.util.Map;

import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static com.tcbs.automation.publicmatcher.Verifier.verify;
import static org.hamcrest.Matchers.is;

public class HashMapPartialMatcher {
  public static Matcher<HashMap<String, Object>> matchWith(HashMap<String, Object> expected) {
    return new TypeSafeMatcher<HashMap<String, Object>>() {
      Description mismatchDescriber = new StringDescription();

      @SuppressWarnings("unchecked")
      @Override
      protected boolean matchesSafely(HashMap<String, Object> actualOrder) {
        mismatchDescriber.appendText("\n====== Mismatch result ======");
        boolean theFirstFlag = true;
        CombinableMatcher matcher = null;
        for (Map.Entry<String, Object> entry : expected.entrySet()) {
          if (theFirstFlag) {
            matcher = all(has(entry.getKey(), is(entry.getValue())));
            theFirstFlag = false;
          } else {
            matcher.and(has(entry.getKey(), is(entry.getValue())));
          }
        }

        if (matcher != null) {
          return verify(actualOrder,
            matcher,
            mismatchDescriber);
        } else {
          mismatchDescriber.appendText("Expected object is null, check it please!");
          return false;
        }
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("\nThe actual result should match with expected one");
      }

      @Override
      protected void describeMismatchSafely(HashMap<String, Object> theMismatchItem, Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }
    };
  }
}
