package com.tcbs.automation.coco.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Entity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity("customerRatingSummary")
public class CustomerRatingSummary {
  private String tcbsID;
  private Integer totalScore;
  private Integer totalScoring;
}

