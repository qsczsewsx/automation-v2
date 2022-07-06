package com.tcbs.automation.models;

import java.util.List;

import com.tcbs.automation.tools.CompareUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileOtpChannelDto {
  private String defaultChannel;

  private List<String> supportedChannels;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) return CompareUtils.isObjEmpty(this);
    if (getClass() != obj.getClass())
      return false;
    ProfileOtpChannelDto other = (ProfileOtpChannelDto) obj;
    if (defaultChannel == null) {
      if (other.defaultChannel != null)
        return false;
    } else if (!defaultChannel.equals(other.defaultChannel))
      return false;
    if (supportedChannels == null) {
      if (other.supportedChannels != null)
        return false;
    } else if (!supportedChannels.equals(other.supportedChannels))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((defaultChannel == null) ? 0 : defaultChannel.hashCode());
    result = prime * result + ((supportedChannels == null) ? 0 : supportedChannels.hashCode());
    return result;
  }

}
