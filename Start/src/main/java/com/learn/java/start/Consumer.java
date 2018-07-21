/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.learn.java.start;

import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.rocketmq", value = { "nameServer" })
public class Consumer implements InitializingBean {

	private DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("my-consumer-group");

	public String getNameSrvAdr() {
		return nameSrvAdr;
	}

	public void setNameSrvAdr(String nameSrvAdr) {
		this.nameSrvAdr = nameSrvAdr;
	}

	@Value("${spring.rocketmq.nameServer}")
	private String nameSrvAdr;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// 创建消费实例 和 配置ns地址
		consumer.setNamesrvAddr(nameSrvAdr);
		// 订阅TopicTest topic下所有tag
		consumer.subscribe("TopicTest", "*");
		// 注册listen
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				for (MessageExt msg : msgs) {
					String body = "";
					try {
						body = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), body);

				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		/*
		 * Launch the consumer instance.
		 */
		consumer.start();
		System.out.printf("Consumer Started.%n");

	}
}
