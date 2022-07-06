package com.tcbs.automation.models;

import java.util.Date;

import com.tcbs.automation.tools.CompareUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUploadedDocumentDto {

  private String uploaderTcbsId;

  private String updoadedFileUrl;

  private Date uploadedDate;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) return CompareUtils.isObjEmpty(this);
    if (getClass() != obj.getClass())
      return false;
    ProfileUploadedDocumentDto other = (ProfileUploadedDocumentDto) obj;
    if (updoadedFileUrl == null) {
      if (other.updoadedFileUrl != null)
        return false;
    } else if (!updoadedFileUrl.equals(other.updoadedFileUrl))
      return false;
    if (uploadedDate == null) {
      if (other.uploadedDate != null)
        return false;
    } else if (!uploadedDate.equals(other.uploadedDate))
      return false;
    if (uploaderTcbsId == null) {
      if (other.uploaderTcbsId != null)
        return false;
    } else if (!uploaderTcbsId.equals(other.uploaderTcbsId))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((updoadedFileUrl == null) ? 0 : updoadedFileUrl.hashCode());
    result = prime * result + ((uploadedDate == null) ? 0 : uploadedDate.hashCode());
    result = prime * result + ((uploaderTcbsId == null) ? 0 : uploaderTcbsId.hashCode());
    return result;
  }

}
