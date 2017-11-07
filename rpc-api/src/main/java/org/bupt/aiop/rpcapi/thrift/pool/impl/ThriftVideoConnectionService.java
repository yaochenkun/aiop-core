package org.bupt.aiop.rpcapi.thrift.pool.impl;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.protocol.TProtocol;
import org.bupt.aiop.rpcapi.thrift.pool.ThriftConnectionFactory;
import org.bupt.aiop.rpcapi.thrift.pool.ThriftConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by ken on 2017/10/24.
 */
@Service(value = "thriftVideoConnectionService")
public class ThriftVideoConnectionService implements ThriftConnectionService {


	private GenericObjectPool<TProtocol> connectionPool;

	@Autowired
	public ThriftVideoConnectionService(ThriftConnectionFactory thriftVideoConnectionFactory, GenericObjectPoolConfig genericVideoObjectPoolConfig) {

		connectionPool = new GenericObjectPool<TProtocol>(thriftVideoConnectionFactory, genericVideoObjectPoolConfig);

	}


	@Override
	public TProtocol getConnection() {
		try {
			return connectionPool.borrowObject();
		} catch (Exception e) {
			throw new RuntimeException("Video: Thrift getConnection出现异常", e);
		}
	}

	@Override
	public void returnConnection(TProtocol protocol) {

		try {
			connectionPool.returnObject(protocol);
		} catch (Exception e) {
			throw new RuntimeException("Video: Thrift returnConnection出现异常", e);
		}
	}
}