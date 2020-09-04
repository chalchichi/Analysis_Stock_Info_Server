package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearch_High_Level_Cilent {
	RestHighLevelClient client;
	public ElasticSearch_High_Level_Cilent()
	{
		client = new RestHighLevelClient(
		        RestClient.builder(
		                new HttpHost("localhost", 9200, "http"),
		                new HttpHost("localhost", 9201, "http")));
	}
	public List<Map<String, Object>> search()
	{
		//Create Search Request
		MultiSearchRequest requests = new MultiSearchRequest();
		SearchRequest searchRequest = new SearchRequest("stockinfo2"); 
		SearchRequest searchRequest2 = new SearchRequest("stockinfo2"); 
		//Using Search Source Builder
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(QueryBuilders.termQuery("Price", "236000"));
		query.must(QueryBuilders.termQuery("Name", "SK"));
		sourceBuilder.query(query);
		sourceBuilder.from(0); 
		sourceBuilder.size(10); 
		
		SearchSourceBuilder sourceBuilder2 = new SearchSourceBuilder(); 
		sourceBuilder2.query(QueryBuilders.termQuery("Code", "005930")); 
		sourceBuilder2.from(0); 
		sourceBuilder2.size(5); 
		//Add Builder to Search Request
		searchRequest.source(sourceBuilder);
		searchRequest2.source(sourceBuilder2);
		requests.add(searchRequest);
		requests.add(searchRequest2);
		
		//Execution(Sync)
		try {
			MultiSearchResponse searchResponse = client.msearch(requests, RequestOptions.DEFAULT);
			List <Map<String, Object>> arrList = new ArrayList<>();
			for(Item i:searchResponse.getResponses())
			{
				
				for(SearchHit s:i.getResponse().getHits().getHits())
				  {
					  Map<String, Object>
					  sourceMap = s.getSourceAsMap();
					  arrList.add(sourceMap);
				  }
			}
			
			return arrList;
		} catch (IOException e) {
			System.err.println("Elastic search fail");
		}
		return null;
		
	}
	
	public List<Map<String, Object>> search_basic(String code)
	{
		//make Result Set
		List <Map<String, Object>> arrList = new ArrayList<>();
		
		//Create Search Request
		SearchRequest searchRequest = new SearchRequest("stockinfo2"); 
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.termQuery("Code", code));
		sourceBuilder.sort("Time");
		sourceBuilder.from(0); 
		sourceBuilder.size(10000); 
		//Add Builder to Search Request
		searchRequest.source(sourceBuilder);
		
		//Execution(Sync)
		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			
			for(SearchHit s:searchResponse.getHits().getHits())
			  {
				  Map<String, Object>
				  sourceMap = s.getSourceAsMap();
				  arrList.add(sourceMap);
			  }
			return arrList;
		} catch (IOException e) {
			System.err.println("Elastic search fail");
		}
		return null;
	}
	
	public List<Map<String, Object>> search_multi()
	{
		//Create Search Request
		MultiSearchRequest requests = new MultiSearchRequest();
		SearchRequest searchRequest = new SearchRequest("stockinfo2"); 
		SearchRequest searchRequest2 = new SearchRequest("stockinfo2"); 
		//Using Search Source Builder
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.termQuery("Price", "236000")); 
		sourceBuilder.from(0); 
		sourceBuilder.size(5); 
		
		SearchSourceBuilder sourceBuilder2 = new SearchSourceBuilder(); 
		sourceBuilder2.query(QueryBuilders.termQuery("Price", "54400")); 
		sourceBuilder2.from(0); 
		sourceBuilder2.size(5); 
		//Add Builder to Search Request
		searchRequest.source(sourceBuilder);
		searchRequest2.source(sourceBuilder2);
		requests.add(searchRequest);
		requests.add(searchRequest2);
		
		//Execution(Sync)
		try {
			MultiSearchResponse searchResponse = client.msearch(requests, RequestOptions.DEFAULT);
			List <Map<String, Object>> arrList = new ArrayList<>();
			for(Item i:searchResponse.getResponses())
			{
				
				for(SearchHit s:i.getResponse().getHits().getHits())
				  {
					  Map<String, Object>
					  sourceMap = s.getSourceAsMap();
					  arrList.add(sourceMap);
				  }
			}
			
			return arrList;
		} catch (IOException e) {
			System.err.println("Elastic search fail");
		}
		return null;
	}
	
	public List<Map<String, Object>> search_bool()
	{
		//Create Search Request
		SearchRequest searchRequest = new SearchRequest("stockinfo2"); 
		
		//Using Search Source Builder
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(QueryBuilders.termQuery("Price", "236000"));
		query.must(QueryBuilders.termQuery("Name", "SK"));
		sourceBuilder.query(query);
		sourceBuilder.from(0); 
		sourceBuilder.size(10); 
		
		//Add Builder to Search Request
		searchRequest.source(sourceBuilder);
		
		//Execution(Sync)
		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			List <Map<String, Object>> arrList = new ArrayList<>();
			for(SearchHit s:searchResponse.getHits().getHits())
			  {
				  Map<String, Object> 
				  sourceMap = s.getSourceAsMap();
				  arrList.add(sourceMap);
			  }
			return arrList;
		} catch (IOException e) {
			System.err.println("Elastic search fail");
		}
		return null;
	}
}
