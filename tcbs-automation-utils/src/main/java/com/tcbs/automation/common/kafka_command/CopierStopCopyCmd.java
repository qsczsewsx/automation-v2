package com.tcbs.automation.common.kafka_command;

import com.tcbs.automation.constants.coco.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class CopierStopCopyCmd extends BaseCmd {
  private Account copier;
  private Constants.CopierStopCopyAction action;
  private Constants.CopierStopCopyType type;
  private boolean isAdvance;
}
