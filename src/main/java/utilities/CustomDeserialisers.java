package utilities;

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

import fixture.FixtureHistory;
import static utilities.CommonValues.*;

public final class CustomDeserialisers {

	private static DateFormat simpleFormat = new SimpleDateFormat(
			"dd MMM hh:mm");

	public static final FixtureHistory toFixtureHistory(JsonNode jsonNode) {
		FixtureHistory fixtureHistory = new FixtureHistory();
		DoUntoFixtureHistory.valueList().forEach(
				d -> d.doUnto.accept(jsonNode, fixtureHistory));
		return fixtureHistory;
	}

	private interface ExtractFromJson<T> {
		T extract(JsonNode jsonNode, Integer propertyValue);
	}

	private static Date getDateProperty(JsonNode jsonNode,
			Integer propertyNumber) {
		try {
			return simpleFormat.parse(jsonNode.get(propertyNumber).textValue());
		} catch (ParseException e) {
			return NOT_SET_DATE;
		}
	}

	private static Date getFixureDateProperty(JsonNode jsonNode,
			Integer propertyNumber) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDateProperty(jsonNode, propertyNumber));
		Integer year = 2015;
		if (calendar.get(Calendar.MONTH) > Calendar.JULY) {
			year = 2014;
		}
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}

	private static Integer getIntegerProperty(JsonNode jsonNode,
			Integer propertyNumber) {
		return jsonNode.get(propertyNumber).asInt();
	}

	private static String getStringProperty(JsonNode jsonNode,
			Integer propertyNumber) {
		return jsonNode.get(propertyNumber).textValue();
	}

	static enum DoUntoFixtureHistory {
		FIXTUREDATE((j, f) -> doUnto(0, j, f::setFixtureDate,
				CustomDeserialisers::getFixureDateProperty)),
		GAMESEQUENCE((j, f) -> doUnto(1, j, f::setGameSequence,
				CustomDeserialisers::getIntegerProperty)),
		RESULT((j, f) -> doUnto(2, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		MINUTESPLAYED((j, f) -> doUnto(3, j, f::setMinutesPlayed,
				CustomDeserialisers::getIntegerProperty)),
		GOALSCORED((j, f) -> doUnto(4, j, f::setGoalScored,
				CustomDeserialisers::getIntegerProperty)),
		ASSISTS((j, f) -> doUnto(5, j, f::setAssists,
				CustomDeserialisers::getIntegerProperty)),
		CLEANSHEETS((j, f) -> doUnto(6, j, f::setCleanSheets,
				CustomDeserialisers::getIntegerProperty)),
		OWNGOALS((j, f) -> doUnto(7, j, f::setOwnGoals,
				CustomDeserialisers::getIntegerProperty)),
		PENALTIESSAVED((j, f) -> doUnto(8, j, f::setPenaltiesSaved,
				CustomDeserialisers::getIntegerProperty)),
		YELLOWCARDS((j, f) -> doUnto(9, j, f::setYellowCards,
				CustomDeserialisers::getIntegerProperty)),
		REDCARDS((j, f) -> doUnto(10, j, f::setRedCards,
				CustomDeserialisers::getIntegerProperty)),
		SAVES((j, f) -> doUnto(11, j, f::setSaves,
				CustomDeserialisers::getIntegerProperty)),
		BONUSPOINTS((j, f) -> doUnto(12, j, f::setBonusPoints,
				CustomDeserialisers::getIntegerProperty)),
		EASPORTSPPI((j, f) -> doUnto(13, j, f::setEaSportsPPI,
				CustomDeserialisers::getIntegerProperty)),
		BONUSPOINTSSYSTEM((j, f) -> doUnto(14, j, f::setBonusPointsSystem,
				CustomDeserialisers::getIntegerProperty)),
		NETTRANSFERS((j, f) -> doUnto(15, j, f::setNetTransfers,
				CustomDeserialisers::getIntegerProperty)),
		VALUE((j, f) -> doUnto(16, j, f::setValue,
				CustomDeserialisers::getIntegerProperty));

		public BiConsumer<JsonNode, FixtureHistory> doUnto;

		private DoUntoFixtureHistory(BiConsumer<JsonNode, FixtureHistory> doUnto) {
			this.doUnto = doUnto;
		}

		private static <T> void doUnto(Integer ordinal, JsonNode jsonNode,
				Consumer<T> setter, ExtractFromJson<T> extract) {
			setter.accept(extract.extract(jsonNode, ordinal));
		}

		static List<DoUntoFixtureHistory> valueList() {
			return Lists.newArrayList(values());
		}

	}

}
