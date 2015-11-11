package player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import fixture.Fixture;
import fixture.FixtureHistory;
import fixture.HasFixtureDate;

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
	public static Map<String, Map<PositionType, AdvancedTeamStats>> getTeamWeights(
			Collection<Player> players) {
		Map<String, Map<PositionType, AdvancedTeamStats>> teamMap = new HashMap<>();

		Lists.newArrayList(Team.values()).forEach(team -> {
			String homeName = team.getTeamName() + " (H)";
			teamMap.put(homeName, new HashMap<>());
			String awayName = team.getTeamName() + " (A)";
			teamMap.put(awayName, new HashMap<>());
			Lists.newArrayList(PositionType.values()).forEach(position -> {
				teamMap.get(homeName).put(position, new AdvancedTeamStats());
				teamMap.get(awayName).put(position, new AdvancedTeamStats());
			});
			;
		});
		players.forEach(player ->

		player
				.getPlayerGames()
				.stream()
				.filter(game -> 0 < game.getMinutesPlayed())
				.filter(now())
				.forEach(
						game -> {
							String key = game.getResult().substring(0, 6);
							String teamAgainst = game.getResult().substring(0, 3);
							key =
									key.replace(teamAgainst, Team
											.getByShortName(teamAgainst)
											.getTeamName() + " ");
							AdvancedTeamStats advancedTeamStats =
									teamMap.get(key).get(player.getType());
							advancedTeamStats.getPointsScoredAgainst().add(game.getPoints());
							advancedTeamStats.getPointsExpectedAgainst().add(
									player
											.getAdvancedStats()
											.getPointsPerPlayedGame()
											.equals(Double.NaN) ? 0 : player
											.getAdvancedStats()
											.getPointsPerPlayedGame());
						}));
		return teamMap;
	}

	public static AdvancedPlayerStats getAdvancedStatsForPlayer(Player player) {

		AdvancedPlayerStats advancedStats = new AdvancedPlayerStats();
		advancedStats.setExtraPointsPer90Minutes(averageFromLast(
				38,
				player.getPlayerGames(),
				true,
				fh -> (fh.getExtraPoints() * 90) / fh.getMinutesPlayed()));
		advancedStats.setPointsPerPlayedGame(averagePointsFromLastX(
				38,
				player.getPlayerGames(),
				true));
		advancedStats.setPointsLastGame(averagePointsFromLastX(1, player.getPlayerGames(), false));
		advancedStats.setAveragePointsLast2Games(averagePointsFromLastX(
				2,
				player.getPlayerGames(),
				false));
		advancedStats.setAveragePointsLast4Games(averagePointsFromLastX(
				4,
				player.getPlayerGames(),
				false));
		advancedStats.setAveragePointsLast8Games(averagePointsFromLastX(
				8,
				player.getPlayerGames(),
				false));
		advancedStats.setAveragePointsLast16Games(averagePointsFromLastX(
				16,
				player.getPlayerGames(),
				false));
		advancedStats.setPointsLastPlayedGame(averagePointsFromLastX(
				1,
				player.getPlayerGames(),
				true));
		advancedStats.setAveragePointsLast2PlayedGames(averagePointsFromLastX(
				2,
				player.getPlayerGames(),
				true));
		advancedStats.setAveragePointsLast4PlayedGames(averagePointsFromLastX(
				4,
				player.getPlayerGames(),
				true));
		advancedStats.setAveragePointsLast8PlayedGames(averagePointsFromLastX(
				8,
				player.getPlayerGames(),
				true));
		advancedStats.setAveragePointsLast16PlayedGames(averagePointsFromLastX(
				16,
				player.getPlayerGames(),
				true));
		advancedStats.setAverageAverage(average(
		// advancedStats.getPointsPerPlayedGame(),
		// advancedStats.getPointsLastGame(),
		// advancedStats.getPointsLastPlayedGame(),
				advancedStats.getAveragePointsLast2Games(),
//				advancedStats.getAveragePointsLast2PlayedGames(),
				advancedStats.getAveragePointsLast4Games(),
//				advancedStats.getAveragePointsLast4PlayedGames(),
				advancedStats.getAveragePointsLast8Games(),
//				advancedStats.getAveragePointsLast8PlayedGames(),
				advancedStats.getAveragePointsLast16Games()
//				,
//				advancedStats.getAveragePointsLast16PlayedGames()
				));
		;
		return advancedStats;
	}

	private static Double average(Double... doubles) {
		return Lists.newArrayList(doubles).stream().collect(Collectors.averagingDouble(d -> d));
	}

	private static Double averagePointsFromLastX(
			Integer numberOfGames,
			Collection<FixtureHistory> fixtureHistory,
			boolean playedOnly) {
		return averageFromLast(numberOfGames, fixtureHistory, playedOnly, FixtureHistory::getPoints);

	}

	private static Double averageFromLast(
			Integer numberOfGames,
			Collection<FixtureHistory> fixtureHistory,
			boolean playedOnly,
			ToDoubleFunction<? super FixtureHistory> toDoubleFunction) {
		List<FixtureHistory> list = new ArrayList<>(fixtureHistory);

		Stream<FixtureHistory> filtered = list.stream();
		if (playedOnly) {
			filtered = filtered.filter(f -> f.getMinutesPlayed() > 0);
		}
		list = filtered.filter(now()).collect(Collectors.toList());
		Collections.sort(list, (c1, c2) -> c1.getFixtureDate().compareTo(c2.getFixtureDate()));
		return list
				.subList(Math.max(0, list.size() - numberOfGames), list.size())
				.stream()
				.collect(Collectors.averagingDouble(toDoubleFunction));
	}

	private static Predicate<? super HasFixtureDate> now() {
		Date date = new Date();
		Long now = date.getTime()-120*60*1000;
		Predicate<? super HasFixtureDate> predicate = f -> f.getFixtureDate().getTime() < now;
		return predicate;
	}

	public static void populatePlayerPredictions(
			Map<String, Map<PositionType, AdvancedTeamStats>> teamWeights,
			Collection<Player> values) {
		values.forEach(player -> {
			AdvancedPlayerStats advancedStats = player.getAdvancedStats();
			player
					.getPlayerFixtures()
					.stream()
					.filter(fixture -> !"-".equals(fixture.getAgainst()))
					.forEach(
							fixture -> advancedStats.getGamePredictions().put(
									fixture,
									advancedStats.getAverageAverage()
											* teamWeights
													.get(fixture.getAgainst())
													.get(player.getType())
													.getAveragePointsWeighting()));

			Fixture firstKey = advancedStats.getGamePredictions().firstKey();
			Double nextGameWeighted = advancedStats.getGamePredictions().get(firstKey);
			advancedStats.setNextGameWeekWeighted(advancedStats
					.getGamePredictions()
					.entrySet()
					.stream()
					.filter((e) -> e.getKey().getGameSequence().equals(firstKey.getGameSequence()))
					.collect(Collectors.summingDouble(e -> e.getValue())));
			advancedStats.setTotalRemaining(advancedStats
					.getGamePredictions()
					.values()
					.stream()
					.collect(Collectors.summingDouble(d -> d)));
			advancedStats.setTotalNext5GameWeeks(advancedStats
					.getGamePredictions()
					.entrySet()
					.stream()
					.filter((e) -> e.getKey().getGameSequence() < firstKey.getGameSequence() + 5)
					.collect(Collectors.summingDouble(e -> e.getValue())));
		});

	}
}
