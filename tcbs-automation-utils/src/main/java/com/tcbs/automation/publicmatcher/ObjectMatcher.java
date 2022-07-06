package com.tcbs.automation.publicmatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.OptionalMatchers.isPresent;
import static com.tcbs.automation.publicmatcher.Verifier.verify;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class ObjectMatcher {
  private static final Logger logger = LoggerFactory.getLogger(ObjectMatcher.class);

  /***
   *
   * @param expectedObjects list du lieu chuan dung de verify data
   * @param objMatcherClass class override matcher tung object
   * @return matcher
   *
   * example: org.hamcrest.MatcherAssert.assertThat( actualListObj , ObjectMatcher.sameAs(expectedListObj, SingleObjectMatcher.class))
   */
  public static Matcher<List<HashMap<String, Object>>> sameAs(List<?> expectedObjects, Class objMatcherClass, String... actualId) {
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
            String nameId = null;
            Class clazz = expectObj.getClass();
            for (Field f : clazz.getDeclaredFields()) {
              if (f.getAnnotation(Id.class) != null) {
                nameId = f.getName();
                break;
              }
            }
            if (nameId == null) {
              nameId = actualId[0];
            }
            if (nameId == null) {
              setResult(false);
              mismatchDescriber.appendText("Missing identifier. Annotation @Id is required to indicate identifier.\n");
              throw new NullPointerException("Missing identifier. Annotation @Id is required to indicate identifier.");
            }
            String finalNameId = nameId;
            String idInDB = null;
            try {
              idInDB = String.valueOf(clazz.getMethod(fieldToGetMethod(finalNameId)).invoke(expectObj));
            } catch (Exception e) {
              setResult(false);
              mismatchDescriber.appendText(e.getMessage() + "\n");
              logger.error(e.getMessage(), e.getStackTrace());
            }
            Optional<HashMap<String, Object>> optActualObj = Optional.empty();
            for (HashMap<String, Object> actualObj : actualObjects) {
              String idInApi = actualId.length > 0 ? String.valueOf(actualObj.get(actualId[0])) : String.valueOf(actualObj.get(nameId));
              if (idInApi.equals(idInDB)) {
                optActualObj = Optional.of(actualObj);
                break;
              }
            }
            if (!optActualObj.isPresent()) {
              mismatchDescriber.appendText("idInDB: " + idInDB + " Was missing in actual result\n");
            }
            setResult(verify(optActualObj, isPresent(), mismatchDescriber));
            optActualObj.ifPresent(subActualObj -> {
              try {
                setResult(verify(
                  subActualObj,
                  (Matcher<HashMap<String, Object>>) objMatcherClass.getMethod("sameAs", clazz).invoke(objMatcherClass.newInstance(), expectObj),
                  mismatchDescriber)
                );
              } catch (Exception e) {
                setResult(false);
                mismatchDescriber.appendText(e.getMessage() + "\n");
                logger.error(e.getMessage(), e.getStackTrace());
              }
            });
          });
        }
        return getResult();
      }

      String fieldToGetMethod(String field) {
        return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
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

  public static Matcher<Object> equalTo(Object expectedObject, String tcName) {
    return new TypeSafeMatcher<Object>() {
      final Description mismatchDescriber = new StringDescription();

      @SuppressWarnings("unchecked")
      @Override
      protected boolean matchesSafely(Object actualObject) {
        return matchesSafelyObject(actualObject, expectedObject, mismatchDescriber, tcName);
      }

      @Override
      public void describeTo(Description description) {

        description.appendText("should be the same as " + expectedObject.getClass().getSimpleName());
      }

      @Override
      protected void describeMismatchSafely(Object item, Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }
    };
  }

  public static Matcher<Object> matchWith(Object expectedObject, String tcName) {
    return new TypeSafeMatcher<Object>() {
      final Description mismatchDescriber = new StringDescription();

      @SuppressWarnings("unchecked")
      @Override
      protected boolean matchesSafely(Object actualObject) {
        return matchesObject(actualObject, expectedObject, mismatchDescriber, tcName);
      }

      @Override
      public void describeTo(Description description) {

        description.appendText("should be the same as " + expectedObject.getClass().getSimpleName());
      }

      @Override
      protected void describeMismatchSafely(Object item, Description mismatchDescription) {
        mismatchDescription.appendText(mismatchDescriber.toString());
      }
    };
  }

  public static boolean matchesSafelyObject(Object actualObject, Object expectedObject, Description mismatchDescriber, String tcName) {
    if (!actualObject.getClass().getName().equals(expectedObject.getClass().getName())) {
      mismatchDescriber.appendText("Error: Actual Object va expect Object khac kieu");
      return false;
    } else {
      CombinableMatcher matcher = null;
      mismatchDescriber.appendText("\n====== Mismatch result: " + tcName + " ======");
      Class clazz = expectedObject.getClass();
      boolean theFirstFlag = true;
      try {
        for (Field field : clazz.getDeclaredFields()) {
          String expectedValue = field.get(expectedObject) != null ? field.get(expectedObject).toString() : null;
          if (expectedValue == null || !expectedValue.equals("IGNORE")) {
            if (theFirstFlag) {
              matcher = all(hasProperty(field.getName(), is(field.get(expectedObject))));
              theFirstFlag = false;
            } else {
              matcher.and(hasProperty(field.getName(), is(field.get(expectedObject))));
            }
          }
        }
        if (matcher != null) {
          return verify(actualObject,
            matcher,
            mismatchDescriber);
        } else {
          mismatchDescriber.appendText("Expected object is null, check it please!");
          return false;
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e.getStackTrace());
        return false;
      }
    }
  }

  public static boolean matchesObject(Object actualObject, Object expectedObject, Description mismatchDescriber, String tcName) {
    if (!actualObject.getClass().getName().equals(expectedObject.getClass().getName())) {
      mismatchDescriber.appendText("Error: Actual Object va expect Object khac kieu");
      return false;
    } else {
      CombinableMatcher matcher = null;
      mismatchDescriber.appendText("\n====== Mismatch result: " + tcName + " ======");
      Class clazz = expectedObject.getClass();
      boolean theFirstFlag = true;
      try {
        for (Field field : clazz.getDeclaredFields()) {
          if (field.get(expectedObject) == null) {
            continue;
          }
          String expectedValue = field.get(expectedObject).toString();
          if (expectedValue != null || !expectedValue.equals("IGNORE")) {
            if (theFirstFlag) {
              matcher = all(hasProperty(field.getName(), is(field.get(expectedObject))));
              theFirstFlag = false;
            } else {
              matcher.and(hasProperty(field.getName(), is(field.get(expectedObject))));
            }
          }
        }
        if (matcher != null) {
          return verify(actualObject,
            matcher,
            mismatchDescriber);
        } else {
          mismatchDescriber.appendText("Expected object is null, check it please!");
          return false;
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e.getStackTrace());
        return false;
      }
    }
  }
}
