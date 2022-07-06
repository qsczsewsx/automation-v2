package com.tcbs.automation.abilities;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Question;
import net.thucydides.core.annotations.Step;

public abstract class RememberThat implements Performable {
  public RememberThat() {
  }

  public static MemoryBuilder theValueOf(String memoryKey) {
    return new RememberThat.MemoryBuilder(memoryKey);
  }

  public static class MemoryBuilder {
    private final String memoryKey;

    MemoryBuilder(String memoryKey) {
      this.memoryKey = memoryKey;
    }

    public RememberThat is(Object value) {
      return (RememberThat) Instrumented.instanceOf(RememberThat.WithValue.class).withProperties(new Object[] {this.memoryKey, value});
    }

    public RememberThat isAnsweredBy(Question<?> value) {
      return (RememberThat) Instrumented.instanceOf(RememberThat.WithQuestion.class).withProperties(new Object[] {this.memoryKey, value});
    }
  }

  public static class WithQuestion extends RememberThat {
    private final String memoryKey;
    private final Question<?> question;

    public WithQuestion(String memoryKey, Question<?> question) {
      this.memoryKey = memoryKey;
      this.question = question;
    }

    @Step("{0} remembered #memoryKey")
    public <T extends Actor> void performAs(T actor) {
      actor.remember(this.memoryKey, this.question.answeredBy(actor));
    }
  }

  public static class WithValue extends RememberThat {
    private final String memoryKey;
    private final Object value;

    public WithValue(String memoryKey, Object value) {
      this.memoryKey = memoryKey;
      this.value = value;
    }

    @Step("{0} remembered #memoryKey")
    public <T extends Actor> void performAs(T actor) {
      actor.remember(this.memoryKey, this.value);
    }
  }
}
