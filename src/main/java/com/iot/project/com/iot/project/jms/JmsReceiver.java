package com.iot.project.com.iot.project.jms;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.project.com.iot.project.service.SensorDataService;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class JmsReceiver  {
	protected final Log logger = LogFactory.getLog(getClass().getName());
	ObjectMapper mapper=new ObjectMapper();
	static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	SensorDataService sensorDataService;
	@JmsListener( containerFactory = "jmsTopicContainerFactory", destination = "${mq.destination:HOLAMUNDO}")
	public void onMessage(Message jmsMessage) {
		if(jmsMessage instanceof TextMessage)
		{
			try {
				MyRecievedData recievedData = this.mapper.readValue(((TextMessage) jmsMessage).getText(), MyRecievedData.class);
				recievedData.setDate(SDF.format(new Date()));
				this.sensorDataService.handleMessage(recievedData);
			}catch (Exception e) {
				logger.error("Can´t parse message", e);
			} 
		}
		
	}
	
	
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private static class MyRecievedData{
		String api_key;
		List<SensorJsonData> json_data;
		String date;
		
		@JsonIgnoreProperties(ignoreUnknown = true)
		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		private static class SensorJsonData{
			Integer datetime;
			Double temp;
			Double humidity;
		}		
	}
	
}