package player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AdvancedTeamStats {

	private final Collection<Integer> pointsScoredAgainst = new ArrayList<>();
	private final Collection<Double> pointsWeightings = new ArrayList<>();
	private final Set<Team> teamsAgainst = new HashSet<Team>();

	public Double getAveragePointsAgainst() {
		return pointsScoredAgainst.stream().collect(Collectors.averagingInt(i -> i));
	}

	public Double getAveragePointsWeighting() {
		return pointsWeightings.stream().collect(Collectors.averagingDouble(d -> d));
	}

	@JsonIgnore
	public Collection<Integer> getPointsScoredAgainst() {
		return pointsScoredAgainst;
	}
	@JsonIgnore
	public Collection<Double> getPointsWeightings() {
		return pointsWeightings;
	}
	@JsonIgnore
	public Set<Team> getTeamsAgainst() {
		return teamsAgainst;
	}

}
