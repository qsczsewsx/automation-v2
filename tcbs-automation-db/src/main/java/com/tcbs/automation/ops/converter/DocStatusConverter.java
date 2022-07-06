package com.tcbs.automation.ops.converter;

import com.tcbs.automation.ops.DocStatus;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class DocStatusConverter implements AttributeConverter<DocStatus, String> {

  @Override
  public String convertToDatabaseColumn(DocStatus docStatus) {
    if (docStatus == null) {
      return null;
    }
    return docStatus.getValue();
  }

  @Override
  public DocStatus convertToEntityAttribute(String value) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    Optional<DocStatus> comp = Stream.of(DocStatus.values()).filter(e -> e.getValue().equals(value)).findFirst();
    if (comp.isPresent()) {
      return comp.get();
    }
    return null;
  }
}
