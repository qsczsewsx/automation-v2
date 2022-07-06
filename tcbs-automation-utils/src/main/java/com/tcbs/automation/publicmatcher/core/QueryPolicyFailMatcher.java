package com.tcbs.automation.publicmatcher.core;

import com.tcbs.automation.functions.core.FailResponse;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashMap;

import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static com.tcbs.automation.publicmatcher.Verifier.verify;
import static org.hamcrest.Matchers.equalTo;

public class QueryPolicyFailMatcher {

  public static Matcher<HashMap> sameAs(FailResponse expectedValue) {
    return new TypeSafeMatcher<HashMap>() {
      Description mismatchDescriber = new StringDescription();

      @SuppressWarnings("unchecked")
      @Override
      protected boolean matchesSafely(HashMap actualValue) {
        mismatchDescriber.appendText(String.format("\n====Validating negative policy has code is: <%s> and message is <%s> ======", expectedValue.getCode(), expectedValue.getMessage()));
        return verify(actualValue,
          all(
            has("code", equalTo(expectedValue.getCode())))
            .and(
              has("message", equalTo(expectedValue.getMessage().replace("''", " ")))),
          mismatchDescriber);
      }

      @Override
      public void describeTo(Description description) {

        description.appendText("\nValidating negative policy should be the same as expected result");
      }

      @Override
      protected void describeMismatchSafely(HashMap theMismatchItem, Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }
    };
  }
}