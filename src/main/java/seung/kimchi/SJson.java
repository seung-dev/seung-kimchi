package seung.kimchi;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;

import seung.kimchi.types.SLinkedHashMap;

public class SJson {

	public static String stringify(
			Object data
			, boolean is_pretty
			) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
//			@Override
//			public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//				jsonGenerator.writeFieldName("");
//			}
//		});
		return objectMapper
				.setSerializationInclusion(Include.ALWAYS)
				.setAccessorNaming(new DefaultAccessorNamingStrategy.Provider().withGetterPrefix("").withSetterPrefix(""))
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
				.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
				.configure(SerializationFeature.INDENT_OUTPUT, is_pretty)
				.writeValueAsString(data)
				;
	}// end of stringify
	
	public static <T> T parse(String data, Class<T> type) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper
				.registerModule(
						new SimpleModule("seung", Version.unknownVersion()).addAbstractTypeMapping(Map.class, SLinkedHashMap.class)
						)
				.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
				.readValue(data, type)
				;
	}// end of parse
	
	public static <T> T parse(String data, TypeReference<T> type) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper
				.registerModule(
						new SimpleModule("seung", Version.unknownVersion()).addAbstractTypeMapping(Map.class, SLinkedHashMap.class)
						)
				.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
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
