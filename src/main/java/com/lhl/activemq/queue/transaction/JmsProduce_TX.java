package com.lhl.activemq.queue.transaction;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 演示MQ的事务和签收：
 *  Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 *  括号里第一个参数是代表事务的值，有两种取值方式true和false
 *      *为true时：主要处理批量生成的消息，当某一条消息出错时，可以回滚，需要自己提交事务
 *      *为false时：自动提交事务，但是消息推送过程发生错误时，不能回滚。
 *
 *   注意：消费者设置事务为true有没有提交的话，会存在消息重复消费，且在
 *   activeMQ网页查看也看不到消费记录
 *
 *   括号里第二个参数代表签收方式：(分两种情况：事务true和非事务false)
 *
 *    非事务false下的签收：
 *      1.Session.AUTO_ACKNOWLEDGE:自动签收 （常用）
 *      2.Session.CLIENT_ACKNOWLEDGE:手动签收（常用）
 *          *手动签收，客户端要调用acknowledge方法手动签收
 *      3.Session.DUPS_OK_ACKNOWLEDGE:允许重复消息（不常用）
 *      4.。。。
 *
 *   事务true下的签收：
 *      1.Session.AUTO_ACKNOWLEDGE:自动签收 （常用）
 *      2.Session.CLIENT_ACKNOWLEDGE:手动签收（常用）
 *          *不需要再手动调用acknowledge方法签收，它自动就签收了
 *      3.Session.DUPS_OK_ACKNOWLEDGE:允许重复消息（不常用）
 *      4.。。。
 *      补充：在事务下，不管你设置Session.AUTO_ACKNOWLEDGE还是Session.CLIENT_ACKNOWLEDGE
 *      都是自动签收，前提写了事务一定要提交，否则会出现重复消费信息
 *
 */


public class JmsProduce_TX {

    private static final String ACTIVEMQ_URL = "tcp://192.168.1.104:61616";
    private static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException {

        //1.创建连接工厂,按照给定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获取连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建会话session
        //有两个参数，第一个叫事务/第二个叫签收
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地(目的地具体指的是队列queue还是主题topic)
        //Destination destination = session.createQueue(QUEUE_NAME);//类似于Collection collection = new ArrayList 因为Collection有两个实现list和set
        Queue queue = session.createQueue(QUEUE_NAME);
        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(queue);

        //持久化消息,服务器宕机,消息已持久化,不会丢失,队列默认是持久化的
        //messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

        //非持久化消息,服务器宕机,消息没有持久化,会丢失
        //messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        //6.通过使用messageProducer生产3条消息发送到MQ的队列里面
        for (int i = 1;i<=3;i++){
            //7.创建消息，这个消息是根据要求格式写好的
            TextMessage message = session.createTextMessage("MessageListener---" + i);//理解为一个字符串
            //8.通过messageProducer发送（推送）给mq
            messageProducer.send(message);
        }

        //9.关闭资源
        messageProducer.close();
        //在session关闭前提交事务
        //这里可以定义异常处理
        /*try {
            //没有异常提交事务
            session.commit();
        }catch (Exception e){
            //出现异常事务回滚
            session.rollback();

        }finally {
            if(session != null){
                //关闭session
                session.close();
            }
        }*/
        session.commit();
        session.close();
        connection.close();

        System.out.println("******消息发送到MQ完成");
    }
}
