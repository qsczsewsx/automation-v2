package com.tcbs.automation.dynamicwatchlist.elliot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Property;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordString {
  @Property(value = "value")
  String value;
  @Property(value = "time")
  LocalDateTime time;
}
