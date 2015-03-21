package fixture;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import static utilities.CommonValues.*;

public final class CustomDeserialisers {

	private static DateFormat simpleFormat = new SimpleDateFormat("dd MMM hh:mm");

	public static final FixtureHistory toFixtureHistory(JsonNode jsonNode) {
		FixtureHistory fixtureHistory = new FixtureHistory();
		DoUntoFixtureHistory.valueList().forEach(d -> d.doUnto.accept(jsonNode, fixtureHistory));
		return fixtureHistory;
	}

	public static final Fixture toFixture(JsonNode jsonNode) {
		Fixture fixture = new Fixture();
		DoUntoFixture.valueList().forEach(d -> d.doUnto.accept(jsonNode, fixture));
		return fixture;
	}

	private interface ExtractFromJson<T> {
		T extract(JsonNode jsonNode, Integer propertyValue);
	}

	private static Date getDateProperty(JsonNode jsonNode, Integer propertyNumber) {
		try {
			return simpleFormat.parse(jsonNode.get(propertyNumber).textValue());
		} catch (ParseException e) {
			return NOT_SET_DATE;
		}
	}

	private static Date getFixureDateProperty(JsonNode jsonNode, Integer propertyNumber) {
		Calendar calendar = Calendar.getInstance();
		Date dateProperty = getDateProperty(jsonNode, propertyNumber);
		if (dateProperty == null)
			return dateProperty;
		calendar.setTime(dateProperty);
		Integer year = 2015;
		if (calendar.get(Calendar.MONTH) > Calendar.JULY) {
			year = 2014;
		}
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}

	private static Integer getIntegerProperty(JsonNode jsonNode, Integer propertyNumber) {
		return jsonNode.get(propertyNumber).asInt();
	}

	private static String getStringProperty(JsonNode jsonNode, Integer propertyNumber) {
		return jsonNode.get(propertyNumber).textValue();
	}

	private static final Pattern GAME_SEQUENCE_FROM_FIXTURE_JSON = Pattern.compile("(\\d+)$");

	static enum DoUntoFixture {
		FIXTUREDATE(
				Fixture::setFixtureDate, CustomDeserialisers::getFixureDateProperty),
		GAMESEQUENCE(
				(f, g) -> {
					Matcher m = GAME_SEQUENCE_FROM_FIXTURE_JSON.matcher(g);
					m.find();
					f.setGameSequence(Integer.parseInt(m.group(1)));
				}, CustomDeserialisers::getStringProperty),
		AGAINST(
				Fixture::setAgainst, CustomDeserialisers::getStringProperty);

		public BiConsumer<JsonNode, Fixture> doUnto;

		private <T> DoUntoFixture(BiConsumer<Fixture, T> consumer, ExtractFromJson<T> extract) {
			this.doUnto = (j, f) -> doUnto(this.ordinal(), j, o -> consumer.accept(f, o), extract);
		}

		private static <T> void doUnto(
				Integer ordinal,
				JsonNode jsonNode,
				Consumer<T> setter,
				ExtractFromJson<T> extract) {
			setter.accept(extract.extract(jsonNode, ordinal));
		}

		static List<DoUntoFixture> valueList() {
			return Lists.newArrayList(values());
		}
	}

	static enum DoUntoFixtureHistory {
		FIXTUREDATE(
				FixtureHistory::setFixtureDate, CustomDeserialisers::getFixureDateProperty),
		GAMESEQUENCE(
				FixtureHistory::setGameSequence, CustomDeserialisers::getIntegerProperty),
		RESULT(
				FixtureHistory::setResult, CustomDeserialisers::getStringProperty),
		MINUTESPLAYED(
				FixtureHistory::setMinutesPlayed, CustomDeserialisers::getIntegerProperty),
		GOALSCORED(
				FixtureHistory::setGoalsScored, CustomDeserialisers::getIntegerProperty),
		GOALSCONCECED(
				FixtureHistory::setGoalsConceded, CustomDeserialisers::getIntegerProperty),
		ASSISTS(
				FixtureHistory::setAssists, CustomDeserialisers::getIntegerProperty),
		CLEANSHEETS(
				FixtureHistory::setCleanSheets, CustomDeserialisers::getIntegerProperty),
		OWNGOALS(
				FixtureHistory::setOwnGoals, CustomDeserialisers::getIntegerProperty),
		PENALTIESSAVED(
				FixtureHistory::setPenaltiesSaved, CustomDeserialisers::getIntegerProperty),
		PENALTIESMADE(
				FixtureHistory::setPenaltiesMade, CustomDeserialisers::getIntegerProperty),
		YELLOWCARDS(
				FixtureHistory::setYellowCards, CustomDeserialisers::getIntegerProperty),
		REDCARDS(
				FixtureHistory::setRedCards, CustomDeserialisers::getIntegerProperty),
		SAVES(
				FixtureHistory::setSaves, CustomDeserialisers::getIntegerProperty),
		BONUSPOINTS(
				FixtureHistory::setBonusPoints, CustomDeserialisers::getIntegerProperty),
		EASPORTSPPI(
				FixtureHistory::setEaSportsPPI, CustomDeserialisers::getIntegerProperty),
		BONUSPOINTSSYSTEM(
				FixtureHistory::setBonusPointsSystem, CustomDeserialisers::getIntegerProperty),
		NETTRANSFERS(
				FixtureHistory::setNetTransfers, CustomDeserialisers::getIntegerProperty),
		VALUE(
				FixtureHistory::setValue, CustomDeserialisers::getIntegerProperty),
		POINTS(
				FixtureHistory::setPoints, CustomDeserialisers::getIntegerProperty);

		public BiConsumer<JsonNode, FixtureHistory> doUnto;

		private <T> DoUntoFixtureHistory(BiConsumer<FixtureHistory, T> consumer, ExtractFromJson<T> extract) {
			this.doUnto = (j, f) -> doUnto(this.ordinal(), j, o -> consumer.accept(f, o), extract);
		}

		private static <T> void doUnto(
				Integer ordinal,
				JsonNode jsonNode,
				Consumer<T> setter,
				ExtractFromJson<T> extract) {
			setter.accept(extract.extract(jsonNode, ordinal));
		}

		static List<DoUntoFixtureHistory> valueList() {
			return Lists.newArrayList(values());
		}

	}

}
