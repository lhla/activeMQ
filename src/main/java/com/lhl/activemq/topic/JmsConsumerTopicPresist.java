package com.lhl.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * 演示topic持久化：
 *    1.一定要先运行一次消费者，等于向MQ注册，类似我订阅了这个主题。
 *    2.然后再运行生产者发送信息，此时,无论消费者是否在线，
 *    都会接收到，不在线的话，下次连接的时候，会把没有收过的消息都接收下来。
 **/


public class JmsConsumerTopicPresist {


    private static final String ACTIVEMQ_URL = "tcp://192.168.1.104:61616";
    private static final String TOPIC_NAME = "Topic-Persist";

    public static void main(String[] args) throws JMSException, IOException {

        System.out.println("****李四");

        //1.创建连接工厂,按照给定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获取连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        //设置客户端id也就是订阅者：表明有一个名叫张三的用户订阅了消息
        connection.setClientID("李四");

        //3.创建会话session
        //有两个参数，第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地(目的地具体指的是队列queue还是主题topic)
        Topic topic = session.createTopic(TOPIC_NAME);
        //创建主题的订阅者，传入订阅的主题还有备注
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic,"remark...");
        connection.start();

        Message message = topicSubscriber.receive();
        while (message != null){
            TextMessage textMessage = (TextMessage)message;
            System.out.println("****收到的持久化topic："+textMessage.getText());
            message = topicSubscriber.receive();
        }

        session.close();
        connection.close();

    }
}
