/**
 * @author Adam Hardie
 */
package com.bpc.services.service;

import java.util.Observable;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bpc.services.messaging.JmsApplication;

public class MarketService extends Observable {
	
	private static final Logger logger = LogManager.getLogger("com.bpc.services.market");
	
	//connect to the PriceStreamingService via JMS.
	//receive messages and then call update() on the listening threads
	JmsApplication messagingApp = null;
	MessageListener listener = null;
	volatile String newMessage = "";
	
	public MarketService(String currencyPair){
		listener = new MessageListener() {
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
//                      System.out.println("Received message:\n" + textMessage.getText());
                        consumeMessage(textMessage.getText());
                    }
                } catch (Exception e) {
                    System.out.println("Caught:" + e);
                    e.printStackTrace();
                }
            }
        };
        //messagingApp = new JmsApplication(listener, currencyPair);
	}
	
	public void consumeMessage(String text){
		newMessage = text;
		setChanged();
		notifyObservers();
	}
	
	public String receiveUpdate(){
		return newMessage;
	}
	
	public void restart(){
		try{
			this.messagingApp.closeConnection();
			this.messagingApp.beginConnection(listener, "");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}