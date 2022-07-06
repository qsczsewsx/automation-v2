package com.tcbs.automation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoKyc {

  private String videoKycUrl;

  private String objectId;

  private String videoKycStatus;

  private String videoKycDenyReason;

  private String videoKycDenyContent;

  private String textTranscriptFileName;

  private String textTranscriptUrl;

  private String audioTranscriptFileName;

  private String audioTranscriptUrl;

}
