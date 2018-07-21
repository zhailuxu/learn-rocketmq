package com.learn.java.start;

import java.io.UnsupportedEncodingException;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.qianmi.ms.starter.rocketmq.core.RocketMQTemplate;

/**
 * Hello world!
 * 
 */
@RestController
@SpringBootApplication
public class App {

	@Autowired
	private Producer producer;

	@RequestMapping("/home")
	String home() {
		return "Hello World!";
	}

	@RequestMapping("/sendmsg")
	String sendmsg() {
		try {
			Message msg = new Message("TopicTest" /* Topic */, "TagA" /* Tag */,
					("Hello RocketMQ ").getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
			);
			
			return JSON.toJSONString( producer.send(msg));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "error!";
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
