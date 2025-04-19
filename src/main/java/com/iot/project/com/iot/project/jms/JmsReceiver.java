package com.iot.project.com.iot.project.jms;

import com.iot.project.com.iot.project.dto.sensorData.CreateSensorDataRequest;
import com.iot.project.com.iot.project.entity.SensorDataHeader;

import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.project.com.iot.project.service.SensorDataService;

import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class JmsReceiver  {
	protected final Log logger = LogFactory.getLog(getClass().getName());
	ObjectMapper mapper = new ObjectMapper();
	@Autowired SensorDataService sensorDataService;
	@JmsListener( containerFactory = "jmsTopicContainerFactory", destination = "tf-mining-01")
	public void onMessage(Message jmsMessage) {
		if(jmsMessage instanceof TextMessage)
		{
			try {
				CreateSensorDataRequest receivedData = this.mapper.readValue(((TextMessage) jmsMessage).getText(), CreateSensorDataRequest.class);
				SensorDataHeader _response = sensorDataService.createSensorData(receivedData);
				System.out.println(_response.getId());
			}catch (JsonParseException e) {
				logger.error("Can´t parse message 1", e);
			}
			catch (Exception e) {
				logger.error("Can´t parse message 2", e);
			} 
		}
		
	}

}