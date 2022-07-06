package com.tcbs.automation.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDto {
  private JwtHeader header;
  private JwtBody body;

  public JwtDto(JwtHeader header, JwtBody body) {
    this.header = header;
    this.body = body;
  }
}
