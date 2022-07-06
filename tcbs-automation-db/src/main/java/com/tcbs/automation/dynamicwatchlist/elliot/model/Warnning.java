package com.tcbs.automation.dynamicwatchlist.elliot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity("warning_collection")
public class Warnning {
  @Property(value = "ticker")
  String ticker;

  @Property(value = "tcbsid")
  String tcbsid;

  @Property(value = "description")
  String description;

  @Property(value = "created_on")
  Double created_on;
}