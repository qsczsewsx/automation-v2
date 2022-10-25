package com.automation.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtHeader {
  private String alg;
  private String typ;
}
