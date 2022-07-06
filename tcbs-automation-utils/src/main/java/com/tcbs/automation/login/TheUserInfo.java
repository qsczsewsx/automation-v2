package com.tcbs.automation.login;

import net.serenitybdd.screenplay.Question;

import java.util.List;

import static com.tcbs.automation.config.common.CommonKey.JWT_INFO;
import static com.tcbs.automation.config.common.CommonKey.LOGIN_INFO;


public class TheUserInfo {

  public static Question<LoginData> aboutLoginData() {
    return Question.about("login data")
      .answeredBy(
        actor -> actor.recall(LOGIN_INFO)
      );
  }

  public static Question<JwtDto> aboutJwtInfo() {
    return Question.about("jwt info")
      .answeredBy(
        actor -> actor.recall(JWT_INFO)
      );
  }

  public static Question<JwtBody> aboutJwtBody() {
    return Question.about("jwt body")
      .answeredBy(
        actor -> {
          JwtDto jwtDto = actor.asksFor(TheUserInfo.aboutJwtInfo());
          return jwtDto.getBody();
        }
      );
  }

  public static Question<List<String>> aboutUserRoles() {
    return Question.about("user roles")
      .answeredBy(
        actor -> {
          JwtDto jwtDto = actor.asksFor(TheUserInfo.aboutJwtInfo());
          return jwtDto.getBody().getRoles();
        }
      );
  }

  public static Question<JwtHeader> aboutJwtHeader() {
    return Question.about("jwt header")
      .answeredBy(
        actor -> {
          JwtDto jwtDto = actor.asksFor(TheUserInfo.aboutJwtInfo());
          return jwtDto.getHeader();
        }
      );
  }
}
