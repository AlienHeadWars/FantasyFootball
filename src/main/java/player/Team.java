package player;

import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public enum Team {
	ARSENAL(
			"Arsenal", "ARS"),
	ASTON_VILLA(
			"Aston Villa", "AVL"),
	BURNLEY(
			"Burnley", "BUR"),
	CHELSEA(
			"Chelsea", "CHE"),
	CRYSTAL_PALACE(
			"Crystal Palace", "CRY"),
	EVERTON(
			"Everton", "EVE"),
	HULL(
			"Hull", "HUL"),
	LEICESTER(
			"Leicester", "LEI"),
	LIVERPOOL(
			"Liverpool", "LIV"),
	MANCHESTER_CITY(
			"Man City", "MCI"),
	MANCHESTER_UNITED(
			"Man Utd", "MUN"),
	NEWCASTLE(
			"Newcastle", "NEW"),
	QUEENS_PARK_RANGERS(
			"QPR", "QPR"),
	SOUTHAMPTON(
			"Southampton", "SOU"),
	STOKE(
			"Stoke", "STK"),
	SWANSEA(
			"Swansea", "SWA"),
	SUNDERLAND(
			"Sunderland", "SUN"),
	TOTTENHAM(
			"Spurs", "TOT"),
	WEST_BROM(
			"West Brom", "WBA"),
	WEST_HAM(
			"West Ham", "WHU");
	private String teamName, shortName;

	private Team() {

	}

	private Team(String teamName, String shortName) {
		this.teamName = teamName;
		this.shortName = shortName;
	}

	public static Team getByTeamName(String teamName) {
		return Lists
				.newArrayList(Team.values())
				.stream()
				.filter(t -> teamName.equals(t.getTeamName()))
				.collect(Collectors.toList())
				.get(0);
	}

	public static Team getByShortName(String shortName) {
		return Lists
				.newArrayList(Team.values())
				.stream()
				.filter(t -> shortName.equals(t.getShortName()))
				.collect(Collectors.toList())
				.get(0);
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
