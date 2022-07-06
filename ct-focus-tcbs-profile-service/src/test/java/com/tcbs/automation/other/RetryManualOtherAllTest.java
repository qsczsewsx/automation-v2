package com.tcbs.automation.other;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/RetryManualOtherAll.csv", separator = '|')
public class RetryManualOtherAllTest {
  @Getter
  private String testCaseName;


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify retry manual other all")
  public void perfomTest() {

  }
}
