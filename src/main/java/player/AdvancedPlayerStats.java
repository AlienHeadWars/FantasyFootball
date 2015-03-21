package player;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import fixture.Fixture;

public class AdvancedPlayerStats {

	private Double pointsPerPlayedGame;
	private Double extraPointsPer90Minutes;
	private Double pointsLastGame;
	private Double averagePointsLast2Games;
	private Double averagePointsLast4Games;
	private Double averagePointsLast8Games;
	private Double averagePointsLast16Games;
	private Double pointsLastPlayedGame;
	private Double averagePointsLast2PlayedGames;
	private Double averagePointsLast4PlayedGames;
	private Double averagePointsLast8PlayedGames;
	private Double averagePointsLast16PlayedGames;
	private Double averageAverage;
	@JsonIgnore
	private SortedMap<Fixture, Double> gamePredictions = new TreeMap<Fixture, Double>(
			(f1, f2) -> f1.getFixtureDate().compareTo(f2.getFixtureDate()));
	private Double nextGameWeighted;
	private Double totalNext5GameWeeks;
	private Double totalRemaining;

	public Double getPointsPerPlayedGame() {
		return pointsPerPlayedGame;
	}

	public void setPointsPerPlayedGame(Double pointsPerPlayedGame) {
		this.pointsPerPlayedGame = pointsPerPlayedGame;
	}

	public Double getExtraPointsPer90Minutes() {
		return extraPointsPer90Minutes;
	}

	public void setExtraPointsPer90Minutes(Double extraPointsPer90Minutes) {
		this.extraPointsPer90Minutes = extraPointsPer90Minutes;
	}

	public Double getPointsLastGame() {
		return pointsLastGame;
	}

	public void setPointsLastGame(Double pointsLastGame) {
		this.pointsLastGame = pointsLastGame;
	}

	public Double getAveragePointsLast2Games() {
		return averagePointsLast2Games;
	}

	public void setAveragePointsLast2Games(Double averagePointsLast2Games) {
		this.averagePointsLast2Games = averagePointsLast2Games;
	}

	public Double getAveragePointsLast4Games() {
		return averagePointsLast4Games;
	}

	public void setAveragePointsLast4Games(Double averagePointsLast4Games) {
		this.averagePointsLast4Games = averagePointsLast4Games;
	}

	public Double getAveragePointsLast8Games() {
		return averagePointsLast8Games;
	}

	public void setAveragePointsLast8Games(Double averagePointsLast8Games) {
		this.averagePointsLast8Games = averagePointsLast8Games;
	}

	public Double getAveragePointsLast16Games() {
		return averagePointsLast16Games;
	}

	public void setAveragePointsLast16Games(Double averagePointsLast16Games) {
		this.averagePointsLast16Games = averagePointsLast16Games;
	}

	public Double getPointsLastPlayedGame() {
		return pointsLastPlayedGame;
	}

	public void setPointsLastPlayedGame(Double pointsLastPlayedGame) {
		this.pointsLastPlayedGame = pointsLastPlayedGame;
	}

	public Double getAveragePointsLast2PlayedGames() {
		return averagePointsLast2PlayedGames;
	}

	public void setAveragePointsLast2PlayedGames(Double averagePointsLast2PlayedGames) {
		this.averagePointsLast2PlayedGames = averagePointsLast2PlayedGames;
	}

	public Double getAveragePointsLast4PlayedGames() {
		return averagePointsLast4PlayedGames;
	}

	public void setAveragePointsLast4PlayedGames(Double averagePointsLast4PlayedGames) {
		this.averagePointsLast4PlayedGames = averagePointsLast4PlayedGames;
	}

	public Double getAveragePointsLast8PlayedGames() {
		return averagePointsLast8PlayedGames;
	}

	public void setAveragePointsLast8PlayedGames(Double averagePointsLast8PlayedGames) {
		this.averagePointsLast8PlayedGames = averagePointsLast8PlayedGames;
	}

	public Double getAveragePointsLast16PlayedGames() {
		return averagePointsLast16PlayedGames;
	}

	public void setAveragePointsLast16PlayedGames(Double averagePointsLast16PlayedGames) {
		this.averagePointsLast16PlayedGames = averagePointsLast16PlayedGames;
	}

	public Double getAverageAverage() {
		return averageAverage;
	}

	public void setAverageAverage(Double averageAverage) {
		this.averageAverage = averageAverage;
	}

	public SortedMap<Fixture, Double> getGamePredictions() {
		return gamePredictions;
	}

	public void setGamePredictions(SortedMap<Fixture, Double> gamePredictions) {
		this.gamePredictions = gamePredictions;
	}

	@JsonProperty
	public Map<String, Double> getPredictions() {
		return gamePredictions
				.entrySet()
				.stream()
				.collect(Collectors.toMap(e -> e.getKey().getAgainst(), e -> e.getValue()));
	}

	@JsonIgnore
	public void setPredictions(SortedMap<Fixture, Double> gamePredictions) {
	}

	public Double getNextGameWeighted() {
		return nextGameWeighted;
	}

	public void setNextGameWeighted(Double nextGameWeighted) {
		this.nextGameWeighted = nextGameWeighted;
	}

	public Double getTotalNext5GameWeeks() {
		return totalNext5GameWeeks;
	}

	public void setTotalNext5GameWeeks(Double totalNext5GameWeeks) {
		this.totalNext5GameWeeks = totalNext5GameWeeks;
	}

	public Double getTotalRemaining() {
		return totalRemaining;
	}

	public void setTotalRemaining(Double totalRemaining) {
		this.totalRemaining = totalRemaining;
	}

}
