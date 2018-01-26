package org.bupt.aiop.mis;

import org.bupt.common.bean.PageResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {
	public static void main(String[] args) throws UnknownHostException {
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("10.109.246.35"), 9300));

		SearchResponse response = client.prepareSearch("instruction_common", "instruction_nlp")
				.setQuery(QueryBuilders.multiMatchQuery("自然语言处理", "title", "content"))
				.setFrom(1)
				.setSize(10)
				.highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<font color='#dd4b39'>").postTags("</font>"))
				.get();

//		PageResult pageResult = new PageResult(, response.getHits().getTotalHits());

//		response.getHits().getHits()

//		System.out.println(response.getHits().getAt(1).getSourceAsMap().toString());
		System.out.println(response.toString());

		client.close();
	}
}
