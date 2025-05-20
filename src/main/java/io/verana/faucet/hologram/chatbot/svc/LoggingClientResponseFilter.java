package io.verana.faucet.hologram.chatbot.svc;

import java.io.BufferedInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import io.twentysixty.sa.client.util.JsonUtil;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;

public class LoggingClientResponseFilter implements ClientResponseFilter {
	
	private static Logger logger = Logger.getLogger(LoggingClientResponseFilter.class);

	

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		logger.info("request: " + requestContext.getUri() + " " + 
	
				JsonUtil.serialize(requestContext.getEntity(), false));
		
		BufferedInputStream stream = new BufferedInputStream(responseContext.getEntityStream());        
    	String payload;
		try {
			payload = IOUtils.toString(stream, "UTF-8");
			
			logger.info("response: " + payload);
			
			responseContext.setEntityStream(IOUtils.toInputStream(payload, "UTF-8"));
	    	
		} catch (IOException e) {
			
		}         
    	
		
		
	}

}
