package fixture;

import java.util.Date;

import player.Team;

public class Fixture implements HasFixtureDate{
	private Date fixtureDate;
	private String against;
	private Integer gameSequence;
	
	public Date getFixtureDate() {
		return fixtureDate;
	}
	public void setFixtureDate(Date fixtureDate) {
		this.fixtureDate = fixtureDate;
	}
	public String getAgainst() {
		return against;
	}
	public void setAgainst(String against) {
		this.against = against;
	}
	public Integer getGameSequence() {
		return gameSequence;
	}
	public void setGameSequence(Integer gameSequence) {
		this.gameSequence = gameSequence;
	}

}
