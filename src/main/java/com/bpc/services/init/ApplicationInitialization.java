package com.bpc.services.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bpc.services.service.MarketService;

public class ApplicationInitialization implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void contextInitialized(ServletContextEvent e) {
		ServletContext cntxt = e.getServletContext();
		cntxt.setAttribute("EUR/USD", new MarketService("EUR/USD"));
		cntxt.setAttribute("GBP/USD", new MarketService("GBP/USD"));
		cntxt.setAttribute("AUD/USD", new MarketService("AUD/USD"));
		cntxt.setAttribute("USD/JPY", new MarketService("USD/JPY"));
		cntxt.setAttribute("USD/CAD", new MarketService("USD/CAD"));
		cntxt.setAttribute("USD/CHF", new MarketService("USD/CHF"));
	}
}
