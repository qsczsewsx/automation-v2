package com.tcbs.automation.other;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/CancelTaskRetryManualOtherWithTaskId.csv", separator = '|')
public class CancelTaskRetryManualWithTaskIdTest {
  @Getter
  private String testCaseName;


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify cancel retry manual other with taskId")
  public void perfomTest() {

  }
}
