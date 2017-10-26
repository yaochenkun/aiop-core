package org.bupt.aiop.rpcapi.thrift.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.stereotype.Component;

/**
 * Thrift RPC连接工厂
 * Created by ken on 2017/10/24.
 */
@Component
public class ThriftConnectionFactory implements PooledObjectFactory<TProtocol> {

	private String proxyIP;
	private int proxyPort;
	private int proxyTimeout;


	@Override
	public PooledObject<TProtocol> makeObject() throws Exception {

		TTransport transport = new TSocket(this.proxyIP, this.proxyPort, this.proxyTimeout);
		TProtocol protocol = new TBinaryProtocol(transport);
		protocol.getTransport().open();

		return new DefaultPooledObject<TProtocol>(protocol);
	}

	@Override
	public void destroyObject(PooledObject<TProtocol> pooledObject) throws Exception {

		TProtocol protocol = pooledObject.getObject();
		if (protocol.getTransport().isOpen()) {
			protocol.getTransport().close();
		}
	}

	@Override
	public boolean validateObject(PooledObject<TProtocol> pooledObject) {
		return pooledObject.getObject().getTransport().isOpen();
	}

	@Override
	public void activateObject(PooledObject<TProtocol> pooledObject) throws Exception {

	}

	@Override
	public void passivateObject(PooledObject<TProtocol> pooledObject) throws Exception {

	}


	public String getProxyIP() {
		return proxyIP;
	}

	public void setProxyIP(String proxyIP) {
		this.proxyIP = proxyIP;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public int getProxyTimeout() {
		return proxyTimeout;
	}

	public void setProxyTimeout(int proxyTimeout) {
		this.proxyTimeout = proxyTimeout;
	}
}
