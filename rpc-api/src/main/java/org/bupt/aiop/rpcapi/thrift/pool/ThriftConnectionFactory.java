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

	private String serverIP;
	private int serverPort;
	private int timeout;


	@Override
	public PooledObject<TProtocol> makeObject() throws Exception {

		TTransport transport = new TSocket(this.serverIP, this.serverPort, this.timeout);
		TProtocol protocol = new TBinaryProtocol(transport);
		protocol.getTransport().open();

		return new DefaultPooledObject<TProtocol>(protocol);
	}

	@Override
	public void destroyObject(PooledObject<TProtocol> pooledObject) throws Exception {

		TProtocol protocol = pooledObject.getObject();
		if(protocol.getTransport().isOpen()) {
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



	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
