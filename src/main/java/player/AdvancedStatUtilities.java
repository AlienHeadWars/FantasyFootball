package player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import fixture.FixtureHistory;

public class AdvancedStatUtilities {

	// private Double pointsPerPlayedGame;
	// private Double pointsLastGame;
	// private Double averagePointsLast2Games;
	// private Double averagePointsLast4Games;
	// private Double averagePointsLast8Games;
	// private Double averagePointsLast16Games;
	// private Double pointsLastPlayedGame;
	// private Double averagePointsLast2PlayedGame;
	// private Double averagePointsLast4PlayedGame;
	// private Double averagePointsLast8PlayedGame;
	// private Double averagePointsLast16PlayedGame;
	// private Double averageAverage;
	// private SortedMap<Integer, Double> gamePredictions;
	// private Double nextGameWeighted;
	// private Double totalNext5GameWeeks;
	// private Double totalRemaining;
	public static AdvancedStats getAdvancedStatsForPlayer(Player player) {

		AdvancedStats advancedStats = new AdvancedStats();
		advancedStats.setAverageAverage(average(
				advancedStats.getPointsPerGame(),
				advancedStats.getPointsPerPlayedGame(),
				advancedStats.getPointsLastGame(),
				advancedStats.getPointsLastPlayedGame(),
				advancedStats.getAveragePointsLast2Games(),
				advancedStats.getAveragePointsLast2PlayedGames(),
				advancedStats.getAveragePointsLast4Games(),
				advancedStats.getAveragePointsLast4PlayedGames(),
				advancedStats.getAveragePointsLast8Games(),
				advancedStats.getAveragePointsLast8PlayedGames(),
				advancedStats.getAveragePointsLast16Games(),
				advancedStats.getAveragePointsLast16PlayedGames()));
		advancedStats.setPointsPerPlayedGame(averageFromLastX(38, player.getPlayerGames(), true));
		advancedStats.setPointsLastGame(averageFromLastX(1, player.getPlayerGames(), false));
		advancedStats
				.setAveragePointsLast2Games(averageFromLastX(2, player.getPlayerGames(), false));
		advancedStats
				.setAveragePointsLast4Games(averageFromLastX(4, player.getPlayerGames(), false));
		advancedStats
				.setAveragePointsLast8Games(averageFromLastX(8, player.getPlayerGames(), false));
		advancedStats.setAveragePointsLast16Games(averageFromLastX(
				16,
				player.getPlayerGames(),
				false));
		advancedStats.setPointsLastPlayedGame(averageFromLastX(1, player.getPlayerGames(), true));
		advancedStats.setAveragePointsLast2PlayedGames(averageFromLastX(
				2,
				player.getPlayerGames(),
				true));
		advancedStats.setAveragePointsLast4PlayedGames(averageFromLastX(
				4,
				player.getPlayerGames(),
				true));
		advancedStats.setAveragePointsLast8PlayedGames(averageFromLastX(
				8,
				player.getPlayerGames(),
				true));
		advancedStats.setAveragePointsLast16PlayedGames(averageFromLastX(
				16,
				player.getPlayerGames(),
				true));

		advancedStats.setAverageAverage(average());
		return advancedStats;
	}

	private static Double average(Double... doubles) {
		return Lists.newArrayList(doubles).stream().collect(Collectors.averagingDouble(d -> d));
	}

	private static Double averageFromLastX(
			Integer numberOfGames,
			Collection<FixtureHistory> fixtureHistory,
			boolean playedOnly) {
		List<FixtureHistory> list = new ArrayList<>(fixtureHistory);
		if (playedOnly)
			list = list.stream().filter(f -> f.getMinutesPlayed() > 0).collect(Collectors.toList());
		Collections.sort(list, (c1, c2) -> c1.getFixtureDate().compareTo(c2.getFixtureDate()));
		return list
				.subList(Math.max(0, list.size() - numberOfGames), list.size())
				.stream()
				.collect(Collectors.averagingInt(FixtureHistory::getPoints));

	}

}
