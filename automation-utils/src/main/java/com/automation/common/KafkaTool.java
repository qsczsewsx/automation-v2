package com.automation.common;

import org.apache.kafka.common.serialization.StringDeserializer;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static com.automation.config.common.CommonConfig.*;

@Getter
public class KafkaTool {
  private KafkaConsumer<String, String> kafkaConsumer;
  private final AtomicLong maxTimestamp = new AtomicLong();

  public KafkaTool(String topic) {
    try {
      this.kafkaConsumer = new KafkaConsumer<String, String>(
        getKafkaConsumerProp(KAFKA_USER, KAFKA_PASS, KAFKA_SERVER, KAFKA_GROUP));
      this.kafkaConsumer.subscribe(Collections.singletonList(topic));
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      if (this.kafkaConsumer != null) {
        this.kafkaConsumer.close();
      }
    }
  }

  public KafkaTool(String topic, String user, String password, String server, String group) {
    try {
      this.kafkaConsumer = new KafkaConsumer<String, String>(getKafkaConsumerProp(user, password, server, group));
      this.kafkaConsumer.subscribe(Collections.singletonList(topic));
      this.maxTimestamp.set(new Date().getTime());
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      if (this.kafkaConsumer != null) {
        this.kafkaConsumer.close();
      }
    }
  }

  public List<String> getMessages(long durationMillisecond) {
    List<String> result = new ArrayList<>();
    ConsumerRecords<String, String> consumerRecords = this.kafkaConsumer.poll(Duration.ofMillis(durationMillisecond));
    for (ConsumerRecord<String, String> record : consumerRecords) {
      if (record.timestamp() > maxTimestamp.get()) {
        String msgRecv = record.value();
        System.out.println(msgRecv);
        result.add(msgRecv);
      }
    }
    this.maxTimestamp.set(new Date().getTime());
    return result;
  }

  public void close() {
    this.kafkaConsumer.close();
  }

  private Properties getKafkaConsumerProp(String user, String password, String server, String group) {
    Properties props = new Properties();
    props.put("bootstrap.servers", server);
    props.put("key.deserializer", StringDeserializer.class);
    props.put("value.deserializer", StringDeserializer.class);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    props.put("sasl.mechanism", "PLAIN");
    props.put("security.protocol", "SASL_PLAINTEXT");
    props.put(SaslConfigs.SASL_JAAS_CONFIG, getKafkaAuthen(user, password));
    return props;
  }

  public static String getKafkaAuthen(String user, String password) {
    return "org.apache.kafka.common.security.plain.PlainLoginModule required " +
      "username=\"" + user + "\" " +
      "password=\"" + password + "\";";
  }
}
