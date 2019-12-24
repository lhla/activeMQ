package com.lhl.activemq.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpringMQ_Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringMQ_Consumer consumer = ctx.getBean(SpringMQ_Consumer.class);
        //消费者消费消息 因为传过来的是字符串，所有使用取值转换为字符串接收
        String retValue = (String) consumer.jmsTemplate.receiveAndConvert();

        System.out.println("******消费者收到的消息："+retValue);
    }

}
