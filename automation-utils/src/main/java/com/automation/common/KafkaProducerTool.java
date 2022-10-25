package com.automation.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.function.Consumer;

import static com.automation.config.coco.CocoServiceConfig.*;

public class KafkaProducerTool {
  private static final Logger logger = LoggerFactory.getLogger(KafkaProducerTool.class);
  private static final Producer<String, String> producer = new KafkaProducer<>(getProducerProps());

  private static Properties getProducerProps() {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, COCO_KAFKA_SERVER);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put("sasl.mechanism", "PLAIN");
    props.put("security.protocol", "SASL_PLAINTEXT");
    props.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required " +
      "username=\"" + COCO_KAFKA_USER + "\" " +
      "password=\"" + COCO_KAFKA_PASS + "\";");
    return props;
  }

  public static void sendCommand(Object command, String topic, Integer partition, Consumer<Throwable> errorHandler) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    try {
      ProducerRecord<String, String> record =
        new ProducerRecord<>(topic, partition, null, objectMapper.writeValueAsString(command));
      RecordMetadata metadata = producer.send(record).get();
    } catch (Exception ex) {
      if (ex instanceof JsonProcessingException) {
        logger.error("object mapping failed {}", command);
      } else {
        logger.error("produce msg to kafka failed: ", ex);
      }
      if (errorHandler != null) {
        errorHandler.accept(ex);
      }
    }
  }

  public static void close() {
    producer.close();
  }
}
