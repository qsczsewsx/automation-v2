package com.tcbs.automation.coco.socialinvest;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class PagingParam {
  protected int page;
  protected int size;
}
