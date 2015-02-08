package main.player;

import static main.java.utilities.CommonValues.NOT_SET_INTEGER;
import static main.java.utilities.CommonValues.NOT_SET_STRING;

import java.util.ArrayList;
import java.util.Collection;

import main.fixture.Fixture;
import main.fixture.FixtureHistory;
import main.java.utilities.CustomDeserialisers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {

	private Integer playerId=NOT_SET_INTEGER;
	private String webName=NOT_SET_STRING;
	private Collection<FixtureHistory> playerGames=new ArrayList<FixtureHistory>();
	private Collection<Fixture> playerFixtures=new ArrayList<Fixture>();
	private PositionType type=PositionType.POSITION_NOT_FOUND;
	private Team team=Team.TEAM_NOT_SET;
	private String firstName=NOT_SET_STRING;
	private String lastName=NOT_SET_STRING;
	private Integer chanceOfPlayingThisRound=NOT_SET_INTEGER;
	private Integer chanceOfPlayingNextRound=NOT_SET_INTEGER;
	private JsonNode playerFixturesJson;
	
	public Integer getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	public String getWebName() {
		return webName;
	}
	public void setWebName(String webName) {
		this.webName = webName;
	}
	public Collection<FixtureHistory> getPlayerGames() {
		return playerGames;
	}
	
	@JsonIgnore
	public void setPlayerGames(Collection<FixtureHistory> playerGames) {
		this.playerGames = playerGames;
	}
	
	@JsonProperty("fixture_history")
	public void setPlayerGames(JsonNode node){
		JsonNode allGames=node.get("all");
		allGames.elements().forEachRemaining(g->playerGames.add(CustomDeserialisers.toFixtureHistory(g)));
	}
	
	public Collection<Fixture> getPlayerFixtures() {
		return playerFixtures;
	}
	@JsonIgnore	
	public void setPlayerFixtures(Collection<Fixture> playerFixtures) {
		this.playerFixtures = playerFixtures;
	}
	@JsonProperty("fixtures")
	public void setPlayerFixturesJson(JsonNode node){
		playerFixturesJson = node.get("all");
	}
	
	public JsonNode getPlayerFixturesJson(){
		return playerFixturesJson;
	}
	
	public PositionType getType() {
		return type;
	}
	
	@JsonIgnore
	public void setType(PositionType type) {
		this.type = type;
	}
	
	@JsonProperty("type_name")
	public void setType(String typeName){
		this.type=PositionType.valueOf(typeName.toUpperCase());
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Integer team) {
		this.team = Team.values()[team-1];
	}
	public String getFirstName() {
		return firstName;
	}
	@JsonProperty("first_name")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	@JsonProperty("second_name")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getChanceOfPlayingThisRound() {
		return chanceOfPlayingThisRound;
	}
	@JsonProperty("chance_of_playing_this_round")
	public void setChanceOfPlayingThisRound(Integer chanceOfPlayingThisRound) {
		this.chanceOfPlayingThisRound = chanceOfPlayingThisRound;
	}
	public Integer getChanceOfPlayingNextRound() {
		return chanceOfPlayingNextRound;
	}
	@JsonProperty("chance_of_playing_next_round")
	public void setChanceOfPlayingNextRound(Integer chanceOfPlayingNextRound) {
		this.chanceOfPlayingNextRound = chanceOfPlayingNextRound;
	}
}
