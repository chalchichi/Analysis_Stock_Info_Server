package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import service.AnalyzeStock;
import service.Crawling_Send;
import service.ElasticSearch_High_Level_Cilent;

@Controller
public class ViewController {
	@Autowired
	AnalyzeStock al;
	@Autowired
	Crawling_Send cr;
	@Autowired
	ElasticSearch_High_Level_Cilent elastic;
	
	@RequestMapping(value = "/analyze", method = RequestMethod.GET)
	public String analyze(@RequestParam("code") String code,Model modelMap)
	{
		List<List<String>> list= al.Getresult(code);
		String mainurl ="https://ssl.pstatic.net/imgfinance/chart/item/area/year/"+code+".png?sidcode=1598693784176";
		modelMap.addAttribute("mainurl", mainurl);
		modelMap.addAttribute("name"+Integer.toString(0),list.get(0).get(1));
		String[] index = {"종목명","종목코드","코스피 현재가","전일대비","전일가","시가 ","고가 ","상한가 ","저가 ","하한가","거래량 "};
		ArrayList<String> na = new ArrayList<>();
		for(int i=0;i<index.length;i++)
		{
			na.add("data"+Integer.toString(i));
		}
		HashMap<String, String> maindata=cr.getdata(code);
		for(int j=0;j<index.length;j++)
		{
			String key=index[j];
			modelMap.addAttribute(na.get(j)+Integer.toString(0),key+" : " + maindata.get(key));
		}
		for(int i=1;i<4;i++)
		{
			String url="https://ssl.pstatic.net/imgfinance/chart/item/area/year/"+list.get(i).get(0)+".png?sidcode=1598693784176";
			HashMap<String, String> data=cr.getdata(list.get(i).get(0));
			modelMap.addAttribute("url"+Integer.toString(i), url);
			modelMap.addAttribute("name"+Integer.toString(i),list.get(i).get(1));
			for(int j=0;j<index.length;j++)
			{
				String key=index[j];
				modelMap.addAttribute(na.get(j)+Integer.toString(i),key+" : " + data.get(key));
			}
		}
		return "hello";
	}
	@RequestMapping("/index")
	public String index()
	{
		return "index";
	}
	
	@RequestMapping("buttons")
	public String buttons()
	{
		return "buttons";
	}
	
	@RequestMapping("/cards")
	public String cards()
	{
		return "cards";
	}
	
	@RequestMapping(value = "/charts" , method = RequestMethod.GET)
	public String charts(@RequestParam(value = "name",defaultValue="삼성전자") String name, Model model) {
		String code = null;
		List<Map<String, Object>> temp = elastic.search_code(name);
		code = (String) temp.get(0).get("Code");
		List<List<String>> list= al.Getresult(code);
		for(int i=0;i<4;i++)
		{
			List<Map<String, Object>> arr = elastic.search_basic(list.get(i).get(0));
			ArrayList<Integer> mainprice = new ArrayList<>();
			ArrayList<String> maindate = new ArrayList<>();
			for(Map<String, Object> m:arr)
			{
				if(m.get("Price").toString().equals("null"))
				{
				}
				else {
					mainprice.add(Integer.parseInt(m.get("Price").toString()));
					maindate.add(m.get("Time").toString().substring(0,10));
				}
				
			}
	  		model.addAttribute("data"+Integer.toString(i), mainprice.toArray());
	  		model.addAttribute("date"+Integer.toString(i), maindate.toArray());
	  		model.addAttribute("name"+Integer.toString(i), list.get(i).get(1));
		}
		return "charts";
	}
	
	@RequestMapping("/blank")
	public String blank()
	{
		return "blank";
	}
	
	@RequestMapping("/forgot-password")
	public String forgotpassword()
	{
		return "forgot-password";
	}
	
	@RequestMapping("/login")
	public String login()
	{
		return "login";
	}
	@RequestMapping("/404")
	public String error404()
	{
		return "404";
	}
	@RequestMapping("/register")
	public String register()
	{
		return "register";
	}
	@RequestMapping("/tables")
	public String tables()
	{
		return "tables";
	}
	
	@RequestMapping("/utilities-animation")
	public String animation()
	{
		return "utilities-animation";
	}
	
	@RequestMapping("/utilities-border")
	public String border()
	{
		return "utilities-border";
	}
	
	@RequestMapping("/utilities-color")
	public String color()
	{
		return "utilities-color";
	}
	
	@RequestMapping("/utilities-other")
	public String orther()
	{
		return "utilities-other";
	}
	
}