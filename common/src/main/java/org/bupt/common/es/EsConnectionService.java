package org.bupt.common.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * ElasticSearch连接获取
 * Created by ken on 2017/10/24.
 */
public class EsConnectionService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private TransportClient client;

	// 单节点
	public EsConnectionService(String host, Integer port) throws UnknownHostException {
		client = new PreBuiltTransportClient(Settings.EMPTY);
		client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
	}

	// 多节点（集群）
	public EsConnectionService(String[] hosts, Integer[] ports) throws UnknownHostException {
		client = new PreBuiltTransportClient(Settings.EMPTY);
		for (int i = 0; i < hosts.length; i++) {
			client.addTransportAddress(new TransportAddress(InetAddress.getByName(hosts[i]), ports[i]));
		}
	}

	// 获取可以操作ES的连接
	public TransportClient getConnection() {
		return client;
	}
}
