package com.bpc.services.resource;

import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;

import com.bpc.services.service.MarketObserverThread;
import com.bpc.services.service.MarketService;

@Singleton
@Path("marketservice")
public class MarketResource {	
	@GET
	@Path("/stream_price")
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	public EventOutput getServerSentEvents(@Context ServletContext context, @QueryParam("sym") String sym){
		final EventOutput eventOutput = new EventOutput();
		final MarketService marketService = (MarketService) context.getAttribute(sym.toUpperCase());
		new Thread( new MarketObserverThread(eventOutput, marketService) ).start();
		return eventOutput;
	}
}