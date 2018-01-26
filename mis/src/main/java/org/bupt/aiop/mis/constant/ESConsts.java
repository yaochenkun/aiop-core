package org.bupt.aiop.mis.constant;

/**
 * ElasticSearch配置
 */
public class ESConsts {

    /**
     * 索引
     */

    // 技术文档
    public static final String INDEX_INSTRUCTION_COMMON = "instruction_common";
    public static final String INDEX_INSTRUCTION_NLP = "instruction_nlp";
    public static final String INDEX_INSTRUCTION_IMAGE = "instruction_image";
    public static final String INDEX_INSTRUCTION_SPEECH = "instruction_speech";
    public static final String INDEX_INSTRUCTION_VIDEO = "instruction_video";

    public static final String FIELD_INSTRUCTION_TITLE = "title"; // 标题
    public static final String FIELD_INSTRUCTION_LINK = "link"; // 链接(暂未参与检索项)
    public static final String FIELD_INSTRUCTION_CONTENT = "content"; // 内容


    /**
     * 配置项
     */

    // 高亮
    public static final String HIGHLIGHT_PRE_TAGS = "<font color='#dd4b39'>"; // 前缀
    public static final String HIGHLIGHT_POST_TAGS = "</font>"; // 后缀
}
