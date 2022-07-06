package com.tcbs.automation.matchers;

import com.tcbs.automation.models.ProfileDto;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

public class SingleDtoActionMatcher {

  public static Matcher<ProfileDto> sameAs(List<ProfileDto> expectedResult) {

    return new TypeSafeMatcher<ProfileDto>() {
      Description mismatchDescriber = new StringDescription();

      @Override
      protected boolean matchesSafely(ProfileDto actualAPI) {
        if (1 != expectedResult.size()) return false;
        return expectedResult.get(0).equals(actualAPI);
      }

      @Override
      public void describeTo(Description description) {
        description
          .appendText("\nProfileDto from API should be the same as expected result");
      }

      @Override
      protected void describeMismatchSafely(ProfileDto theMismatchItem,
                                            Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }
    };
  }
}