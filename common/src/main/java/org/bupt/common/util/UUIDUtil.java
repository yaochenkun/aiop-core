package org.bupt.common.util;

import java.util.UUID;

/**
 * 生成UUID工具类
 */
public class UUIDUtil {

	/**
	 * 获得4个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String generate4UUID(){
		UUID id=UUID.randomUUID();
		String[] idd=id.toString().split("-");
		return idd[1];
	}
	/**
	 * 获得8个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String generate8UUID(){
		UUID id=UUID.randomUUID();
		String[] idd=id.toString().split("-");
		return idd[0];
	}
	/**
	 * 获得12个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String generate12UUID(){
		UUID id=UUID.randomUUID();
		String[] idd=id.toString().split("-");
		return idd[0]+idd[1];
	}
	/**
	 * 获得16个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String generate16UUID(){

		UUID id=UUID.randomUUID();
		String[] idd=id.toString().split("-");
		return idd[0]+idd[1]+idd[2];
	}
	/**
	 * 获得20个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String generate20UUID(){

		UUID id=UUID.randomUUID();
		String[] idd=id.toString().split("-");
		return idd[0]+idd[1]+idd[2]+idd[3];
	}
	/**
	 * 获得24个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String generate24UUID(){
		UUID id=UUID.randomUUID();
		String[] idd=id.toString().split("-");
		return idd[0]+idd[1]+idd[4];
	}
	/**
	 * 获得32个长度的十六进制的UUID
	 * @return UUID
	 */
	public static String generate32UUID(){
		UUID id=UUID.randomUUID();
		String[] idd=id.toString().split("-");
		return idd[0]+idd[1]+idd[2]+idd[3]+idd[4];
	}

}
