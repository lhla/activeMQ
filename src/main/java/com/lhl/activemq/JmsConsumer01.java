package com.lhl.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * 演示消费者的3种消费情况：(Y：代表能  N:代表不能)
 *      1.先生产，只启动1号消费者。问题: 1号消费者能消费消息吗? Y
 *      2.先生产，先启动1号消费者，再启动2号消费者，问题: 2号消费者还能消费消息吗?
 *          * 1号可以消费吗？ Y
 *          * 2号可以消费吗？ N
 *      3.先启动2个消费者，再生产6条消息，请问，消费情况如何?（以下三种情况那个成立，在成立那个后面写Y）
 *          1） 2个消费者都有6条
 *          2） 先到先得，6条全给给一个
 *          3） 一人一半 Y
 */

public class JmsConsumer01 {

    private static final String ACTIVEMQ_URL = "tcp://192.168.1.104:61616";
    private static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException, IOException {

        //System.out.println("***我是1号消费者");
        System.out.println("***我是2号消费者");

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
        //通过监听的方式来消费消息
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
