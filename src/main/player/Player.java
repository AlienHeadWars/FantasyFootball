package main.player;

import java.util.Collection;

import main.fixture.Fixture;
import main.fixture.FixtureHistory;

public class Player {

	private Integer playerId;
	private String webName;
	private Collection<FixtureHistory> playerGames;
	private Collection<Fixture> playerFixtures;
	private PositionType type;
	private Team team;
	private String firstName;
	private String lastName;
	private Integer chanceOfPlayingThisRound;
	private Integer chanceOfPlayingNextRound;
}
