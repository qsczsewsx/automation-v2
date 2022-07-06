package com.tcbs.automation.coco;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnIndex {
  String name() default "";

  int index() default 0;

  String fmtIncaseDatetime() default "yyyy-MM-dd";

  boolean ignoreIfNull() default false;
}
