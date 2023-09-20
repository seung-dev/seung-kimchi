package seung.kimchi;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
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
	
	public static SLinkedHashMap parse(String data) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {
//			@Override
//			public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//				jsonGenerator.writeFieldName("");
//			}
//		});
		return objectMapper
				.registerModule(
						new SimpleModule("seung", Version.unknownVersion())
						.addAbstractTypeMapping(Map.class, SLinkedHashMap.class)
						)
				.readValue(data, SLinkedHashMap.class)
				;
	}// end of parse
	
}
