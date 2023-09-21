package seung.kimchi;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import seung.kimchi.types.SLinkedHashMap;

public class SJson {

	public static ObjectMapper object_mapper() {
//		objectMapper.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
//			@Override
//			public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//				jsonGenerator.writeFieldName("");
//			}
//		});
		return JsonMapper.builder()
			.disable(
					MapperFeature.AUTO_DETECT_CREATORS
					, MapperFeature.AUTO_DETECT_FIELDS
					, MapperFeature.AUTO_DETECT_GETTERS
					, MapperFeature.AUTO_DETECT_IS_GETTERS
					, MapperFeature.AUTO_DETECT_SETTERS
					)
			.addModule(
					new SimpleModule("seung", Version.unknownVersion()).addAbstractTypeMapping(Map.class, SLinkedHashMap.class)
					)
			.accessorNaming(new DefaultAccessorNamingStrategy.Provider().withGetterPrefix("").withSetterPrefix(""))
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
			.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
			.build();
	}
	
	public static String stringify(
			Object data
			, boolean is_pretty
			) throws JsonProcessingException {
		return object_mapper()
				.configure(SerializationFeature.INDENT_OUTPUT, is_pretty)
				.writeValueAsString(data)
				;
	}// end of stringify
	
	public static <T> T parse(String data, Class<T> type) throws JsonMappingException, JsonProcessingException {
		return object_mapper()
				.readValue(data, type)
				;
	}// end of parse
	
	public static <T> T parse(String data, TypeReference<T> type) throws JsonMappingException, JsonProcessingException {
		return object_mapper()
				.readValue(data, type)
				;
	}// end of parse
	
	public static SLinkedHashMap to_slinkedhashmap(String data) throws JsonMappingException, JsonProcessingException {
		return parse(data, SLinkedHashMap.class);
	}// end of to_slinkedhashmap
	
	public static List<SLinkedHashMap> to_list_slinkedhashmap(String data) throws JsonMappingException, JsonProcessingException {
		return parse(data, new TypeReference<List<SLinkedHashMap>>() {});
	}// end of to_list_slinkedhashmap
	
}
