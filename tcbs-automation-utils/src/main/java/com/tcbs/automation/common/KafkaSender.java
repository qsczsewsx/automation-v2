package com.tcbs.automation.common;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

import static com.tcbs.automation.common.KafkaTool.getKafkaAuthen;
import static com.tcbs.automation.config.common.CommonConfig.*;

public class KafkaSender {
  private KafkaProducer<String, String> kafkaProducer;

  private KafkaSender() {}

  public static KafkaSender init() {
    KafkaSender kafkaSender = new KafkaSender();
    kafkaSender.kafkaProducer = new KafkaProducer<String, String>(getKafkaProducerProp(KAFKA_USER, KAFKA_PASS, KAFKA_SERVER));
    return kafkaSender;
  }

  public static KafkaSender init(String user, String password, String server) {
    KafkaSender kafkaSender = new KafkaSender();
    kafkaSender.kafkaProducer = new KafkaProducer<String, String>(getKafkaProducerProp(user, password, server));
    return kafkaSender;
  }

  public void send(String topic, String data) {
    this.kafkaProducer.send(new ProducerRecord<String, String>(topic, data));
    this.kafkaProducer.flush();
  }

  public void close() {
    this.kafkaProducer.close();
  }


  private static Properties getKafkaProducerProp(String user, String password, String server) {
    Properties props = new Properties();
    props.put("bootstrap.servers", server);
    props.put("key.serializer", StringSerializer.class);
    props.put("value.serializer", StringSerializer.class);
    props.put("sasl.mechanism", "PLAIN");
    props.put("security.protocol", "SASL_PLAINTEXT");
    props.put(SaslConfigs.SASL_JAAS_CONFIG, getKafkaAuthen(user, password));
    return props;
  }
}
