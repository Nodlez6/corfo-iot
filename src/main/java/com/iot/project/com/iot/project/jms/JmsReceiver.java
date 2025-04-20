package com.iot.project.com.iot.project.jms;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import com.iot.project.com.iot.project.dto.sensorData.CreateSensorDataRequest;
import com.iot.project.com.iot.project.entity.SensorDataHeader;

import jakarta.jms.BytesMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

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
	@JmsListener( containerFactory = "jmsTopicContainerFactory", destination = "tf-minera-01")
	public void onMessage(Message jmsMessage) {
		try {
			if (jmsMessage instanceof TextMessage textMessage) {
				CreateSensorDataRequest receivedData = mapper.readValue(textMessage.getText(), CreateSensorDataRequest.class);
				SensorDataHeader response = sensorDataService.createSensorData(receivedData);
				log.info("📥 SensorDataHeader guardado con ID: {}", response.getId());
			} else if (jmsMessage instanceof BytesMessage bytesMessage) {
				String json = leerBytesMessage(bytesMessage);
				CreateSensorDataRequest receivedData = mapper.readValue(json, CreateSensorDataRequest.class);
				SensorDataHeader response = sensorDataService.createSensorData(receivedData);
				log.info("📦 (Bytes) SensorDataHeader guardado con ID: {}", response.getId());
			} else {
				log.warn("⚠️ Tipo de mensaje no manejado: {}", jmsMessage.getClass().getName());
			}
		} catch (Exception e) {
			log.error("❌ Error procesando mensaje", e);
		}
		
	}
	private String leerBytesMessage(BytesMessage bytesMessage) throws Exception {
		bytesMessage.reset();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = bytesMessage.readBytes(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
	}

}