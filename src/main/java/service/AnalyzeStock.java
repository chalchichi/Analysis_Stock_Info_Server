package service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeStock {
	@Autowired
	SendByCSV cv;
	@Autowired
	ElasticSearch_High_Level_Cilent cl;
	public List<List<String>> Getresult(String code)
	{
		
		ArrayList<Integer> mainprice = new ArrayList<>();
		List<Map<String, Object>> maintemp = cl.search_basic(code);
		int mainsum=0;
		int maincount=1;
		for(Map<String, Object> m:maintemp)
		{
			if(m.get("Price").toString().equals("null"))
			{
				mainprice.add((Integer) mainsum/maincount);
			}
			else {
				maincount+=1;
				mainsum+=Integer.parseInt(m.get("Price").toString());
				mainprice.add(Integer.parseInt(m.get("Price").toString()));
			}
		}
		String[] codelist = cv.GetCode();
		String[] namelist = cv.Getname();
		HashMap<String, String> codeandname = new HashMap<>();
		for(int i=0;i<codelist.length;i++)
		{
			codeandname.put(codelist[i], namelist[i]);
		}
		List<HashMap<String, Integer>> pricelist = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Double> codeandsim = new HashMap<>();
		for(String c:codelist)
		{
			ArrayList<Integer> price = new ArrayList<>();
			List<Map<String, Object>> temp = cl.search_basic(c);
			int sum=0;
			int count=1;
			for(Map<String, Object> x:temp)
			{
				if(x.get("Price").toString().equals("null"))
				{
					price.add((Integer) sum/count);
				}
				else {
					count+=1;
					sum+=Integer.parseInt(x.get("Price").toString());
					price.add(Integer.parseInt(x.get("Price").toString()));
				}
			}
			codeandsim.put(c, Cal(mainprice,price));
		}
		List<List<String>> resultlist = new ArrayList<List<String>>();
		for(String c:codelist)
		{
			List<String> stock = new ArrayList<>();
			stock.add(c);
			stock.add(codeandname.get(c));
			stock.add(codeandsim.get(c).toString());
			resultlist.add(stock);
		}
		final Comparator<List<String>> comp= (x,y)->Double.compare(Double.parseDouble(x.get(2)),Double.parseDouble(y.get(2)));
		List<List<String>> result= resultlist.stream().sorted(comp.reversed())
				.filter(x->{
					if(x.get(2).startsWith("N"))
					{
						return false;
					}
					else {
					  return true;
					}
				})
				.collect(Collectors.toList());
		return result;
	}
	
	public Double Cal(ArrayList<Integer> mainprice,ArrayList<Integer> price)
	{
		Double a=0.0;
		Double b=0.0;
		Double c=0.0;
		int size= (mainprice.size() <= price.size()) ? mainprice.size():price.size();
		for(int i=0;i<size;i++)
		{
			c+= mainprice.get(i).doubleValue() * price.get(i).doubleValue();
			a+=mainprice.get(i).doubleValue()*mainprice.get(i).doubleValue();
			b+=price.get(i).doubleValue()*price.get(i).doubleValue();
		}
		return c/(Math.sqrt(a)*Math.sqrt(b));
	}
}
