package player;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Path("players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

	private WebResource playersResource;
	private PlayerDAO playerDAO;
	private Map<Integer, Player> playerMap;

	public PlayerResource(Client client) {
		playersResource = client.resource("http://fantasy.premierleague.com/web/api/elements/");
		String couchDbUrl = "http://127.0.0.1:5984/";
		playerDAO = new PlayerDAO(client, couchDbUrl);
		playerMap = new HashMap<>();
	}

	@GET
	@Path("/updateFromFF")
	public Map populatePlayers() throws IOException {
		ClientResponse clientResponse = null;
		Integer playerId = 1;
		while (playerId < 10 && (clientResponse == null || clientResponse.getStatus() != 404)) {
			try {
				clientResponse = playersResource.path(playerId.toString()).get(ClientResponse.class);
				if (clientResponse.getStatus() != 404) {
					Player player = clientResponse.getEntity(PlayerFromFFAPI.class);
					playerMap.put(playerId, player);
					playerDAO.forceSaveEntity(player);
					playerId++;
					System.out.println(playerId);
				}
			} catch (ClientHandlerException e) {
				if (e.getCause() instanceof SocketTimeoutException) {
					e.printStackTrace();
					System.out.println("sleeping");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
					}
				} else
					throw e;
			}
		}
		return playerMap;
	}

	@GET
	@Path("/{playerId}")
	public Player getPlayer(final Integer playerId) {
		WebResource playerResource = playersResource.path(playerId.toString());
		PlayerFromFFAPI player = playerResource.get(PlayerFromFFAPI.class);
		return player;
	}

}
