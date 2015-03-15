package player;

import static utilities.CommonValues.NOT_SET_INTEGER;
import static utilities.CommonValues.NOT_SET_STRING;

import java.util.ArrayList;
import java.util.Collection;

import couchdb.Entity;
import fixture.Fixture;
import fixture.FixtureHistory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class Player extends Entity{

	private String webName = NOT_SET_STRING;
	private Collection<FixtureHistory> playerGames = new ArrayList<FixtureHistory>();
	private Collection<Fixture> playerFixtures = new ArrayList<Fixture>();
	private PositionType type = PositionType.POSITION_NOT_FOUND;
	private Team team = Team.TEAM_NOT_SET;
	private String firstName = NOT_SET_STRING;
	private String lastName = NOT_SET_STRING;
	private Integer chanceOfPlayingThisRound = NOT_SET_INTEGER;
	private Integer chanceOfPlayingNextRound = NOT_SET_INTEGER;
	private JsonNode playerFixturesJson;

	public Player() {
		super();
	}

	@JsonProperty("id")
	public void setPlayerId(String id){
		super.setId(id);
	}
	
	public String getWebName() {
		return webName;
	}

	public Collection<FixtureHistory> getPlayerGames() {
		return playerGames;
	}

	public Collection<Fixture> getPlayerFixtures() {
		return playerFixtures;
	}

	public JsonNode getPlayerFixturesJson() {
		return playerFixturesJson;
	}

	public PositionType getType() {
		return type;
	}

	public Team getTeam() {
		return team;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Integer getChanceOfPlayingThisRound() {
		return chanceOfPlayingThisRound;
	}

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

	public void setPlayerFixturesJson(JsonNode playerFixturesJson) {
		this.playerFixturesJson = playerFixturesJson;
	}

}