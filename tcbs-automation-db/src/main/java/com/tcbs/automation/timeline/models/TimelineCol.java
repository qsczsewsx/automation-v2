package com.tcbs.automation.timeline.models;

import com.tcbs.automation.timeline.TimelineDef;
import com.tcbs.automation.timeline.TimelineEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimelineCol {
  private List<TimelineEvent> events;

  private List<TimelineDef> defs;
}
