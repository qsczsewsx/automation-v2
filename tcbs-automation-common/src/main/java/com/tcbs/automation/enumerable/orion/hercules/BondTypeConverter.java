package com.tcbs.automation.enumerable.orion.hercules;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class BondTypeConverter implements AttributeConverter<BondType, String> {

  @Override
  public String convertToDatabaseColumn(BondType docType) {
    if (docType == null) {
      return null;
    }
    return docType.getValue();
  }

  @Override
  public BondType convertToEntityAttribute(String value) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    Optional<BondType> comp = Stream.of(BondType.values()).filter(e -> e.getValue().equals(value)).findFirst();
    return comp.orElse(null);
  }
}
