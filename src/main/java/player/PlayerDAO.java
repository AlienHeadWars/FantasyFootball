package player;

import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.jersey.api.client.Client;

import couchdb.AbstractDAO;
import couchdb.QueryResponse;

public class PlayerDAO extends AbstractDAO<Player> {

	public PlayerDAO(Client clientParameter, String couchDbUrl) {
		super(Player.class, clientParameter, couchDbUrl, Optional.empty());
	}

	@Override
	protected TypeReference<QueryResponse<Player>> getTypeReferenceForResponse() {
		return new TypeReference<QueryResponse<Player>>() {
		};
	}

}
