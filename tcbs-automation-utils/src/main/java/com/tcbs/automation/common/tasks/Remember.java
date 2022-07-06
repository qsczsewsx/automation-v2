package com.tcbs.automation.common.tasks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

@Getter
@Setter
@NoArgsConstructor
public class Remember implements Task {
  private String rememberKey = "rememberKey";
  private Question<?> answer;
  private static Object values;
  private static int type;

  public Remember(Question<?> answer) {
    this.answer = answer;
  }

  public static Remember theValue(Object value) {
    type = 1;
    values = value;
    return instrumented(Remember.class);
  }

  public static Remember theAnswerOf(Question<?> answer) {
    type = 2;
    return instrumented(Remember.class, answer);
  }

  public static Remember theQuestion(Question<?> answer) {
    type = 3;
    return instrumented(Remember.class, answer);
  }

  public Remember as(String rememberKey) {
    this.rememberKey = rememberKey;
    return this;
  }

  @Override
  public <T extends Actor> void performAs(T user) {
    switch (type) {
      case 1:
        user.remember(rememberKey, values);
        break;
      case 2:
        user.remember(rememberKey, answer.answeredBy(user));
        break;
      case 3:
        user.remember(rememberKey, answer);
        break;
      default:
        user.remember(rememberKey, type);
        break;
    }
  }
}
