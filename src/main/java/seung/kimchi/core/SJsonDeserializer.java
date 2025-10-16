package seung.kimchi.core;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;
import seung.kimchi.core.types.SLinkedHashMap;

@Slf4j
public class SJsonDeserializer extends JsonDeserializer<Object> {

	private static final Pattern _ISO_DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?(Z|[+-]\\d{2}:\\d{2})$");
	
	@Override
	public Object deserialize(
			JsonParser p
			, DeserializationContext ctxt
			) throws IOException, JacksonException {
		
		switch(p.currentToken()) {
			case VALUE_STRING:
				String value = p.getValueAsString();
				if(_ISO_DATE_PATTERN.matcher(value).matches()) {
					return ZonedDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME).toInstant().toEpochMilli();
				}
				return value;
			case VALUE_NUMBER_INT:
				return p.getLongValue();
			case VALUE_NUMBER_FLOAT:
				return p.getDecimalValue();
			case VALUE_TRUE:
				return true;
			case VALUE_FALSE:
				return false;
			case VALUE_NULL:
				return null;
			case START_OBJECT:
				return map(p, ctxt);
			case START_ARRAY:
				return arr(p, ctxt);
			default:
				return ctxt.readValue(p, Object.class);
		}
		
	}
	
	private SLinkedHashMap map(
			JsonParser p
			, DeserializationContext ctxt
			) throws JacksonException, IOException {
		SLinkedHashMap map = new SLinkedHashMap();
		while(p.nextToken() != JsonToken.END_OBJECT) {
			String name = p.currentName();
			p.nextToken();
			map.add(name, deserialize(p, ctxt));
			
		}
		return map;
	}
	
	private List<Object> arr(
			JsonParser p
			, DeserializationContext ctxt
			) throws JacksonException, IOException {
		List<Object> arr = new ArrayList<>();
		while(p.nextToken() != JsonToken.END_ARRAY) {
			arr.add(deserialize(p, ctxt));
		}
		return arr;
	}
	
}
