package com.bpc.services.messaging;
import java.io.IOException;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsApplication {
    // JMS Message Receiver
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
    Connection connection = null;
    
    public JmsApplication(MessageListener listener, String currencyPair){
    	try {
    		connection = connectionFactory.createConnection();
			beginConnection(listener, currencyPair);
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }

    // Name of the topic from which we will receive messages from
    public void beginConnection(MessageListener listener, String currencyPair) throws JMSException {
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(currencyPair);
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(listener);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void closeConnection(){
    	try {
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}    