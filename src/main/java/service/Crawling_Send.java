package service;





import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vo.stockprice;

@Service
public class Crawling_Send {
	@Autowired
	KProducer producer;
	public void getandreturn(String code)
	{
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm");
		Date time = new Date();
		String time1 = format1.format(time);
		System.err.println(time1);
		String Price;
		String Code;
		String Name;
		String URL="https://finance.naver.com/item/main.nhn?code="+code;
		try {
			Document doc = Jsoup.connect(URL).get();
			Elements elem =doc.select("dd");
			if(elem.text().indexOf("현재가")<0)
			{
				return;
			}
			Price = elem.text().substring(elem.text().indexOf("현재가")+4,elem.text().indexOf("전일대비")-1).replaceAll(",","");
			Code = elem.text().substring(elem.text().indexOf("종목코드")+5,elem.text().indexOf("종목코드")+11);
			Name = elem.text().substring(elem.text().indexOf("종목명")+4,elem.text().indexOf("종목코드")-1);
				try {
					System.err.println(Price);
					System.err.println(Code);
					System.err.println(Name);
					producer.Kafkasend("{Time="+time1+"Code="+Code+"Price="+Price+"Name="+Name+"}");
				} catch (Exception e) {
					System.out.println(e);
				}
			} catch (IOException e) {}
	}
	
	public HashMap<String, String> getdata(String code)
	{
		String data=null;
		HashMap<String, String> datamap = new HashMap<>();
		String[] index = {"종목명","종목코드","코스피 현재가","전일대비","전일가","시가 ","고가 ","상한가 ","저가 ","하한가","거래량 ","거래대금"};
		String URL="https://finance.naver.com/item/main.nhn?code="+code;
		try {
			Document doc = Jsoup.connect(URL).get();
			Elements elem =doc.select("dd");
			data = elem.text();
			data=data.substring(data.indexOf("장마감")+4);
			for(int i=0;i<index.length-1;i++)
			{
				datamap.put(index[i], data.substring(data.indexOf(index[i])+index[i].length(),data.indexOf(index[i+1])));
			}
		}
		catch (Exception e) {
			for(int i=0;i<index.length-1;i++)
			{
				datamap.put(index[i], "");
			}
		}
		return datamap;
	}
}