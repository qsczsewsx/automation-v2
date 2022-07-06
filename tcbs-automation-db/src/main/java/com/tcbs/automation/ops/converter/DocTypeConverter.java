package com.tcbs.automation.ops.converter;

import com.tcbs.automation.ops.DocType;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class DocTypeConverter implements AttributeConverter<DocType, String> {

  @Override
  public String convertToDatabaseColumn(DocType docType) {
    if (docType == null) {
      return null;
    }
    return docType.getValue();
  }

  @SneakyThrows
  @Override
  public DocType convertToEntityAttribute(String value) {
    Optional<DocType> comp = Stream.of(DocType.values()).filter(e -> e.getValue().equals(value)).findFirst();
    if (comp.isPresent()) {
      return comp.get();
    }
    throw new Exception("Unknown DocType: " + value);
  }
}
