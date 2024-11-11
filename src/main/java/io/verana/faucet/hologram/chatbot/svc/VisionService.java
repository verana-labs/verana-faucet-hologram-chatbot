package io.verana.faucet.hologram.chatbot.svc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.verana.faucet.hologram.chatbot.ex.TokenException;
import io.verana.faucet.hologram.chatbot.model.Session;


@ApplicationScoped
public class VisionService {

	
	@Inject Service service;
	
	@Inject EntityManager em;
	
	
	
	private Session getSession(UUID token) throws TokenException {
		
		Session session = null;
		
		Query q = em.createNamedQuery("Session.findWithToken");
		q.setParameter("token", token);
		
		session = (Session) q.getResultList().stream().findFirst().orElse(null);
		if (session == null) {
			throw new TokenException();
		}
		
		return session;
	}
	
	public List<UUID> listMedias(UUID token) throws Exception {
		
		
		Session session = this.getSession(token);
		
		
		List<UUID> response = new ArrayList<UUID>(1);
		response.add(session.getPhoto());
		
		return response;
		
		
		
	}

	

	public void success(UUID token) throws Exception {
		
		Session session = this.getSession(token);
		
		service.notifySuccess(session.getConnectionId());
		
		
	}

	
	public void failure(UUID token) throws Exception {
		
		Session session = this.getSession(token);
		
		service.notifyFailure(session.getConnectionId());
		
		
	
		
	}
	
	
	
}
