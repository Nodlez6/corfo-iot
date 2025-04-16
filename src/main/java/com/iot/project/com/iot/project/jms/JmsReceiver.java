/*package com.iot.project.com.iot.project.jms;

import com.iot.project.com.iot.project.dto.sensorData.CreateSensorDataRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.project.com.iot.project.service.SensorDataService;

import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class JmsReceiver  {
	protected final Log logger = LogFactory.getLog(getClass().getName());
	ObjectMapper mapper = new ObjectMapper();
	private final SensorDataService sensorDataService;
	@JmsListener( containerFactory = "jmsTopicContainerFactory", destination = "${mq.destination:HOLAMUNDO}")
	public void onMessage(Message jmsMessage) {
		if(jmsMessage instanceof TextMessage)
		{
			try {
				CreateSensorDataRequest receivedData = this.mapper.readValue(((TextMessage) jmsMessage).getText(), CreateSensorDataRequest.class);
				sensorDataService.createSensorData(receivedData);
			}catch (Exception e) {
				logger.error("Can´t parse message", e);
			} 
		}
		
	}

}*/