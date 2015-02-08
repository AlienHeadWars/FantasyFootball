package main.player;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

@Path("players")
public class PlayerResource {

	private Client client;
	private WebResource playersResource;
	
	public PlayerResource(Client client){
		this.client=client;
		playersResource=client.resource("http://fantasy.premierleague.com/web/api/elements/");
	}
	
	@GET
	@Path("/{playerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Player getPLayer(@PathParam("playerId") final Integer playerId) {
		WebResource playerResource=playersResource.path(playerId.toString());
		Player player = playerResource.get(Player.class);
		player.setPlayerId(playerId);
		return player;
	}
}
