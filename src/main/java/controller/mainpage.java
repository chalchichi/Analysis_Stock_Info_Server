package controller;
import stockdao.pricedao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.analysis.Analysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;

import service.AnalyzeStock;
import service.Crawling_Send;
import service.ElasticSearch_High_Level_Cilent;
import service.KConsumer;
import service.SendByCSV;

@RestController
@EnableScheduling
public class mainpage {
	@Autowired
	pricedao pricedao;
	
	@Autowired
	KConsumer con;
	
	@Autowired
	ElasticSearch_High_Level_Cilent elastic;
	
	@Autowired
	SendByCSV csv;
	
	@Autowired
	AnalyzeStock al;
	
	@Autowired
	Crawling_Send cr;
	
	@GetMapping(path = "/helloWorld")
	public void helloWorld(){
		pricedao.SendbyCode();
	}
	@GetMapping(path = "/con")
	public void con(){
		con.Consume();
	}
	
	@GetMapping(path = "/ela/{code}")
	public List<Map<String, Object>> ela(@PathVariable("code") String code )
	{
		return elastic.search_basic(code);
	}
	
	@GetMapping(path = "/al/{code}")
	public List<List<String>> al(@PathVariable("code") String code )
	{
		return al.Getresult(code);
	}

	@GetMapping(path = "/cr")
	public HashMap<String, String> cr()
	{
		return cr.getdata("011160");
	}
	
	@GetMapping(path="/galaxy")
	public String galaxy()
	{
		return "wow galaxy";
	}
}

