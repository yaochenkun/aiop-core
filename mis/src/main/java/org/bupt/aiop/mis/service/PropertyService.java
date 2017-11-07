package org.bupt.aiop.mis.service;

import org.bupt.aiop.mis.constant.CodeConsts;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 读写配置文件的服务
 * Created by ken on 2017/8/18.
 */
@Service
public class PropertyService {

	private static String rootPath = PropertyService.class.getResource("/").getPath();


	/**
	 * 读取一个键值对
	 *
	 * @param filePath
	 * @param key
	 */
	public String readString(String filePath, String key) {

		String targetPath = rootPath + filePath;

		try {

			InputStream inputStream = new FileInputStream(targetPath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			Properties props = new Properties();
			props.load(bufferedReader);

			String value = props.getProperty(key);

			inputStream.close();
			bufferedReader.close();


			return value;
		} catch (IOException e) {

			System.err.println("属性文件读取错误");
			return null;
		}
	}

	/**
	 * 读取一个键值对
	 *
	 * @param filePath
	 * @param key
	 */
	public Integer readInteger(String filePath, String key) {

		String targetPath = rootPath + filePath;

		try {

			InputStream inputStream = new FileInputStream(targetPath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			Properties props = new Properties();
			props.load(bufferedReader);

			inputStream.close();
			bufferedReader.close();

			return Integer.parseInt(props.getProperty(key));
		} catch (IOException e) {

			System.err.println("属性文件读取错误");
			return null;
		}
	}


	/**
	 * 读取一组字符串键值对
	 *
	 * @param filePath
	 * @param keySet
	 * @return
	 */
	public Map<String, String> readStrings(String filePath, Set<String> keySet) {

		String targetPath = rootPath + filePath;

		try {

			InputStream inputStream = new FileInputStream(targetPath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			Properties props = new Properties();
			props.load(bufferedReader);

			Map<String, String> map = new HashMap<>();
			for (String key : keySet)
				map.put(key, props.getProperty(key));

			inputStream.close();
			bufferedReader.close();

			return map;
		} catch (IOException e) {

			System.err.println("属性文件读取错误");
			return null;
		}
	}

	/**
	 * 读取一组整形键值对
	 *
	 * @param filePath
	 * @param keySet
	 * @return
	 */
	public Map<String, Integer> readIntegers(String filePath, Set<String> keySet) {

		String targetPath = rootPath + filePath;

		try {

			InputStream inputStream = new FileInputStream(targetPath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			Properties props = new Properties();
			props.load(bufferedReader);


			Map<String, Integer> map = new HashMap<>();
			for (String key : keySet)
				map.put(key, Integer.parseInt(props.getProperty(key)));

			inputStream.close();
			bufferedReader.close();

			return map;
		} catch (IOException e) {

			System.err.println("属性文件读取错误");
			return null;
		}
	}

	/**
	 * 读取所有的整形键值对
	 *
	 * @param filePath
	 * @return
	 */
	public Map<String, Integer> readIntegers(String filePath) {

		String targetPath = rootPath + filePath;

		try {
			InputStream inputStream = new FileInputStream(targetPath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			Properties props = new Properties();
			props.load(bufferedReader);

			Map<String, Integer> map = new HashMap<>();
			for (Map.Entry entry : props.entrySet())
				map.put(String.valueOf(entry.getKey()), Integer.parseInt(entry.getValue().toString()));

			inputStream.close();
			bufferedReader.close();

			return map;
		} catch (IOException e) {

			System.err.println("属性文件读取错误");
			return null;
		}
	}

	/**
	 * 读取所有的整形键值对
	 *
	 * @param filePath
	 * @return
	 */
	public Map<String, Double> readDoubles(String filePath) {

		String targetPath = rootPath + filePath;

		try {
			InputStream inputStream = new FileInputStream(targetPath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			Properties props = new Properties();
			props.load(bufferedReader);

			Map<String, Double> map = new HashMap<>();
			for (Map.Entry entry : props.entrySet())
				map.put(String.valueOf(entry.getKey()), Double.parseDouble(entry.getValue().toString()));

			inputStream.close();
			bufferedReader.close();

			return map;
		} catch (IOException e) {

			System.err.println("属性文件读取错误");
			return null;
		}
	}

	/**
	 * 更新一个键值对
	 *
	 * @param filePath
	 * @param key
	 * @param value
	 */
	public Integer update(String filePath, String key, String value) {

		String targetPath = rootPath + filePath;

		try {

			InputStream inputStream = new FileInputStream(targetPath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			Properties props = new Properties();
			props.load(bufferedReader);

			OutputStream outputStream = new FileOutputStream(targetPath);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
			props.setProperty(key, value);
			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
			props.store(bufferedWriter, "");


			inputStream.close();
			bufferedReader.close();
			outputStream.close();
			bufferedWriter.close();

			return CodeConsts.CRUD_SUCCESS;

		} catch (IOException e) {

			System.err.println("属性文件更新错误");
			return CodeConsts.CRUD_FAILURE;

		}
	}

	/**
	 * 更新一组键值对
	 *
	 * @param filePath
	 * @param map
	 */
	public Integer update(String filePath, Map<String, Object> map) {

		String targetPath = rootPath + filePath;
		try {

			InputStream inputStream = new FileInputStream(targetPath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			Properties props = new Properties();
			props.load(bufferedReader);

			OutputStream outputStream = new FileOutputStream(targetPath);
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
			for (Map.Entry<String, Object> entry : map.entrySet()) {

				String key = entry.getKey();
				String value = String.valueOf(entry.getValue());

				props.setProperty(key, value);
			}

			props.store(bufferedWriter, ""); // 将此 Properties 表中的属性列表（键和元素对）写入输出流

			inputStream.close();
			bufferedReader.close();
			outputStream.close();
			bufferedWriter.close();


			return CodeConsts.CRUD_SUCCESS;

		} catch (IOException e) {

			System.err.println("属性文件更新错误");
			return CodeConsts.CRUD_FAILURE;
		}
	}
}
