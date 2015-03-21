package player;

import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public enum Team {
	ARSENAL("Arsenal", "ARS"),
	ASTON_VILLA,
	BURNLEY,
	CHELSEA,
	CRYSTAL_PALACE,
	EVERTON,
	HULL,
	LEICESTER,
	LIVERPOOL,
	MANCHESTER_CITY,
	MANCHESTER_UNITED,
	NEWCASTLE,
	QUEENS_PARK_RANGERS,
	SOUTHAMPTON,
	STOKE,
	SWANSEA,
	SUNDERLAND,
	TOTTENHAM,
	WEST_BROM,
	WEST_HAM,
	TEAM_NOT_SET;
	private String teamName, shortName;
	private Team(){
		
	}
	
	private Team(String teamName, String shortName){
		this.teamName=teamName;
		this.shortName=shortName;
	}
	
	public static Team getByTeamName(String teamName){
		return Lists.newArrayList(Team.values()).stream().filter(t->teamName.equals(t.getTeamName())).collect(Collectors.toList()).get(0);
	}

	public static Team getByShortName(String shortName){
		return Lists.newArrayList(Team.values()).stream().filter(t->shortName.equals(t.getShortName())).collect(Collectors.toList()).get(0);
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
