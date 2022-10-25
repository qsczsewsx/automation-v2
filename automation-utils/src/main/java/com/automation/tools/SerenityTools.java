package com.automation.tools;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFailure;

public class SerenityTools {
  public static void failTest(Throwable e) {
    StepEventBus.getEventBus().testFailed(e);
  }

  public static void failStep(Throwable e) {
    StepEventBus.getEventBus().stepFailed(
      new StepFailure(ExecutedStepDescription.withTitle(e.getMessage()), e));
  }

  public static void manualReport(String label, String data) {
    Serenity.recordReportData().withTitle(label)
      .andContents(data);
  }
}
