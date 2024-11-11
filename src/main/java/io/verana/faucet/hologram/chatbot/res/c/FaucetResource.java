package io.verana.faucet.hologram.chatbot.res.c;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@RegisterRestClient
@Path("")
public interface FaucetResource {

	@POST
	@Path("/faucet")
	public FaucetResponse faucet(FaucetRequest request);
}
