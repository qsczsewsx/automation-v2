package com.tcbs.automation.tools;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

public class Logging {
  private Logger logger;

  public Logging() {
    this.logger = Logger.getLogger(Logging.class);
  }

  public Logging(Class<?> clazz) {
    this.logger = Logger.getLogger(clazz);
  }

  public static StringWriter getStackMessage(Exception e) {
    StringWriter stack = new StringWriter();
    e.printStackTrace(new PrintWriter(stack));
    return stack;
  }

  public void fatal(String msg) {
    this.logger.fatal(msg);
  }

  public void error(String msg) {
    this.logger.error(msg);
  }

  public void error(String msg, Exception e) {
    this.logger.error(msg, e);
  }

  public void error(Exception e) {
    this.logger.error((Object) null, e);
  }

  public void warn(String msg) {
    this.logger.warn(msg);
  }

  public void info(String msg) {
    this.logger.info(msg);
  }

  public void debug(String msg) {
    this.logger.debug(msg);
  }

  public void trace(String msg) {
    this.logger.trace(msg);
  }
}
