package com.bpc.services.service;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;

public class MarketObserverThread implements Observer, Runnable {
	
	private EventOutput eventOutput = null;
	private MarketService marketService = null;
	
	public MarketObserverThread(EventOutput eventOutp, MarketService marketServ){
		eventOutput = eventOutp;
		marketService = marketServ;
	}

	public void run() {
		final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
		eventBuilder.name("price-update");
		eventBuilder.data(String.class, "Hello! You are now registered for price updates");
		final OutboundEvent event = eventBuilder.build();
		try {
			eventOutput.write(event);
			marketService.addObserver(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				eventOutput.close();
			}catch(IOException ioClose){
				throw new RuntimeException("Error when closing event output");
			}
		}
	}

	public void update(Observable o, Object arg) {
		String theUpdate = marketService.receiveUpdate();
		pushUpdate(theUpdate);
	}
	
	private void pushUpdate(String message){
		if(eventOutput != null){
			final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
			eventBuilder.name("price-update");
			eventBuilder.data(String.class, "Price Update \n" + message);
			final OutboundEvent event = eventBuilder.build();
			try {
				eventOutput.write(event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
