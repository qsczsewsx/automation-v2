package com.tcbs.automation.coco.mongodb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcbs.automation.tools.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity("traderRatingInfo")
public class TraderRatingInfo {
  private String id;
  private String tcbsID;
  private Long traderID;
  private ArrayList<RatingItem> ratingList;
  private Integer totalScore;
  private Integer totalScoring;

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RatingItem {
    private String tcbsID;
    private int score;
    private String critical;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.ISO_DATE_NO_TIMEZONE, timezone = DateUtils.TIMEZONE_VN)
    private Date lastModified;
  }
}
