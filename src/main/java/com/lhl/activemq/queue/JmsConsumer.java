package com.lhl.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer {

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
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地(目的地具体指的是队列queue还是主题topic)
        Queue queue = session.createQueue(QUEUE_NAME);
        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);
        /*
        第一种接收消息的方式：
            同步阻塞方式(receive()
            订阅者或接收者调用MessageConsumer的receive()方法来接收消息，
            receive方法在能够接收到消息之前(或超时之前)将一直阻塞。
        //代码实现
        while (true){
            //不带时间的接收消息，一直等，不在乎占用系统资源，只要有消息就消费
            //TextMessage textMessage = (TextMessage) messageConsumer.receive();
            //带时间的接收消息，不一直等，只在指定时间内接收消息过时不候
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);
            if(textMessage != null){
                System.out.println("****消费者接收到消息："+textMessage.getText());
            }else {
                break;
            }
        }
        messageConsumer.close();
        session.close();
        connection.close();*/

        /*
        第二种接收消息的方式：通过监听的方式来消费消息
            异步非阻塞方式(监听器onMessage())
            订阅者或接收者通过MessageConsumer的setMessageListener(MessageListener listener)注册一个消息监听器，
            当消息到达之后，系统自动调用监听器MessageListener的onMessage(Message message)方法。

        */
        messageConsumer.setMessageListener(new MessageListener() {
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
        });
        System.in.read();//保持程序一直运行，控制台灯不灭
        messageConsumer.close();
        session.close();
        connection.close();

    }

}
