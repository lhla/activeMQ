package com.lhl.activemq.queue.transaction;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer_TX {

    private static final String ACTIVEMQ_URL = "tcp://192.168.1.104:61616";
    private static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException, IOException {

        //1.创建连接工厂,按照给定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获取连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建会话session
        //有两个参数，第一个叫事务/第二个叫签收
        //Session.AUTO_ACKNOWLEDGE:自动签收  Session.CLIENT_ACKNOWLEDGE:手动签收
        Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        //4.创建目的地(目的地具体指的是队列queue还是主题topic)
        Queue queue = session.createQueue(QUEUE_NAME);
        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);
        while (true){
            //不带时间的接收消息，一直等，不在乎占用系统资源，只要有消息就消费
            //TextMessage textMessage = (TextMessage) messageConsumer.receive();
            //带时间的接收消息，不一直等，只在指定时间内接收消息过时不候
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);

            if(null != textMessage){

                System.out.println("****消费者接收到消息："+textMessage.getText());

                //调用acknowledge方法手动签收
                //textMessage.acknowledge();

            }else {
                break;
            }
        }
        messageConsumer.close();
        session.commit();
        session.close();
        connection.close();

    }

}
