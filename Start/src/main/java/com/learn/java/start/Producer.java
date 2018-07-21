package com.learn.java.start;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.rocketmq", value = { "nameServer" })
public class Producer implements InitializingBean {

	public Producer() {
	}

	public String getNameSrvAdr() {
		return nameSrvAdr;
	}

	public void setNameSrvAdr(String nameSrvAdr) {
		this.nameSrvAdr = nameSrvAdr;
	}

	@Value("${spring.rocketmq.nameServer}")
	private String nameSrvAdr;

	private DefaultMQProducer producer = new DefaultMQProducer("jiaduo-producer-group");

	public SendResult send(Message msg) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msg);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		producer.setNamesrvAddr(nameSrvAdr);
		producer.start();
		System.out.printf("Producer Started.%n");

	}

}
