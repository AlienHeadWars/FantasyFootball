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
		calendar.setTime(getDateProperty(jsonNode, propertyNumber));
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

	static enum DoUntoFixtureHistory {
		FIXTUREDATE(FixtureHistory::setFixtureDate, CustomDeserialisers::getFixureDateProperty),
		GAMESEQUENCE(FixtureHistory::setGameSequence, CustomDeserialisers::getIntegerProperty),
		RESULT(FixtureHistory::setResult, CustomDeserialisers::getStringProperty),
		MINUTESPLAYED(FixtureHistory::setMinutesPlayed, CustomDeserialisers::getIntegerProperty),
		GOALSCORED(FixtureHistory::setGoalsScored, CustomDeserialisers::getIntegerProperty),
		GOALSCONCECED(FixtureHistory::setGoalsConceded, CustomDeserialisers::getIntegerProperty),
		ASSISTS(FixtureHistory::setAssists, CustomDeserialisers::getIntegerProperty),
		CLEANSHEETS(FixtureHistory::setCleanSheets, CustomDeserialisers::getIntegerProperty),
		OWNGOALS(FixtureHistory::setOwnGoals, CustomDeserialisers::getIntegerProperty),
		PENALTIESSAVED(FixtureHistory::setPenaltiesSaved, CustomDeserialisers::getIntegerProperty),
		PENALTIESMADE(FixtureHistory::setPenaltiesMade, CustomDeserialisers::getIntegerProperty),
		YELLOWCARDS(FixtureHistory::setYellowCards, CustomDeserialisers::getIntegerProperty),
		REDCARDS(FixtureHistory::setRedCards, CustomDeserialisers::getIntegerProperty),
		SAVES(FixtureHistory::setSaves, CustomDeserialisers::getIntegerProperty),
		BONUSPOINTS(FixtureHistory::setBonusPoints, CustomDeserialisers::getIntegerProperty),
		EASPORTSPPI(FixtureHistory::setEaSportsPPI, CustomDeserialisers::getIntegerProperty),
		BONUSPOINTSSYSTEM(FixtureHistory::setBonusPointsSystem, CustomDeserialisers::getIntegerProperty),
		NETTRANSFERS(FixtureHistory::setNetTransfers, CustomDeserialisers::getIntegerProperty),
		VALUE(FixtureHistory::setValue, CustomDeserialisers::getIntegerProperty),
		POINTS(FixtureHistory::setPoints, CustomDeserialisers::getIntegerProperty);

		public BiConsumer<JsonNode, FixtureHistory> doUnto;

//		private DoUntoFixtureHistory(BiConsumer<JsonNode, FixtureHistory> doUnto) {
//			this.doUnto = doUnto;
//		}

		private <T> DoUntoFixtureHistory(BiConsumer<FixtureHistory,T> consumer, ExtractFromJson<T> extract) {
			this.doUnto = (j, f) -> doUnto(this.ordinal(), j, o -> consumer.accept(f,o), extract);
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
