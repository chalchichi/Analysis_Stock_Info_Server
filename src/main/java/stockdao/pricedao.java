package stockdao;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import kafka.javaapi.producer.Producer;
import service.KProducer;
import service.Crawling_Send;
import vo.stockprice;

@Service
@Mapper
public class pricedao implements orgindao
{	
	@Autowired
	Crawling_Send crawling;
	stockprice stock;
	String[] code;
	
	public pricedao()
	{
	try {
	       // 바이트 단위로 파일읽기
	        String filePath = "C://Users//dhdbs//Desktop//WEB//종목코드.txt"; // 대상 파일
	        FileInputStream fileStream = null; // 파일 스트림
	        
	        fileStream = new FileInputStream( filePath );// 파일 스트림 생성
	        //버퍼 선언
	        byte[ ] readBuffer = new byte[fileStream.available()];
	        while (fileStream.read( readBuffer ) != -1){}
	        
	        this.code=new String(readBuffer).split("\n");
	        fileStream.close(); //스트림 닫기
	    } catch (Exception e) {
		e.getStackTrace();
	    }
	}


	@Override 
	public String gettime() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getdate() {
		return null;
	}
	
	@Scheduled(cron="0 0/30 9-15 * * MON-FRI")
	public void SendbyCode() {
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm");
				
		Date time = new Date();
				
		String time1 = format1.format(time);
		
		for(int i=0;i<code.length;i++)
		{
			try {
				crawling.getandreturn(code[i]);
			} catch (Exception e) {
				System.out.println(e);
			}
		}		
	}
}
