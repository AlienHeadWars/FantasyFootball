package player;

import java.util.Collection;

import fixture.CustomDeserialisers;
import fixture.Fixture;
import fixture.FixtureHistory;

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
	public void setPlayerGames_(JsonNode node) {
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
	public void setPlayerFixturesJson_(JsonNode node) {
		JsonNode allGames = node.get("all");
		allGames.elements().forEachRemaining(
				g -> getPlayerFixtures().add(
						CustomDeserialisers.toFixture(g)));
	}

	@JsonIgnore
	@Override
	public void setType(PositionType type) {
		super.setType(type);
	}

	@JsonProperty("type_name")
	public void setType_(String typeName) {
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

	@JsonProperty("web_name")
	public void setWebName_(String webName) {
		// TODO Auto-generated method stub
		super.setWebName(webName);
	}

	@JsonProperty("first_name")
	public void setFirstName_(String firstName) {
		// TODO Auto-generated method stub
		super.setFirstName(firstName);
	}

	@JsonProperty("second_name")
	public void setLastName_(String lastName) {
		// TODO Auto-generated method stub
		super.setLastName(lastName);
	}

	@JsonProperty("chance_of_playing_this_round")
	public void setChanceOfPlayingThisRound_(Integer chanceOfPlayingThisRound) {
		// TODO Auto-generated method stub
		super.setChanceOfPlayingThisRound(chanceOfPlayingThisRound);
	}

	@JsonProperty("chance_of_playing_next_round")
	public void setChanceOfPlayingNextRound_(Integer chanceOfPlayingNextRound) {
		// TODO Auto-generated method stub
		super.setChanceOfPlayingNextRound(chanceOfPlayingNextRound);
	}

}
