package org.bupt.common.thrift;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

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

	@Override
	public void destroyObject(PooledObject<TProtocol> pooledObject) throws Exception {
		passivateObject(pooledObject);
		pooledObject.markAbandoned();
	}

	@Override
	public boolean validateObject(PooledObject<TProtocol> pooledObject) {

		TProtocol protocol = pooledObject.getObject();
		if (protocol != null){
			TTransport transport = protocol.getTransport();
			if (transport.isOpen()){
				return true;
			}
			try {
				transport.open();
				return  true;
			} catch (TTransportException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public void activateObject(PooledObject<TProtocol> pooledObject) throws Exception {
		TTransport transport = pooledObject.getObject().getTransport();
		if (!transport.isOpen()){
			transport.open();
		}
	}

	@Override
	public void passivateObject(PooledObject<TProtocol> pooledObject) throws Exception {
		TTransport transport = pooledObject.getObject().getTransport();
		if (transport.isOpen()) {
			transport.flush();
			transport.close();
		}
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
