package org.bupt.common.util;


/**
 * 按照不同格式输出日志正文
 * Created by ken on 2017/6/9.
 */
public class LogUtil {

	/**
	 * 日志格式: domain={domainValue}: subjectKey={subjectValue} verb objectKey={objectValue}
	 * 示例: domain=NLP_REST: user_id=30 enter ability=word_segment
	 * @param domainValue 业务领域名称
	 * @param subjectKey 主语名称
	 * @param subjectValue 主语值
	 * @param verb 谓语动词
	 * @param objectKey 宾语名称
	 * @param objectValue 宾语值
	 * @return
	 */
	public static String body(String domainValue,
							  String subjectKey,
							  Object subjectValue,
							  String verb,
							  String objectKey,
							  Object objectValue) {

		StringBuilder sb = new StringBuilder();
		sb.append("domain=");
		sb.append(domainValue);
		sb.append(": ");
		sb.append(subjectKey);
		sb.append("=");
		sb.append(subjectValue);
		sb.append(" ");
		sb.append(verb);
		sb.append(" ");
		sb.append(objectKey);
		sb.append("=");
		sb.append(objectValue);

		return sb.toString();
	}


	/**
	 * 日志格式: domain={domainValue}: subjectKey={subjectValue} verb objectKey={objectValue}, result={resultValue}
	 * 示例: domain=APP_MANAGE: user_id=30 SAVE/REMOVE/MODIFY/GET app_id=45, result=SUCCESS
	 * @param domainValue 业务领域名称
	 * @param subjectKey 主语名称
	 * @param subjectValue 主语值
	 * @param verb 谓语动词
	 * @param objectKey 宾语名称
	 * @param objectValue 宾语值
	 * @param resultValue 结果
	 * @return
	 */
	public static String body(String domainValue,
							  String subjectKey,
							  Object subjectValue,
							  String verb,
							  String objectKey,
							  Object objectValue,
							  String resultValue) {

		StringBuilder sb = new StringBuilder();
		sb.append("domain=");
		sb.append(domainValue);
		sb.append(": ");
		sb.append(subjectKey);
		sb.append("=");
		sb.append(subjectValue);
		sb.append(" ");
		sb.append(verb);
		sb.append(" ");
		sb.append(objectKey);
		sb.append("=");
		sb.append(objectValue);
		sb.append(", result=");
		sb.append(resultValue);

		return sb.toString();
	}

	/**
	 * 日志格式: domain={domainValue}: subjectKey={subjectValue} verb objectKey={objectValue}, result={resultValue}, reason={reasonValue}
	 * 示例: domain=NLP_REST: app_id=23 INVOKE ability=word_segment, result=ERROR, reason=invalid input params
	 * @param domainValue 业务领域名称
	 * @param subjectKey 主语名称
	 * @param subjectValue 主语值
	 * @param verb 谓语动词
	 * @param objectKey 宾语名称
	 * @param objectValue 宾语值
	 * @param resultValue 结果
	 * @param reasonValue 原因
	 * @return
	 */
	public static String body(String domainValue,
							  String subjectKey,
							  Object subjectValue,
							  String verb,
							  String objectKey,
							  Object objectValue,
							  String resultValue,
							  String reasonValue) {

		StringBuilder sb = new StringBuilder();
		sb.append("domain=");
		sb.append(domainValue);
		sb.append(": ");
		sb.append(subjectKey);
		sb.append("=");
		sb.append(subjectValue);
		sb.append(" ");
		sb.append(verb);
		sb.append(" ");
		sb.append(objectKey);
		sb.append("=");
		sb.append(objectValue);
		sb.append(", result=");
		sb.append(resultValue);
		sb.append(", reason=");
		sb.append(reasonValue);

		return sb.toString();
	}
}
