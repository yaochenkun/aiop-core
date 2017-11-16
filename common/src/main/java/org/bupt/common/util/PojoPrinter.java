package org.bupt.common.util;

import java.lang.reflect.Field;

/**
 * 解析pojo的所有字段
 * 内部不能再有pojo类对象
 */
public class PojoPrinter {
	
	public static String toString(Object bean) {
		Field[] fields = bean.getClass().getDeclaredFields();
		
		StringBuilder res = new StringBuilder("");
		res.append("[ ");
		for(Field field : fields){
			res.append(" ");
			res.append(field.getName());
			res.append(" : ");
			try {
				field.setAccessible(true);
				Object subfield = field.get(bean);
				if(subfield == null)
					res.append("null");
				else
					res.append(subfield.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			res.append(" ,");
		}
		res.append(" ]");
		
		return res.toString();
	}
}
