package org.bupt.common.util;


/**
 * 按照不同格式输出日志正文
 * Created by ken on 2017/6/9.
 */
public class LogUtil {

	/**
	 * 日志格式: domain={domainValue}: subjectKey={subjectValue} predicate objectKey={objectValue} {adverbialValue}
	 * 示例1: domain=app_manage: user_id=30 add/delete/modify/query app_id=45 SUCCESS
	 * 示例2: domain=nlp_rest: app_id=23 invoke ability=word_segment ERROR
	 * 示例3: domain=nlp_rest: app_id=45 enter/exit ability=word_segment SUCCESS
	 * @param domainValue
	 * @param subjectKey
	 * @param subjectValue
	 * @param predicate
	 * @param objectKey
	 * @param objectValue
	 * @param adverbialValue
	 * @return
	 */
	public static String body(String domainValue,
							  String subjectKey,
							  Object subjectValue,
							  String predicate,
							  String objectKey,
							  Object objectValue,
							  String adverbialValue) {

		StringBuilder sb = new StringBuilder();
		sb.append("domain=");
		sb.append(domainValue);
		sb.append(": ");
		sb.append(subjectKey);
		sb.append("=");
		sb.append(subjectValue);
		sb.append(" ");
		sb.append(predicate);
		sb.append(" ");
		sb.append(objectKey);
		sb.append("=");
		sb.append(objectValue);
		sb.append(" ");
		sb.append(adverbialValue);

		return sb.toString();
	}
}
