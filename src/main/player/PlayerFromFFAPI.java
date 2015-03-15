package main.player;

import java.util.Collection;

import main.fixture.Fixture;
import main.fixture.FixtureHistory;
import main.utilities.CustomDeserialisers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerFromFFAPI extends Player {

	@JsonIgnore
	@Override
	public void setPlayerGames(Collection<FixtureHistory> playerGames) {
		super.setPlayerGames(playerGames);
	}

	@JsonProperty("fixture_history")
	public void setPlayerGames(JsonNode node) {
		JsonNode allGames = node.get("all");
		allGames.elements().forEachRemaining(
				g -> getPlayerGames().add(
						CustomDeserialisers.toFixtureHistory(g)));
	}

	@JsonIgnore
	@Override
	public void setPlayerFixtures(Collection<Fixture> playerFixtures) {
		super.setPlayerFixtures(playerFixtures);
	}

	@JsonProperty("fixtures")
	public void setPlayerFixturesJson(JsonNode node) {
		super.setPlayerFixturesJson(node.get("all"));
	}

	@JsonIgnore
	@Override
	public void setType(PositionType type) {
		super.setType(type);
	}

	@JsonProperty("type_name")
	public void setType(String typeName) {
		setType(PositionType.valueOf(typeName.toUpperCase()));
	}

	@Override
	@JsonIgnore
	public void setTeam(Team team){
		super.setTeam(team);
	}
	@JsonProperty
	public void setTeam(Integer team) {
		setTeam(Team.values()[team - 1]);
	}

	@Override
	@JsonProperty("web_name")
	public void setWebName(String webName) {
		// TODO Auto-generated method stub
		super.setWebName(webName);
	}

	@Override
	@JsonProperty("first_name")
	public void setFirstName(String firstName) {
		// TODO Auto-generated method stub
		super.setFirstName(firstName);
	}

	@Override
	@JsonProperty("last_name")
	public void setLastName(String lastName) {
		// TODO Auto-generated method stub
		super.setLastName(lastName);
	}

	@Override
	@JsonProperty("chance_of_playing_this_round")
	public void setChanceOfPlayingThisRound(Integer chanceOfPlayingThisRound) {
		// TODO Auto-generated method stub
		super.setChanceOfPlayingThisRound(chanceOfPlayingThisRound);
	}

	@Override
	@JsonProperty("chance_of_playing_next_round")
	public void setChanceOfPlayingNextRound(Integer chanceOfPlayingNextRound) {
		// TODO Auto-generated method stub
		super.setChanceOfPlayingNextRound(chanceOfPlayingNextRound);
	}

}
