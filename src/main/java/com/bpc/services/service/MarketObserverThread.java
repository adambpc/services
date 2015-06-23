package com.bpc.services.service;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;

public class MarketObserverThread implements Observer, Runnable {
	
	private EventOutput eventOutput = null;
	private MarketService marketService = null;
	private UpdatesQueue updatesQ = null;
	
	public MarketObserverThread(EventOutput eventOutp, MarketService marketServ){
		eventOutput = eventOutp;
		marketService = marketServ;
		updatesQ = new UpdatesQueue();
	}

	public void run() {
		try {
			String message = "{'symbol':'test','entryType'='0','price'='test'}";
			Thread.sleep(1000);
			OutboundEvent.Builder builder = new OutboundEvent.Builder();
			builder.mediaType(MediaType.APPLICATION_JSON_TYPE);
			builder.data(String.class, message);
			OutboundEvent event = builder.build();
			eventOutput.write(event);
			System.out.println(">>>>>>SSE CLIENT HAS BEEN REGISTERED!");
			Thread.sleep(500);
			marketService.addObserver(this);
			while(!eventOutput.isClosed()){
				if(!updatesQ.isEmpty()){
					pushUpdate(updatesQ.dequeue());
					Thread.sleep(200);
				}
			}
			System.out.println("<<<<<<<SSE CLIENT HAS BEEN DEREGISTERED!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(Observable o, Object arg) {
		updatesQ.enqueue(marketService.receiveUpdate());
	}
	
	private void pushUpdate(String message){
		if(!eventOutput.isClosed()){
			OutboundEvent.Builder builder = new OutboundEvent.Builder();
			builder.mediaType(MediaType.APPLICATION_JSON_TYPE);
			builder.data(String.class, message);
			OutboundEvent event = builder.build();
			try {
				eventOutput.write(event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			System.out.println("<<<<<<<EVENT OUTPUT NOT OPEN!");
		}
	}
}