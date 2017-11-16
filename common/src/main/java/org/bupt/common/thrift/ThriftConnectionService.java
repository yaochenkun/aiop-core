package org.bupt.common.thrift;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by ken on 2017/10/24.
 */
public class ThriftConnectionService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private GenericObjectPool<TProtocol> connectionPool;

	public ThriftConnectionService(ThriftConnectionFactory thriftConnectionFactory, GenericObjectPoolConfig genericObjectPoolConfig) {

		connectionPool = new GenericObjectPool<TProtocol>(thriftConnectionFactory, genericObjectPoolConfig);
	}

	public TProtocol getConnection() {

		try {
			return connectionPool.borrowObject();
		} catch (Exception e) {
			throw new RuntimeException("Thrift getConnection出现异常", e);
		}
	}

	public void returnConnection(TProtocol protocol) {

		try {
			connectionPool.returnObject(protocol);
		} catch (Exception e) {
			throw new RuntimeException("Thrift returnConnection出现异常", e);
		}
	}
}
