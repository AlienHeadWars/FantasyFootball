package player;

import java.io.IOException;
import java.net.SocketTimeoutException;
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

	public PlayerResource(Client client) {
		playersResource = client
				.resource("http://fantasy.premierleague.com/web/api/elements/");
		String couchDbUrl = "http://127.0.0.1:5984/";
		playerDAO = new PlayerDAO(client, couchDbUrl);
		playerMap = new HashMap<>();
		try {
			populatePlayersFromDB();
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
						Collectors.toMap(player -> player.getWebName(),
								Player::getAdvancedStats, (p1, p2) -> p1))
				.entrySet()
				.stream()
				.sorted((e1, e2) -> -e1.getValue().getAverageAverage()
						.compareTo(e2.getValue().getAverageAverage()))
				.collect(Collectors.toList());

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

	@Path("/last32")
	@GET
	public Object last32() {
		Map<String, Integer> map = new HashMap<>();
		playerMap.forEach((k, v) -> map.put(
				v.getWebName(),
				valueFromLastX(2, v.getPlayerGames())
						+ valueFromLastX(4, v.getPlayerGames())
						+ valueFromLastX(8, v.getPlayerGames())
						+ valueFromLastX(16, v.getPlayerGames())
						+ valueFromLastX(32, v.getPlayerGames())));
		List<Entry<String, Integer>> list = new ArrayList(map.entrySet());
		Collections.sort(list,
				(e1, e2) -> -e1.getValue().compareTo(e2.getValue()));
		return list;

	}

	@Path("/last16")
	@GET
	public Object last16() {
		Map<String, Integer> map = new HashMap<>();
		playerMap.forEach((k, v) -> map.put(
				v.getWebName(),
				valueFromLastX(2, v.getPlayerGames())
						+ valueFromLastX(4, v.getPlayerGames())
						+ valueFromLastX(8, v.getPlayerGames())
						+ valueFromLastX(16, v.getPlayerGames())));
		List<Entry<String, Integer>> list = new ArrayList(map.entrySet());
		Collections.sort(list,
				(e1, e2) -> -e1.getValue().compareTo(e2.getValue()));
		return list;

	}

	@Path("/last8")
	@GET
	public Object last8() {
		Map<String, Integer> map = new HashMap<>();
		playerMap.forEach((k, v) -> map.put(
				v.getWebName(),
				valueFromLastX(2, v.getPlayerGames())
						+ valueFromLastX(4, v.getPlayerGames())
						+ valueFromLastX(8, v.getPlayerGames())));
		List<Entry<String, Integer>> list = new ArrayList(map.entrySet());
		Collections.sort(list,
				(e1, e2) -> -e1.getValue().compareTo(e2.getValue()));
		return list;

	}

	private Integer valueFromLastX(Integer numberOfGames,
			Collection<FixtureHistory> fixtureHistory
	// ,
	// Function<FixtureHistory, Integer> mapFunction
	) {
		List<FixtureHistory> list = new ArrayList<>(fixtureHistory);
		list = list.stream().filter(f -> f.getMinutesPlayed() > 0)
				.collect(Collectors.toList());
		Collections.sort(list,
				(c1, c2) -> c1.getFixtureDate().compareTo(c2.getFixtureDate()));
		return list
				.subList(Math.max(0, list.size() - numberOfGames), list.size())
				.stream()
				.collect(Collectors.summingInt(FixtureHistory::getPoints));

	}

	@GET
	@Path("/updateFromFF")
	public Map populatePlayers() throws IOException {
		ClientResponse clientResponse = null;
		Integer playerId = 1;
		Integer fails = 0;
		while (clientResponse == null || clientResponse.getStatus() != 404) {
			try {
				clientResponse = playersResource.path(playerId.toString()).get(
						ClientResponse.class);
				if (clientResponse.getStatus() != 404) {
					Player player = clientResponse
							.getEntity(PlayerFromFFAPI.class);
					playerMap.put(playerId, player);
					player.setAdvancedStats(AdvancedStatUtilities
							.getAdvancedStatsForPlayer(player));
					playerDAO.forceSaveEntity(player);
					playerId++;
					System.out.println(playerId);
					fails = 0;
				}
			} catch (ClientHandlerException e) {
				fails++;
				if (fails < 10
						&& (e.getCause() instanceof SocketTimeoutException || e
								.getCause() instanceof ConnectTimeoutException)) {
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
