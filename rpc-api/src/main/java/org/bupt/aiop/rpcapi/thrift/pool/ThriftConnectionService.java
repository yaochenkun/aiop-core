package org.bupt.aiop.rpcapi.thrift.pool;

import org.apache.thrift.protocol.TProtocol;

/**
 * Created by ken on 2017/10/24.
 */
public interface ThriftConnectionService {

	/**
	 * 从Thrift连接池获取一个连接
	 * @return
	 */
	TProtocol getConnection();

	/**
	 * 将protocol连接放回连接池
	 * @param protocol
	 */
	void returnConnection(TProtocol protocol);
}
