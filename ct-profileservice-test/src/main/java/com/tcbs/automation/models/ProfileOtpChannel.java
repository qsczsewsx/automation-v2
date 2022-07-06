package com.tcbs.automation.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileOtpChannel {

  private String defaultChannel;

  private List<String> supportedChannels;
}
