package player;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.conn.ConnectTimeoutException;

import com.google.common.collect.Maps;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import fixture.FixtureHistory;

@Path("players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

	private WebResource playersResource;
	private PlayerDAO playerDAO;
	private Map<Integer, Player> playerMap;
	private Map<String, Map<PositionType, AdvancedTeamStats>> teamWeights;

	public PlayerResource(Client client) {
		playersResource = client.resource("http://fantasy.premierleague.com/web/api/elements/");
		String couchDbUrl = "http://127.0.0.1:5984/";
		playerDAO = new PlayerDAO(client, couchDbUrl);
		playerMap = new HashMap<>();
		try {
			populatePlayersFromDB();
			teamWeights = AdvancedStatUtilities.getTeamWeights(playerMap.values());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@GET
	@Path("/all")
	public Map getPlayers() throws IOException {
		return playerMap;
	}

	@GET
	@Path("/stats")
	public List getPlayerStats() throws IOException {
		return playerMap
				.values()
				.stream()
				.collect(
						Collectors.toMap(player -> player.getWebName(), Player::getAdvancedStats, (
								p1,
								p2) -> p1))

				.entrySet()
				.stream()
				.sorted(
						(e1, e2) -> -e1
								.getValue()
								.getNextGameWeighted()
								.compareTo(e2.getValue().getNextGameWeighted()))
				.collect(Collectors.toList());

	}

	@GET
	@Path("/teamstats")
	public List getTeamStats() throws IOException {
		List<Object> collect =
				teamWeights
						.entrySet()
						.stream()
						.flatMap(
								teamEntry -> teamEntry
										.getValue()
										.entrySet()
										.stream()
										.map(
												positionEntry -> new SimpleEntry<String, AdvancedTeamStats>(
														teamEntry.getKey() + positionEntry.getKey(),
														positionEntry.getValue())))
						.sorted(
								(e1, e2) -> -((AdvancedTeamStats) ((Entry) e1).getValue())
										.getAveragePointsWeighting()
										.compareTo(
												((AdvancedTeamStats) ((Entry) e2).getValue())
														.getAveragePointsWeighting()))
						.collect(Collectors.toList());
		return collect;

	}

	private void populatePlayersFromDB() throws IOException {
		Integer playerId = 1;
		Player player = playerDAO.getEntity(playerId.toString());
		while (player != null) {
			playerMap.put(playerId, player);
			System.out.println(playerId);
			playerId++;
			player = playerDAO.getEntity(playerId.toString());
		}
	}

	@GET
	@Path("/updateFromFF")
	public Map populatePlayers() throws IOException {
		ClientResponse clientResponse = null;
		Integer playerId = 1;
		Integer fails = 0;
		while (clientResponse == null || clientResponse.getStatus() != 404) {
			try {
				clientResponse =
						playersResource.path(playerId.toString()).get(ClientResponse.class);
				if (clientResponse.getStatus() != 404) {
					Player player = clientResponse.getEntity(PlayerFromFFAPI.class);
					playerMap.put(playerId, player);
					playerId++;
					System.out.println(playerId);
					fails = 0;
				}
			} catch (ClientHandlerException e) {
				fails++;
				if (fails < 10
						&& (e.getCause() instanceof SocketTimeoutException || e.getCause() instanceof ConnectTimeoutException)) {
					e.printStackTrace();
					System.out.println("sleeping" + fails);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
					}
				} else
					throw e;
			}
		}
		recalculateStats();

		playerMap.forEach((id, player) -> {
			try {
				playerDAO.forceSaveEntity(player);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		return playerMap;
	}

	@GET
	@Path("/recalculateStats")
	public  List recalculateStats() throws IOException {
		playerMap.forEach((id, player) -> player.setAdvancedStats(AdvancedStatUtilities
				.getAdvancedStatsForPlayer(player)));
		teamWeights = AdvancedStatUtilities.getTeamWeights(playerMap.values());
		AdvancedStatUtilities.populatePlayerPredictions(teamWeights, playerMap.values());
		return getPlayerStats();
	}

	@GET
	@Path("/{playerId}")
	public Player getPlayer(final Integer playerId) {
		WebResource playerResource = playersResource.path(playerId.toString());
		PlayerFromFFAPI player = playerResource.get(PlayerFromFFAPI.class);
		return player;
	}

}
