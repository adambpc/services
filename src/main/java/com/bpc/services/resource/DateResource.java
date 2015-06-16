/**
 * @author Adam Hardie
 */
package com.bpc.services.resource;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bpc.services.service.DateService;

@Path("dates")
public class DateResource {
	
	private static final Logger logger = LogManager.getLogger("com.itb.services.dates");
	
	/**
	 * Return days of the week using locale lookup
	 * @return
	 */
	@GET
	@Path("weekdays")
	@Produces({MediaType.APPLICATION_XML})
	public Response getDays(@QueryParam("language") String language) {
		String response = "";
		try {
			response = DateService.getDays(language.toLowerCase());
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
			response = wrapException(e);
		}
		return Response.status(200).entity(response).build();
	}
	
	/**
	 * Return month names using locale lookup
	 * @return
	 */
	@GET
	@Path("months")
	@Produces({MediaType.APPLICATION_XML})
	public Response getMonths(@QueryParam("language") String language) {
		String response = "";
		try {
			response = DateService.getMonths(language.toLowerCase());
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
			response = wrapException(e);
		}
		return Response.status(200).entity(response).build();
	}
	
	private String wrapException(Exception e){
		return "<error>" + e.getMessage() + "</error>";
	}
}
