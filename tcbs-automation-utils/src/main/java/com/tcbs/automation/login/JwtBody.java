package com.tcbs.automation.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtBody {
  private String iss;
  private Long exp;
  private String jti;
  private String iat;
  private String sub;
  private String custodyID;
  private String email;
  private List<String> roles;
  @JsonProperty("stepup_exp")
  @SerializedName("stepup_exp")
  private Long stepupExp;
  @JsonProperty("sotp_sign")
  @SerializedName("sotp_sign")
  private String sotpSign;
  @JsonProperty("client_key")
  private String clientKey;
  @JsonProperty("sessionID")
  private String sessionId;
  @JsonProperty("account_status")
  private String accountStatus;
  private String otp;
  private String otpType;
  private String otpSource;
}
