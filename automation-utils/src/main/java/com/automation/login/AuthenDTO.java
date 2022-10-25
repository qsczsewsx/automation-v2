package com.automation.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenDTO {
  private String xxxxId;
  private String otp;
  private String otpTypeName;
  private String isValid;
  private String token;
}
