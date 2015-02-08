package main.java.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import main.fixture.FixtureHistory;
import static main.java.utilities.CommonValues.*;

public final class CustomDeserialisers {

	private static DateFormat simpleFormat = new SimpleDateFormat();

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
				CustomDeserialisers::getDateProperty)),
		GAMESEQUENCE((j, f) -> doUnto(0, j, f::setGameSequence,
				CustomDeserialisers::getIntegerProperty)),
		RESULT((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		MINUTESPLAYED((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		GOALSCORED((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		ASSISTS((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		CLEANSHEETS((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		OWNGOALS((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		PENALTIESSAVED((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		YELLOWCARDS((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		REDCARDS((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		SAVES((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		BONUSPOINTS((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		EASPORTSPPI((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		BONUSPOINTSSYSTEM((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		NETTRANSFERS((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty)),
		VALUE((j, f) -> doUnto(0, j, f::setResult,
				CustomDeserialisers::getStringProperty));

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
