package com.tcbs.automation.coco.mongodb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity("userProfile")
public class UserProfile {
  @Property("_id")
  private String id;
  private String custodyId;
  private String fullname;
}
