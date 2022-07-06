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
public class CombinedData {
  @Property(value = "sma5")
  Double sma5;

  @Property(value = "sma10")
  Double sma10;

  @Property(value = "sma20")
  Double sma20;

  @Property(value = "forecastingVolume")
  Double forecastingVolume;

  @Property(value = "price")
  Double price;

  @Property(value = "volume")
  Double volume;

  @Property(value = "rsi14")
  Double rsi14;

  @Property(value = "calculatedAt")
  LocalDateTime calculatedAt;
}
