package player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AdvancedTeamStats {

	private final Collection<Integer> pointsScoredAgainst = new ArrayList<>();
	private final Collection<Double> pointsExpectedAgainst = new ArrayList<>();
	private final Set<Team> teamsAgainst = new HashSet<Team>();
	private Optional<Double> averagePointsAgainst = Optional.empty();
	private Optional<Double> averagePointsWeighting = Optional.empty();

	public Double getAveragePointsAgainst() {
		if (!averagePointsAgainst.isPresent()) {
			averagePointsAgainst =
					Optional.of(pointsScoredAgainst.stream().collect(
							Collectors.averagingInt(i -> (Integer) i)));
		}
		 return averagePointsAgainst.get();
//		return pointsScoredAgainst.stream().collect(Collectors.averagingInt(i -> i));
	}

	public Double getAveragePointsWeighting() {
		if (!averagePointsWeighting.isPresent()) {
			Double pointsAgainstTotal = pointsScoredAgainst.stream().collect(Collectors.summingDouble(d-> d));
			Double pointsExpectedAgainstTotal = pointsExpectedAgainst.stream().collect(Collectors.summingDouble(d-> d));
			averagePointsWeighting =
					Optional.of(pointsAgainstTotal/pointsExpectedAgainstTotal);
		}
		 return averagePointsWeighting.get();
	}

	@JsonIgnore
	public Collection<Integer> getPointsScoredAgainst() {
		return pointsScoredAgainst;
	}

	@JsonIgnore
	public Collection<Double> getPointsExpectedAgainst() {
		return pointsExpectedAgainst;
	}

	@JsonIgnore
	public Set<Team> getTeamsAgainst() {
		return teamsAgainst;
	}

}
