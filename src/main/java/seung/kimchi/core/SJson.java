package seung.kimchi.core;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature;
import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import seung.kimchi.core.types.SException;
import seung.kimchi.core.types.SLinkedHashMap;

/**
 * JSON 처리와 관련된 기능들을 제공합니다.
 * 
 * @author seung
 * @since 0.0.1
 */
public class SJson {

	/**
	 * 기본 ObjectMapper를 생성합니다.
	 * 
	 * <p>디폴트 객체:</p>
	 * <p>- {@link SLinkedHashMap}</p>
	 * <p>비활성:</p>
	 * <p>- {@link MapperFeature#AUTO_DETECT_CREATORS}</p>
	 * <p>- {@link MapperFeature#AUTO_DETECT_FIELDS}</p>
	 * <p>- {@link MapperFeature#AUTO_DETECT_GETTERS}</p>
	 * <p>- {@link MapperFeature#AUTO_DETECT_IS_GETTERS}</p>
	 * <p>- {@link MapperFeature#AUTO_DETECT_SETTERS}</p>
	 * <p>설정:</p>
	 * <p>- {@link SerializationFeature#FAIL_ON_EMPTY_BEANS} = {@code false}</p>
	 * <p>- {@link SerializationFeature#WRITE_ENUMS_USING_TO_STRING} = {@code true}</p>
	 * <p>- {@link DeserializationFeature#FAIL_ON_NULL_FOR_PRIMITIVES} = {@code false}</p>
	 * <p>- {@link DeserializationFeature#USE_BIG_DECIMAL_FOR_FLOATS} = {@code true}</p>
	 * <p>- {@link JsonGenerator.Feature#WRITE_BIGDECIMAL_AS_PLAIN} = {@code true}</p>
	 * <p>- {@link JsonNodeFeature#STRIP_TRAILING_BIGDECIMAL_ZEROES} = {@code true}</p>
	 * 
	 * @return {@link ObjectMapper}
	 * @since 0.0.1
	 */
	public static ObjectMapper object_mapper() {
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
				.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
				.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
				.configure(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES, true)
				.build();
	}// end of object_mapper
	
	/**
	 * 요청값을 JSON 포멧 텍스트로 변환합니다.
	 * 
	 * @param value  요청값
	 * @param indent 들여쓰기 여부
	 * @return JSON 포멧 텍스트
	 * @throws SException {@link JsonProcessingException}
	 * @since 0.0.1
	 * @see #object_mapper()
	 */
	public static String stringify(
			final Object value
			, final boolean indent
			) throws SException {
		try {
			return object_mapper()
					.configure(SerializationFeature.INDENT_OUTPUT, indent)
					.writeValueAsString(value)
					;
		} catch (JsonProcessingException e) {
			throw new SException(e, "[JsonProcessingException] Invalid argument.");
		}
	}// end of stringify
	
	/**
	 * JSON 포멧 텍스트를 요청한 타입으로 변환합니다.
	 * 
	 * @param value 요청값
	 * @param type  요청타입
	 * @return 요청타입 객체
	 * @throws SException {@link JsonMappingException}, {@link JsonProcessingException}
	 * @since 0.0.1
	 * @see #object_mapper()
	 */
	public static <T> T parse(
			final String value
			, final TypeReference<T> type
			) throws SException {
		try {
			return object_mapper()
					.readValue(value, type)
					;
		} catch (JsonMappingException e) {
			throw new SException(e, "[JsonMappingException] Invalid argument.");
		} catch (JsonProcessingException e) {
			throw new SException(e, "[JsonProcessingException] Invalid argument.");
		}
	}// end of parse
	
	/**
	 * JSON 포멧 텍스트를 {@link SLinkedHashMap}으로 변환합니다.
	 * 
	 * @param value 요청값
	 * @return {@link SLinkedHashMap}
	 * @throws SException {@link JsonMappingException}, {@link JsonProcessingException}
	 * @since 0.0.1
	 * @see #parse(String, TypeReference)
	 */
	public static SLinkedHashMap to_slinkedhashmap(
			final String value
			) throws SException {
		return parse(value, new TypeReference<SLinkedHashMap>() {});
	}// end of to_slinkedhashmap
	
	/**
	 * JSON 포멧 텍스트를 {@link SLinkedHashMap} 리스트로 변환합니다.
	 * 
	 * @param value 요청값
	 * @return {@link SLinkedHashMap} 리스트
	 * @throws SException {@link JsonMappingException}, {@link JsonProcessingException}
	 * @since 0.0.1
	 * @see #parse(String, TypeReference)
	 */
	public static List<SLinkedHashMap> to_list_slinkedhashmap(
			final String value
			) throws SException {
		return parse(value, new TypeReference<List<SLinkedHashMap>>() {});
	}// end of to_list_slinkedhashmap
	
}
