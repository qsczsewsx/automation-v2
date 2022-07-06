package com.tcbs.automation.abilities;

import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Description;

public class SoftAsserts extends SoftAssertions {

  public SoftAsserts() {
    super();
  }

  /**
   * This method should only be used with hamcrest matcher matchSafely
   *
   * @param descriptor: org.hamcrest.StringDescription from hamcrest matchSafely to describe errors.
   * @return Return results whether all assertions are passed or failed
   */
  public boolean assertAll(Description descriptor) {
    boolean result = true;
    if (!this.errorsCollected().isEmpty()) {
      result = false;
      this.errorsCollected().forEach(error -> {
        String[] message = error.getMessage().split("\n", 3);
        descriptor.appendText(String.join("\n", message[0], message[1], "\n"));
      });
    }
    return result;
  }

}
