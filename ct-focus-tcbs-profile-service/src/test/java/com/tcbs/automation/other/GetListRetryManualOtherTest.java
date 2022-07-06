package com.tcbs.automation.other;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GetListRetryManualOther.csv", separator = '|')
public class GetListRetryManualOtherTest {
  @Getter
  private String testCaseName;


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify get list retry manual other")
  public void perfomTest() {

  }
}
