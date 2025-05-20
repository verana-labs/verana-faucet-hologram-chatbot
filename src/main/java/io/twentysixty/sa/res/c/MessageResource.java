package io.twentysixty.sa.res.c;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


import io.twentysixty.sa.client.res.c.MessageInterface;
import io.verana.faucet.hologram.chatbot.svc.LoggingClientRequestFilter;
import io.verana.faucet.hologram.chatbot.svc.LoggingClientResponseFilter;


@RegisterProvider(LoggingClientRequestFilter.class)
@RegisterProvider(LoggingClientResponseFilter.class)
@RegisterRestClient
public interface MessageResource extends MessageInterface {

	
}
