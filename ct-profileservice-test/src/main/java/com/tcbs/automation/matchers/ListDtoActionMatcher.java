package com.tcbs.automation.matchers;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import com.tcbs.automation.models.ProfileDto;

public class ListDtoActionMatcher {

  public static Matcher<List<ProfileDto>> sameAs(boolean forcedIgnoreCompare,
      List<ProfileDto> expectedResult) {

    return new TypeSafeMatcher<List<ProfileDto>>() {
      Description mismatchDescriber = new StringDescription();

      @Override
      protected boolean matchesSafely(List<ProfileDto> actualAPI) {
        if (null == actualAPI && null == expectedResult)
          return true;
        if (actualAPI.size() != expectedResult.size())
          return false;

        if (forcedIgnoreCompare)
          return true;

        final List<ProfileDto> matcherList = new ArrayList<>();
        matcherList.addAll(expectedResult);
        matcherList.removeAll(actualAPI);

        if (matcherList.size() == 0)
          return true;

        return false;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("\nProfileDto from API should be the same as expected result");
      }

      @Override
      protected void describeMismatchSafely(List<ProfileDto> theMismatchItem,
          Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }
    };
  }
}