package com.lhl.activemq.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

@Service
public class SpringMQ_Produce {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringMQ_Produce produce = ctx.getBean(SpringMQ_Produce.class);//等同于SpringMQ_Produce produce = new SpringMQ_Produce();
        //使用lambda表达式
        produce.jmsTemplate.send(session -> {
//            TextMessage textMessage = session.createTextMessage("***Spring和ActiveMQ的整合case1......");
            TextMessage textMessage = session.createTextMessage("***Spring和ActiveMQ的整合case for MessageListener......");
            return textMessage;
        });

        System.out.println("*******send task over");

    }

}
