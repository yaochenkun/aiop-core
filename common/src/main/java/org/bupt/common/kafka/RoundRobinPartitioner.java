package org.bupt.common.kafka;


import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * 以轮询策略选择partition
 * 生产者将消息以这种策略放入多个partition
 */
public class RoundRobinPartitioner implements Partitioner {

	@Override
	public void configure(Map<String, ?> arg0) {
	}

	@Override
	public void close() {
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic); // 获取topic对应的partitions数量
		int numPartitions = partitions.size();
		int partitionNum;
		try {
			partitionNum = Integer.parseInt((String) key);
		} catch (Exception e) {
			partitionNum = key.hashCode() ;
		}
		return Math.abs(partitionNum  % numPartitions);
	}
}