package service;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
public class KProducer{

	
	public void Kafkasend(String msg) throws Exception {
		
        Properties configs = new Properties();
        configs.put("bootstrap.servers", "localhost:9092"); // kafka host 및 server 설정
        configs.put("acks", "all");                         // 자신이 보낸 메시지에 대해 카프카로부터 확인을 기다리지 않습니다.
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");   // serialize 설정
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // serialize 설정
        // producer 생성
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configs);
        
        RecordMetadata meta = producer.send(new ProducerRecord<String, String>("stockinfo", msg)).get();
        producer.flush();
        producer.close();
	}

}
