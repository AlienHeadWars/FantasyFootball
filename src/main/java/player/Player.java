package player;

import static utilities.CommonValues.NOT_SET_INTEGER;
import static utilities.CommonValues.NOT_SET_STRING;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import couchdb.SimpleEntity;
import fixture.Fixture;
import fixture.FixtureHistory;

public class Player extends SimpleEntity{

	private String webName = NOT_SET_STRING;
	private Collection<FixtureHistory> playerGames = new ArrayList<FixtureHistory>();
	private Collection<Fixture> playerFixtures = new ArrayList<Fixture>();
	private PositionType type = null;
	private Team team = null;
	private String firstName = NOT_SET_STRING;
	private String lastName = NOT_SET_STRING;
	private Integer chanceOfPlayingThisRound = NOT_SET_INTEGER;
	private Integer chanceOfPlayingNextRound = NOT_SET_INTEGER;
	private AdvancedPlayerStats advancedStats;

	public Player() {
		super();
	}

	@JsonProperty("id")
	public void setPlayerId(String id){
		super.setId(id);
	}
	
	@JsonProperty("webName")
	public String getWebName() {
		return webName;
	}

	@JsonProperty("playerGames")
	public Collection<FixtureHistory> getPlayerGames() {
		return playerGames;
	}

	@JsonProperty("playerFixtures")
	public Collection<Fixture> getPlayerFixtures() {
		return playerFixtures;
	}

	@JsonProperty("type")
	public PositionType getType() {
		return type;
	}

	@JsonProperty("team")
	public Team getTeam() {
		return team;
	}
	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("chanceOfPlayingThisRound")
	public Integer getChanceOfPlayingThisRound() {
		return chanceOfPlayingThisRound;
	}

	@JsonProperty("changeOfPlayingNextRound")
	public Integer getChanceOfPlayingNextRound() {
		return chanceOfPlayingNextRound;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public void setPlayerGames(Collection<FixtureHistory> playerGames) {
		this.playerGames = playerGames;
	}

	public void setPlayerFixtures(Collection<Fixture> playerFixtures) {
		this.playerFixtures = playerFixtures;
	}

	public void setType(PositionType type) {
		this.type = type;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setChanceOfPlayingThisRound(Integer chanceOfPlayingThisRound) {
		this.chanceOfPlayingThisRound = chanceOfPlayingThisRound;
	}

	public void setChanceOfPlayingNextRound(Integer chanceOfPlayingNextRound) {
		this.chanceOfPlayingNextRound = chanceOfPlayingNextRound;
	}

	public AdvancedPlayerStats getAdvancedStats() {
		return advancedStats;
	}

	public void setAdvancedStats(AdvancedPlayerStats advancedStats) {
		this.advancedStats = advancedStats;
	}

}