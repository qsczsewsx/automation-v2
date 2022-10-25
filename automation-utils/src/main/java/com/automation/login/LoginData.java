package com.automation.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginData {
  @JsonProperty("first_name")
  @SerializedName("first_name")
  private String firstName;
  @JsonProperty("last_name")
  @SerializedName("last_name")
  private String lastName;
  private String username;
  private String email;
  private String xxxxid;
  private String token;
  private String code;
  private String message;
  private String ichats_token;
}
