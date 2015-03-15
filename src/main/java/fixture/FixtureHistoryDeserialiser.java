package fixture;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import static fixture.CustomDeserialisers.toFixtureHistory;

public class FixtureHistoryDeserialiser extends JsonDeserializer<FixtureHistory>{

	@Override
	public FixtureHistory deserialize(JsonParser jp,
			DeserializationContext dc) throws IOException,
			JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		return toFixtureHistory(node);
	}

}
