package service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vo.stockprice;

@Service
public class SendByCSV {
	@Autowired
	KProducer producer;
	
	stockprice stock;
	String[] code;
	String[] name;
	
	public SendByCSV()
	{
		code = GetCode();
		name = Getname();
	}
	public String[] GetCode()
	{
		String[] code = null;
		try {
		// 바이트 단위로 파일읽기
        String filePath = "C://Users//dhdbs//Desktop//WEB//종목코드.txt"; // 대상 파일
        FileInputStream fileStream = null; // 파일 스트림
        
        fileStream = new FileInputStream( filePath );// 파일 스트림 생성
        //버퍼 선언
        byte[ ] readBuffer = new byte[fileStream.available()];
        while (fileStream.read( readBuffer ) != -1){}
        
        code=new String(readBuffer).split("\n");
        fileStream.close(); //스트림 닫기
    } catch (Exception e) {
	e.getStackTrace();
    }
	for(int i=0;i<code.length;i++)
	{
		code[i]=code[i].substring(0,code[i].length()-1);
	}
	return code;
	}
	public String[] Getname()
	{
		String[] name = null;
		try {
		// 바이트 단위로 파일읽기
        String filePath = "C://Users//dhdbs//Desktop//WEB//name.txt"; // 대상 파일
        FileInputStream fileStream = null; // 파일 스트림
        
        fileStream = new FileInputStream( filePath );// 파일 스트림 생성
        //버퍼 선언
        byte[ ] readBuffer = new byte[fileStream.available()];
        while (fileStream.read( readBuffer ) != -1){}
        
        name=new String(readBuffer).split("\n");
        fileStream.close(); //스트림 닫기
    } catch (Exception e) {
	e.getStackTrace();
    }
		for(int i=0;i<name.length;i++)
		{
			name[i]=name[i].substring(0,name[i].length()-1);
		}
		return name;
	}
	
	public List<List<String>> GetByCSV(String name)
	{
		System.err.println(code);
		List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;
        try{
            br = Files.newBufferedReader(Paths.get("C://Users//dhdbs//Desktop//WEB//stock_CSV//"+name.substring(0,name.length()-1)+".csv"));
            Charset.forName("UTF-8");
            String line = "";
    
            while((line = br.readLine()) != null){
                //CSV 1행을 저장하는 리스트
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",");
                //배열에서 리스트 반환
                tmpList = Arrays.asList(array);
                ret.add(tmpList);
            }
            return ret;
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(br != null){
                    br.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
		return ret;
    }

	public void SendtoKafka()
	{
		
		List<List<String>> temp = new ArrayList<List<String>>();
		for(int i=0;i<code.length;i++)
		{
			String oc=code[i];
			String on=name[i];
			try {
				temp=GetByCSV(oc);
				temp.remove(0);
				temp.stream().filter(x->x.get(0).startsWith("2"))
				.forEach(x->{
					try {
						System.err.println("{Time="+x.get(0)+" 00:00"+"Code="+oc+"Price="+x.get(4).substring(0,x.get(4).length()-7)+"Name="+on+"}");
						producer.Kafkasend("{Time="+x.get(0)+" 00:00"+"Code="+oc+"Price="+x.get(4).substring(0,x.get(4).length()-7)+"Name="+on+"}");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
			catch (Exception e) {
				continue;
			}
		}
	}
}