package org.bupt.aiop.mis.controller;

import org.bupt.aiop.mis.constant.ESConsts;
import org.bupt.common.bean.PageResult;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.es.EsConnectionService;
import org.bupt.common.util.ESUtil;
import org.bupt.common.util.Validator;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * 全文检索控制器
 */
@RestController
@RequestMapping("api/search")
public class SearchController {

	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private EsConnectionService esConnectionService;

	/**
	 * 检索所有索引
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public ResponseResult listAll(@RequestBody Map<String, Object> params) {

		// 获取输入参数
		Integer pageNow = (Integer) params.get("pageNow");
		Integer pageSize = (Integer) params.get("pageSize");
		String keyword = (String) params.get("keyword");

		// 连接ES进行检索
		SearchResponse response;
		try {
			response = esConnectionService.getConnection()
					.prepareSearch(ESConsts.INDEX_INSTRUCTION_COMMON, ESConsts.INDEX_INSTRUCTION_NLP)
					.setQuery(Validator.checkEmpty(keyword) ? QueryBuilders.matchAllQuery() : QueryBuilders.multiMatchQuery(keyword, ESConsts.FIELD_INSTRUCTION_TITLE, ESConsts.FIELD_INSTRUCTION_CONTENT))
					.setFrom((pageNow - 1) * pageSize)
					.setSize(pageSize)
					.highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags(ESConsts.HIGHLIGHT_PRE_TAGS).postTags(ESConsts.HIGHLIGHT_POST_TAGS))
					.get();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("检索所有索引列表失败，关键字={}", keyword);
			return ResponseResult.error("检索失败");
		}

		// 包装成含有分页信息的数据
		PageResult pageResult = new PageResult(ESUtil.unionWithHighlights(response.getHits()), response.getHits().getTotalHits());

		logger.debug("检索所有索引列表成功，关键字={}", keyword);
		return ResponseResult.success("检索成功", pageResult);
	}
}
