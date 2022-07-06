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
public class ProfileBookDto {

  private Long bookId;

  private String bookStatus;

  private String bookNumber;

  private String bookType;

  private Date bookReceivedDate;

  private String bookManagerGroup;

  private String bookManagerUser;

  private String bookNote;

  private String bookEmailRMUpload;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) return CompareUtils.isObjEmpty(this);
    if (getClass() != obj.getClass())
      return false;
    ProfileBookDto other = (ProfileBookDto) obj;
    if (bookEmailRMUpload == null) {
      if (other.bookEmailRMUpload != null)
        return false;
    } else if (!bookEmailRMUpload.equals(other.bookEmailRMUpload))
      return false;
    if (bookId == null) {
      if (other.bookId != null)
        return false;
    } else if (!bookId.equals(other.bookId))
      return false;
    if (bookManagerGroup == null) {
      if (other.bookManagerGroup != null)
        return false;
    } else if (!bookManagerGroup.equals(other.bookManagerGroup))
      return false;
    if (bookManagerUser == null) {
      if (other.bookManagerUser != null)
        return false;
    } else if (!bookManagerUser.equals(other.bookManagerUser))
      return false;
    if (bookNote == null) {
      if (other.bookNote != null)
        return false;
    } else if (!bookNote.equals(other.bookNote))
      return false;
    if (bookNumber == null) {
      if (other.bookNumber != null)
        return false;
    } else if (!bookNumber.equals(other.bookNumber))
      return false;
    if (bookReceivedDate == null) {
      if (other.bookReceivedDate != null)
        return false;
    } else if (!bookReceivedDate.equals(other.bookReceivedDate))
      return false;
    if (bookStatus == null) {
      if (other.bookStatus != null)
        return false;
    } else if (!bookStatus.equals(other.bookStatus))
      return false;
    if (bookType == null) {
      if (other.bookType != null)
        return false;
    } else if (!bookType.equals(other.bookType))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bookEmailRMUpload == null) ? 0 : bookEmailRMUpload.hashCode());
    result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
    result = prime * result + ((bookManagerGroup == null) ? 0 : bookManagerGroup.hashCode());
    result = prime * result + ((bookManagerUser == null) ? 0 : bookManagerUser.hashCode());
    result = prime * result + ((bookNote == null) ? 0 : bookNote.hashCode());
    result = prime * result + ((bookNumber == null) ? 0 : bookNumber.hashCode());
    result = prime * result + ((bookReceivedDate == null) ? 0 : bookReceivedDate.hashCode());
    result = prime * result + ((bookStatus == null) ? 0 : bookStatus.hashCode());
    result = prime * result + ((bookType == null) ? 0 : bookType.hashCode());
    return result;
  }
}
