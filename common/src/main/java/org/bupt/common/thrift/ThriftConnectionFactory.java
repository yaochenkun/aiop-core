package org.bupt.common.thrift;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Thrift RPC连接工厂
 * Created by ken on 2017/10/24.
 */
public class ThriftConnectionFactory implements PooledObjectFactory<TProtocol> {

	private String proxyIP;
	private int proxyPort;


	public PooledObject<TProtocol> makeObject() throws Exception {

		TTransport transport = new TSocket(this.proxyIP, this.proxyPort);
		TProtocol protocol = new TBinaryProtocol(transport);
		protocol.getTransport().open();

		return new DefaultPooledObject<TProtocol>(protocol);
	}

	public void destroyObject(PooledObject<TProtocol> pooledObject) throws Exception {

		TProtocol protocol = pooledObject.getObject();
		if (protocol.getTransport().isOpen()) {
			protocol.getTransport().close();
		}
	}

	public boolean validateObject(PooledObject<TProtocol> pooledObject) {
		return pooledObject.getObject().getTransport().isOpen();
	}

	public void activateObject(PooledObject<TProtocol> pooledObject) throws Exception {

	}

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
}
