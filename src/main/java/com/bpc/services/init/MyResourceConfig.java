package com.bpc.services.init;

import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.web.filter.RequestContextFilter;

public class MyResourceConfig extends ResourceConfig {
	
	public MyResourceConfig(){
		register(RequestContextFilter.class);
		register(SseFeature.class);
	}

}
