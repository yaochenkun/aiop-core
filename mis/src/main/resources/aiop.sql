/*
 Navicat MySQL Data Transfer

 Source Server         : aiop
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : 10.109.246.35
 Source Database       : aiop

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : utf-8

 Date: 01/08/2018 00:04:59 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ability`
-- ----------------------------
DROP TABLE IF EXISTS `ability`;
CREATE TABLE `ability` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zh_name` varchar(255) DEFAULT NULL,
  `en_name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL COMMENT '基础算法/模型算法',
  `model_id` int(11) DEFAULT NULL,
  `invoke_limit` int(11) DEFAULT NULL COMMENT '默认调用量限制',
  `qps_limit` int(11) DEFAULT NULL COMMENT '默认调用量限制',
  `restapi_url` varchar(255) DEFAULT NULL,
  `doc_url` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  `update_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `model_id` (`model_id`),
  CONSTRAINT `ability:model_id` FOREIGN KEY (`model_id`) REFERENCES `model` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `ability`
-- ----------------------------
BEGIN;
INSERT INTO `ability` VALUES ('1', '中文分词', 'word_seg', '基础算法', null, '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/word_seg', '/instruction/word-seg', '分词接口提供基本词和混排两种粒度的分词结果，基本词粒度较小，适用于搜索引擎等需要更多召回的任务，而混排粒度倾向于保留更多的短语。', 'v1', '2017-12-20', '2018-01-06'), ('2', '词性标注', 'word_pos', '基础算法', null, '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/word_pos', '/instruction/word-pos', '为分词结果中的每个单词标注词性，包括名词、动词、形容词或其他词性。', 'v1,v2', '2017-12-20', '2018-01-16'), ('3', '命名实体识别', 'word_ner', '基础算法', null, '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/word_ner', '/instruction/word-ner', '识别文本中具有特定意义的实体，主要包括人名、地名、机构名、专有名词等。', 'v1', '2018-01-04', '2018-01-04'), ('4', '依存句法分析', 'dependency_parse', '基础算法', null, '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/dependency_parse', '/instruction/dependency-parse', '依存句法分析接口可自动分析文本中的依存句法结构信息，利用句子中词与词之间的依存关系来表示词语的句法结构信息（如“主谓”、“动宾”、“定中”等结构关系），并用树状结构来表示整句的结构（如“主谓宾”、“定状补”等）。', 'v1', '2018-01-05', '2018-01-06'), ('5', '关键词提取', 'text_keywords', '基础算法', null, '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/text_keywords', '/instruction/text-keywords', '提取出若干个代表输入文本语义内容的词汇或短语。', 'v1', '2018-01-05', '2018-01-05'), ('6', '摘要提取', 'text_summaries', '基础算法', null, '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/text_summaries', '/instruction/text-summaries', '从原始文档集中抽取一些具有代表性的文本片段构成摘要。', 'v1', '2018-01-05', '2018-01-05'), ('7', '句子短语提取', 'text_phrases', '基础算法', null, '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/text_phrases', '/instruction/text-phrases', '提取句子中的若干关键短语。', 'v1', '2018-01-05', '2018-01-05'), ('8', '词向量生成', 'word_2_vec', '模型算法', '1', '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/word_2_vec', '/instruction/word-2-vec', '查询词汇的词向量，实现文本的可计算。', 'v1', '2017-12-21', '2018-01-09'), ('9', '汉字转拼音', 'word_2_pinyin', '基础算法', null, '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/word_2_pinyin', '/instruction/word-2-pinyin', '将原始汉字文本逐字转换成拼音表示。', 'v1', '2018-01-01', '2018-01-18'), ('10', '简体转繁体', 'simplified_2_traditional', '基础算法', null, '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/simplified_2_traditional', '/instruction/simplified-2-traditional', '将原始简体文本逐字转换成繁体表示。', 'v1', '2017-12-13', '2018-01-18'), ('11', '繁体转简体', 'traditional_2_simplified', '基础算法', null, '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/traditional_2_simplified', '/instruction/traditional-2-simplified', '将原始繁体文本逐字转换成简体表示。', 'v1', '2017-12-09', '2018-01-11'), ('12', '短文本相似度计算', 'word_sim', '模型算法', '1', '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/word_sim', '/instruction/word-sim', '判断两个短文本的相似度得分。', 'v1', '2018-01-09', '2018-01-17'), ('13', '文档相似度计算', 'document_sim', '模型算法', '1', '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/document_sim\n', '/instruction/document-sim', '判断两个文档的相似度得分。', 'v1', '2017-12-22', '2018-01-18'), ('14', '最相似短文本', 'nearest_words', '模型算法', '1', '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/nearest_words', '/instruction/nearest-words', '选出词库中与输入词汇最相似的若干个词汇，按照相似度得分从高至低排列。', 'v1', '2017-12-14', '2018-01-11'), ('15', '情感倾向分析', 'motion_classify', '模型算法', '2', '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/motion_classify', '/instruction/motion-classify', '对包含主观观点信息的文本进行情感极性类别（积极、消极、中性）的判断，并给出相应的置信度。', 'v1', '2017-11-16', '2018-01-06'), ('16', '文本分类', 'category_classify', '模型算法', '3', '10000', '50', 'http://aiop.bupt.com/restapi/nlp/v1/category_classify', '/instruction/category-classify', '将输入文本按照不同领域进行归类（包括：科技、人文、娱乐、历史等类别）。', 'v1', '2017-12-11', '2018-01-06');
COMMIT;

-- ----------------------------
--  Table structure for `ability_invoke_log`
-- ----------------------------
DROP TABLE IF EXISTS `ability_invoke_log`;
CREATE TABLE `ability_invoke_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `ability_id` int(11) DEFAULT NULL,
  `invoke_result` varchar(255) DEFAULT NULL COMMENT '调用成功/失败',
  `invoke_time` datetime DEFAULT NULL COMMENT '调用时间',
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `ability_id` (`ability_id`),
  CONSTRAINT `ability_invoke_log:ability_id` FOREIGN KEY (`ability_id`) REFERENCES `ability` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ability_invoke_log:app_id` FOREIGN KEY (`app_id`) REFERENCES `app` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `app`
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `developer_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '应用名称',
  `type` varchar(255) DEFAULT NULL COMMENT '应用类型',
  `platform` varchar(255) DEFAULT NULL COMMENT '应用平台',
  `client_id` varchar(255) DEFAULT NULL,
  `client_secret` varchar(255) DEFAULT NULL,
  `ability_scope` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL COMMENT '状态（已上线、运行中、关闭、异常）',
  `logo_file` varchar(255) DEFAULT NULL COMMENT '应用图标',
  `create_date` date DEFAULT NULL,
  `update_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `developer_id` (`developer_id`),
  CONSTRAINT `app:developer_id` FOREIGN KEY (`developer_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `app`
-- ----------------------------
BEGIN;
INSERT INTO `app` VALUES ('1', '3', '识字软件', '学习办公', 'iOS', '03c7f07d6295ca261e51495c', 'c5c909ab712d4c509965684b3c61b56e', 'word_ner,dependency_parse,text_keywords,text_summaries,text_phrases,word_2_vec,word_2_pinyin,simplified_2_traditional,traditional_2_simplified,word_sim,document_sim,nearest_words,motion_classify,category_classify', '一款帮助学龄前儿童识字的移动应用', '运行中', 'default.png', '2018-01-07', '2018-01-07'), ('2', '3', '物流配送路径规划系统', '交通出行', 'Windows', 'b6cc3a69f87c0cae2a245818', '550fe54f7567408c99167dc9463ed36b', 'word_seg,word_pos,word_ner,dependency_parse,text_keywords,text_summaries,text_phrases,word_2_pinyin,simplified_2_traditional,traditional_2_simplified', '为货车司机规划最优行径路线的智能出行软件', '关闭', 'default.png', '2018-01-07', '2018-01-07'), ('3', '3', '心情笔记APP', '其它', 'Android', '6fb3d4d40a342c2bbd8e761a', 'cd592c53d42a4b01bc57dd203df5cce9', 'word_seg,word_pos,word_ner,dependency_parse,text_keywords,text_summaries,text_phrases,word_2_vec,word_2_pinyin,simplified_2_traditional,traditional_2_simplified,word_sim,document_sim,nearest_words,motion_classify,category_classify', '帮助抑郁症患者检测心情波动、稳定状态的软件', '关闭', 'default.png', '2018-01-07', '2018-01-07'), ('4', '3', '高校推荐软件', '工具应用', 'Android', '8444fe443df231617b7c509a', 'e6cbcd6263ed48a0989d57c295bbe622', 'word_seg,text_keywords,text_summaries,text_phrases,word_2_vec,simplified_2_traditional,document_sim', '基于Android平台的高校评分、智能推荐、横向比较软件。', '运行中', 'default.png', '2018-01-07', '2018-01-07'), ('5', '3', '意绘', '学习办公', 'Windows', '6e72a4805faae6b3b93cf890', '26417c0480cd4402b6521a35ba68b658', 'word_seg,word_pos,dependency_parse,text_summaries,word_2_vec,simplified_2_traditional,document_sim,motion_classify,category_classify', '便于单手持握使用的智能绘图软件，可以方便手功能有障碍的残障人士使用。', '关闭', 'default.png', '2018-01-07', '2018-01-07'), ('6', '3', '网上书店', '其它', 'Web', 'c8603b62e76652ded4cdfad7', '6f407d6b92e14f788f52ce34a75cb8a5', 'word_seg,dependency_parse,text_phrases,word_2_vec,traditional_2_simplified,word_sim,document_sim,nearest_words,motion_classify,category_classify', '一个方便卖家和买家交易书籍的在线网上书城。', '关闭', 'default.png', '2018-01-07', '2018-01-07'), ('7', '3', '塔防游戏', '游戏娱乐', 'iOS', '4814182fa333c456a4d501ad', 'a4735d5d60e545dfb652a35b38245ddb', 'word_seg,word_pos,word_ner,word_2_vec,word_sim,document_sim', '类似于保卫萝卜一样的塔防游戏。', '已上线', 'default.png', '2018-01-07', '2018-01-07');
COMMIT;

-- ----------------------------
--  Table structure for `app_ability`
-- ----------------------------
DROP TABLE IF EXISTS `app_ability`;
CREATE TABLE `app_ability` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `ability_id` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL COMMENT '该app下的该ability状态(开放/关闭)',
  `invoke_limit` int(11) DEFAULT NULL COMMENT '调用量限制（100/天）',
  `qps_limit` int(11) DEFAULT NULL COMMENT 'QPS限制',
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `ability_id` (`ability_id`),
  CONSTRAINT `app_ability:ability_id` FOREIGN KEY (`ability_id`) REFERENCES `ability` (`id`) ON DELETE CASCADE,
  CONSTRAINT `app_ability:app_id` FOREIGN KEY (`app_id`) REFERENCES `app` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `app_ability`
-- ----------------------------
BEGIN;
INSERT INTO `app_ability` VALUES ('1', '1', '3', '允许调用', '10000', '50'), ('6', '1', '8', '允许调用', '10000', '50'), ('8', '1', '10', '允许调用', '10000', '50'), ('9', '1', '11', '允许调用', '10000', '50'), ('11', '1', '13', '允许调用', '10000', '50'), ('13', '1', '15', '允许调用', '10000', '50'), ('15', '2', '1', '允许调用', '10000', '50'), ('16', '2', '2', '允许调用', '10000', '50'), ('17', '2', '3', '允许调用', '10000', '50'), ('18', '2', '4', '允许调用', '10000', '50'), ('19', '2', '5', '允许调用', '10000', '50'), ('20', '2', '6', '允许调用', '10000', '50'), ('21', '2', '7', '允许调用', '10000', '50'), ('22', '2', '9', '允许调用', '10000', '50'), ('23', '2', '10', '允许调用', '10000', '50'), ('24', '2', '11', '允许调用', '10000', '50'), ('25', '3', '1', '允许调用', '10000', '50'), ('26', '3', '2', '允许调用', '10000', '50'), ('27', '3', '3', '允许调用', '10000', '50'), ('28', '3', '4', '允许调用', '10000', '50'), ('29', '3', '5', '允许调用', '10000', '50'), ('30', '3', '6', '允许调用', '10000', '50'), ('31', '3', '7', '允许调用', '10000', '50'), ('32', '3', '8', '允许调用', '10000', '50'), ('33', '3', '9', '允许调用', '10000', '50'), ('34', '3', '10', '允许调用', '10000', '50'), ('35', '3', '11', '允许调用', '10000', '50'), ('36', '3', '12', '允许调用', '10000', '50'), ('37', '3', '13', '允许调用', '10000', '50'), ('38', '3', '14', '允许调用', '10000', '50'), ('39', '3', '15', '允许调用', '10000', '50'), ('40', '3', '16', '允许调用', '10000', '50'), ('41', '4', '1', '允许调用', '10000', '50'), ('42', '4', '5', '允许调用', '10000', '50'), ('43', '4', '6', '允许调用', '10000', '50'), ('44', '4', '7', '允许调用', '10000', '50'), ('45', '4', '8', '允许调用', '10000', '50'), ('46', '4', '10', '允许调用', '10000', '50'), ('47', '4', '13', '允许调用', '10000', '50'), ('48', '5', '1', '允许调用', '10000', '50'), ('49', '5', '2', '允许调用', '10000', '50'), ('50', '5', '4', '允许调用', '10000', '50'), ('51', '5', '6', '允许调用', '10000', '50'), ('52', '5', '8', '允许调用', '10000', '50'), ('53', '5', '10', '允许调用', '10000', '50'), ('54', '5', '13', '允许调用', '10000', '50'), ('55', '5', '15', '允许调用', '10000', '50'), ('56', '5', '16', '允许调用', '10000', '50'), ('57', '6', '1', '允许调用', '10000', '50'), ('58', '6', '4', '允许调用', '10000', '50'), ('59', '6', '7', '允许调用', '10000', '50'), ('60', '6', '8', '允许调用', '10000', '50'), ('61', '6', '11', '允许调用', '10000', '50'), ('62', '6', '12', '允许调用', '10000', '50'), ('63', '6', '13', '允许调用', '10000', '50'), ('64', '6', '14', '允许调用', '10000', '50'), ('65', '6', '15', '允许调用', '10000', '50'), ('66', '6', '16', '允许调用', '10000', '50'), ('67', '7', '1', '允许调用', '10000', '50'), ('68', '7', '2', '允许调用', '10000', '50'), ('69', '7', '3', '允许调用', '10000', '50'), ('70', '7', '8', '允许调用', '10000', '50'), ('71', '7', '12', '允许调用', '10000', '50'), ('72', '7', '13', '允许调用', '10000', '50'), ('75', '1', '2', '允许调用', '10000', '50'), ('78', '1', '1', '允许调用', '10000', '50'), ('79', '1', '12', '允许调用', '10000', '50');
COMMIT;

-- ----------------------------
--  Table structure for `model`
-- ----------------------------
DROP TABLE IF EXISTS `model`;
CREATE TABLE `model` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `file` varchar(255) DEFAULT NULL,
  `size` float DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  `update_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `model`
-- ----------------------------
BEGIN;
INSERT INTO `model` VALUES ('1', '词向量模型', 'vec.model', '2.34838', '2018-01-05', '2018-01-07'), ('2', '情感倾向分析模型', 'motion.model', '0.674805', '2018-01-05', '2018-01-05'), ('3', '文本分类模型', 'category.model', '0.000657082', '2018-01-05', '2018-01-07');
COMMIT;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(255) DEFAULT NULL COMMENT '手机',
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `avatar_file` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('1', 'super_admin', 'ken19931108@163.com', '18899898988', '202cb962ac59075b964b07152d234b70', '超级管理员', 'default.png'), ('2', 'bussiness_admin', 'ken19931108@sina.com', '13980823993', '202cb962ac59075b964b07152d234b70', '业务管理员', 'default.png'), ('3', 'ken19931108', '690559724@qq.com', '18801176260', '202cb962ac59075b964b07152d234b70', '开发者', 'default.png'), ('4', 'log_admin', 'yaochenkun@sina.com', '15122707970', '202cb962ac59075b964b07152d234b70', '日志管理员', 'default.png'), ('5', 'yaochenkun', '514451926@qq.com', '18801176261', '202cb962ac59075b964b07152d234b70', '开发者', 'default.png'), ('6', 'yck19931108', '123123123@qq.com', '18788888888', '202cb962ac59075b964b07152d234b70', '开发者', 'default.png'), ('7', 'yaochenkun99', '9082309812@163.com', '18889999999', '202cb962ac59075b964b07152d234b70', '开发者', 'default.png'), ('8', 'hahahahahha', '123456@qq.com', '18899899999', '202cb962ac59075b964b07152d234b70', '开发者', 'default.png'), ('9', 'yycckk99', '69055224@qq.com', '18801778877', '202cb962ac59075b964b07152d234b70', '开发者', 'default.png');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
