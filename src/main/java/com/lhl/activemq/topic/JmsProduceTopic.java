package com.lhl.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduceTopic {

    private static final String ACTIVEMQ_URL = "tcp://192.168.1.104:61616";
    private static final String TOPIC_NAME = "topic-jingdong";

    public static void main(String[] args) throws JMSException {

        //1.创建连接工厂,按照给定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获取连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建会话session
        //有两个参数，第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地(目的地具体指的是队列queue还是主题topic)
        //Destination destination = session.createQueue(QUEUE_NAME);//类似于Collection collection = new ArrayList 因为Collection有两个实现list和set
        Topic topic = session.createTopic(TOPIC_NAME);
        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);
        //6.通过使用messageProducer生产3条消息发送到MQ的主题里面
        for (int i = 1;i<=3;i++){
            //7.创建消息，这个消息是根据要求格式写好的
            TextMessage message = session.createTextMessage("TOPIC_NAME---" + i);//理解为一个字符串
            //8.通过messageProducer发送（推送）给mq
            messageProducer.send(message);
        }

        //9.关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("******主题(Topic)消息发送到MQ完成");
    }
}
