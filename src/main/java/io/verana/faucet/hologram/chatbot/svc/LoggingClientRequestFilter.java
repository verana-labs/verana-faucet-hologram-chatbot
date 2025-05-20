package io.verana.faucet.hologram.chatbot.svc;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;

public class LoggingClientRequestFilter implements ClientRequestFilter {
	
	private static Logger logger = Logger.getLogger(LoggingClientRequestFilter.class);

	@ConfigProperty(name = "com.mobiera.bitdefender.apikey")
	String apiKey;
	
	
	@Override
    public void filter(ClientRequestContext requestContext) {
        //requestContext.getHeaders().add("my_header", "value");
        List<Object> auth = new ArrayList();
        auth.add("ApiKey " + apiKey);
		requestContext.getHeaders().put("Authorization", auth);
        logger.info("filter: " + requestContext.getUri());
        logger.info("filter: " + requestContext.getHeaderString("Authorization"));
        
        
        
    }

}
