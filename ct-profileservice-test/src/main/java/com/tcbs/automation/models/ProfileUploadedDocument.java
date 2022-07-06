package com.tcbs.automation.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUploadedDocument {

  private String uploaderTcbsId;

  private String updoadedFileUrl;

  private Date uploadedDate;

}
