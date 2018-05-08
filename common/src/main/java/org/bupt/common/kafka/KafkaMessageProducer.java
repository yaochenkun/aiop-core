package org.bupt.common.kafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * kafka消息生产者
 */
public class KafkaMessageProducer extends Thread {

    private Logger logger = LoggerFactory.getLogger(KafkaMessageProducer.class);
    private Producer<Integer, String> producer;

    private int msgNumber;

    private static final int MAX_MSG_NUMBER = 1000; // 取模用，防msgNumber溢出

    public KafkaMessageProducer(String brokerAddressList) {

        Properties props = new Properties();

        // bootstrap.servers是新版的api
        props.put("bootstrap.servers", brokerAddressList);

        // ack方式，all，会等所有的commit，最慢的方式
        props.put("acks", "1");

        // 生产者放入partition的负载策略类
        props.put("partitioner.class", "org.bupt.common.kafka.RoundRobinPartitioner");

        // 键序列化类
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");

        // 值序列化类
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // // 失败是否重试，设置会有可能产生重复数据
        // props.put("retries", 0);
        //
        // // 对于每个partition的batch buffer大小
        // props.put("batch.size", 16384);
        //
        // // 等多久，如果buffer没满，比如设为1，即消息发送会多1ms的延迟，如果buffer没满
        // props.put("linger.ms", 1);
        //
        // // 整个producer可以用于buffer的内存大小
        // props.put("buffer.memory", 33554432);


        producer = new KafkaProducer<>(props);
    }

    /**
     * 发送消息
     */
    public void send(String topic, Map<String, Object> params) {

        msgNumber = (msgNumber + 1) % MAX_MSG_NUMBER; // msgNumber到MAX_MSG_NUMBER时会归零，防溢出
        String msgContent = JSON.toJSONString(params);

        // 发送
        producer.send(new ProducerRecord<>(topic, msgNumber, msgContent));

        logger.debug("producer thread: {} sent msg: {} msgContent: {}", Thread.currentThread().getId(), msgNumber, msgContent);
    }
}
