package com.lhl.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTopic {


    private static final String ACTIVEMQ_URL = "tcp://192.168.1.104:61616";
    private static final String TOPIC_NAME = "topic-jingdong";

    public static void main(String[] args) throws JMSException, IOException {

        //System.out.println("我是1号消费者");
        System.out.println("我是3号消费者");

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
        Topic topic = session.createTopic(TOPIC_NAME);
        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);

        //通过监听的方式来消费消息
        /*messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message != null && message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("****消费者接收到消息："+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/
        //使用lanmbda表达式代替上面注释了的代码
        messageConsumer.setMessageListener(message ->  {
            if(message != null && message instanceof TextMessage){
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("****消费者接收到消息："+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
          }
        });

        System.in.read();//保持程序一直运行，控制台灯不灭
        messageConsumer.close();
        session.close();
        connection.close();

    }
}
