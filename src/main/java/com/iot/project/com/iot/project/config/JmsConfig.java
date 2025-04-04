package com.iot.project.com.iot.project.config;



import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.DeliveryMode;
import jakarta.jms.Session;
import lombok.extern.log4j.Log4j2;

@Configuration
@EnableJms
@Log4j2
public class JmsConfig {

	@Value("${mq.use_single_connection:false}")
	private boolean MQ_USE_SINGLE_CONNECTION;
	@Value("${mq.implementation.factory:org.apache.activemq.ActiveMQConnectionFactory}")
	private String MQ_IMPLEMENTAION_FACTORY;
	
	@Value("${mq.broker:tcp://localhost:61616?connectionTimeout=0&keepAlive=true}")
	//@Value("${mq.broker:tcp://186.64.120.248:61617?connectionTimeout=0&keepAlive=true}")	
	
	private String MQ_BROKER;
	
	@Value("${mq.username:}")
	private String MQ_USERNAME;
	
	@Value("${mq.password:}")
	private String MQ_PASSWORD;
	
	@Value("${mq.consumers:1-7}")
	private String MQ_CONSUMERS;

	@Value("${mq.is_pub_sub:true}")
	private boolean IS_PUB_SUB;
	
	
	
	
	

	@Bean(name="jmsTopicContainerFactory")
	JmsListenerContainerFactory<?> jmsTopicContainerFactory() {
		DefaultJmsListenerContainerFactory factory_ =
				new DefaultJmsListenerContainerFactory();
		factory_.setConnectionFactory(connectionFactory());
		factory_.setPubSubDomain(IS_PUB_SUB);
		factory_.setConcurrency(MQ_CONSUMERS);
		return factory_;
	}

	
	@Bean
	JmsTemplate jmsTemplate() {

		JmsTemplate jmsTemplate = new JmsTemplate();

		jmsTemplate.setDefaultDestinationName("");

		jmsTemplate.setExplicitQosEnabled(true);
		jmsTemplate.setSessionTransacted(false);
		jmsTemplate.setPubSubDomain(IS_PUB_SUB);
		//jmsTemplate.setMessageConverter(new JmsMessageConverter());
		jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
		jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		jmsTemplate.setConnectionFactory(connectionFactory());
		log.debug("Template "+jmsTemplate);

		return jmsTemplate;
	}
	@Bean
	ConnectionFactory connectionFactory() {

		log.debug(" ---------- JmsConfig - connectionFactory. Using: "+ MQ_IMPLEMENTAION_FACTORY);

		ConnectionFactory connectionFactory = null;

		try {
			Constructor<?> constructor=null;
			Object o=null;
			try {
				constructor = Class.forName(MQ_IMPLEMENTAION_FACTORY).getConstructor(String.class);
				o=constructor.newInstance(MQ_BROKER);
				log.debug("Consturctor ConectionFactory(BrokerUrl) FOUND.");


			} catch (NoSuchMethodException | SecurityException e) {
				log.error("Consturctor ConectionFactory(BrokerUrl) NOT FOUND!.");
				return null;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			connectionFactory = (ConnectionFactory) o;

		} catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			log.error(e.getMessage());
		}


		return connectionFactory;

	}

	
	
}
