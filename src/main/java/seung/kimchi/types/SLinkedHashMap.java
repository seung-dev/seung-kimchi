package seung.kimchi.types;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import seung.kimchi.SJson;

@SuppressWarnings("rawtypes")
public class SLinkedHashMap extends LinkedHashMap {

	private static final long serialVersionUID = 1L;
	
	private static final List<String> _S_EXCLUDE_REFLECTION_FIELDS = Arrays.asList("log", "serialVersionUID");
	
	public SLinkedHashMap() {
	}
	public SLinkedHashMap(Map data) {
		merge(data);
	}
	public SLinkedHashMap(String data) throws JsonMappingException, JsonProcessingException {
		merge(data);
	}
	public SLinkedHashMap(Object data) throws IllegalArgumentException, IllegalAccessException {
		merge(data);
	}
	public SLinkedHashMap(List data) {
		merge(data);
	}
	
	public String stringify(boolean is_pretty) throws JsonProcessingException {
		return SJson.stringify(this, is_pretty);
	}
	public String stringify() throws JsonProcessingException {
		return stringify(false);
	}
	
	public boolean is_null(Object key) {
		return get(key) == null;
	}
	
	public boolean is_blank(Object key) {
		return "".equals(get(key));
	}
	
	public boolean is_empty(Object key) {
		return is_null(key) || is_blank(key);
	}
	
	public boolean is_equal(Object key, Object object) {
		return get(key) == object;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> keys() {
		return new ArrayList<String>(this.keySet());
	}
	
	public Object get(Object key, Object default_value) {
		if(is_null(key)) {
			return default_value;
		}
		return get(key);
	}
	
	public String get_text(Object key, String default_value) {
		if(is_null(key)) {
			return default_value;
		}
		Object value = get(key);
		if(value instanceof String) {
			return "" + value;
		}
		if(value instanceof String[]) {
			String[] items = (String[]) value;
			return items[0];
		}
		if(value instanceof List) {
			List items = (List) value;
			if(items.size() == 0) {
				return default_value;
			}
			return "" + items.get(0);
		}
		return "" + get(key);
	}
	public String get_text(Object key) {
		return get_text(key, null);
	}
	
	public Boolean get_bool(Object key, Boolean default_value) {
		if(is_empty(key)) {
			return default_value;
		}
		Object value = get(key);
		if(value instanceof Boolean) {
			return (boolean) value;
		}
		switch (get_text(key, "")) {
			case "1":
			case "true":
				return true;
			case "0":
			case "false":
				return false;
			default:
				break;
		}
		throw new ClassCastException("The value cannot be cast to Boolean.");
	}
	public Boolean get_bool(Object key) {
		return get_bool(key, null);
	}
	
	public Integer get_int(Object key, Integer default_value) {
		if(is_empty(key)) {
			return default_value;
		}
		String value = get_text(key);
		if(!Pattern.matches("[0-9+-]+", value)) {
			throw new NumberFormatException(String.format("Failed to cast to Integer. value=%s", value));
		}
		return Integer.parseInt(value);
	}
	public Integer get_int(Object key) {
		return get_int(key, null);
	}
	
	public Long get_long(Object key, Long default_value) {
		if(is_empty(key)) {
			return default_value;
		}
		String value = get_text(key);
		if(!Pattern.matches("[0-9+-]+", value)) {
			throw new NumberFormatException(String.format("Failed to cast to Long. value=%s", value));
		}
		return Long.parseLong(value);
	}
	public Long get_long(Object key) {
		return get_long(key, null);
	}
	
	public BigInteger get_bigint(Object key, BigInteger default_value) {
		if(is_empty(key)) {
			return default_value;
		}
		String value = get_text(key);
		if(!Pattern.matches("[0-9+-]+", value)) {
			throw new NumberFormatException(String.format("Failed to cast to BigInteger. value=%s", value));
		}
		return BigInteger.valueOf(get_long(key));
	}
	public BigInteger get_bigint(Object key) {
		return get_bigint(key, null);
	}
	
	public Double get_double(Object key, Double default_value) {
		if(is_empty(key)) {
			return default_value;
		}
		String value = get_text(key);
		if(!Pattern.matches("[0-9+-.]+", value)) {
			throw new NumberFormatException(String.format("Failed to cast to Double. value=%s", value));
		}
		return Double.parseDouble(value);
	}
	public Double get_double(Object key) {
		return get_double(key, null);
	}
	
	public BigDecimal get_decimal(Object key, BigDecimal default_value) {
		if(is_empty(key)) {
			return default_value;
		}
		String value = get_text(key);
		if(!Pattern.matches("[0-9+-.]+", value)) {
			throw new NumberFormatException(String.format("Failed to cast to BigInteger. value=%s", value));
		}
		return BigDecimal.valueOf(get_double(key));
	}
	public BigDecimal get_decimal(Object key) {
		return get_decimal(key, null);
	}
	
	public Map get_map(Object key) {
		if(is_null(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof Map) {
			return (Map) value;
		}
		throw new ClassCastException("Failed to cast to Map.");
	}
	
	public SLinkedHashMap get_slinkedhashmap(Object key) throws IllegalArgumentException, IllegalAccessException {
		if(is_null(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof SLinkedHashMap) {
			return (SLinkedHashMap) value;
		}
		return new SLinkedHashMap(value);
	}// end of get_slinkedhashmap
	
	public List get_list(Object key) {
		if(is_null(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof List) {
			return (List) value;
		}
		throw new ClassCastException("Failed to cast to List.");
	}// end of get_list
	
	@SuppressWarnings("unchecked")
	public List<SLinkedHashMap> get_list_slinkedhashmap(Object key) {
		if(is_null(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof List) {
			return (List<SLinkedHashMap>) value;
		}
		throw new ClassCastException("Failed to cast to List<SLinkedHashMap>.");
	}// end of get_list_slinkedhashmap
	
	@SuppressWarnings("unchecked")
	public String[] get_array_string(Object key) {
		if(is_empty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof String) {
			String[] array = {
					"" + value
			};
			return array;
		}
		if(value instanceof String[]) {
			return (String[]) value;
		}
		if(value instanceof List) {
			List<String> items = (List) value;
			return items.stream().toArray(String[]::new);
		}
		return null;
	}// end of get_array_string
	
	@SuppressWarnings("unchecked")
	public int[] get_array_int(Object key) {
		if(is_empty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof Integer) {
			int[] array = {
					(int) value
			};
			return array;
		}
		if(value instanceof int[]) {
			return (int[]) value;
		}
		if(value instanceof List) {
			List<Integer> values = (List) value;
			return values.stream().mapToInt(Integer::intValue).toArray();
		}
		return null;
	}// end of get_array_int
	
	@SuppressWarnings("unchecked")
	public long[] get_array_long(Object key) {
		if(is_empty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof Integer) {
			long[] array = {
					(long) value
			};
			return array;
		}
		if(value instanceof long[]) {
			return (long[]) value;
		}
		if(value instanceof List) {
			List<Long> values = (List) value;
			return values.stream().mapToLong(Long::longValue).toArray();
		}
		return null;
	}// end of get_array_long
	
	@SuppressWarnings("unchecked")
	public double[] get_array_double(Object key) {
		if(is_empty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof double[]) {
			return (double[]) value;
		}
		if(value instanceof Double) {
			double[] array = {
					(double) value
			};
			return array;
		}
		if(value instanceof List) {
			List<Double> values = (List) value;
			return values.stream().mapToDouble(Double::doubleValue).toArray();
		}
		return null;
	}// end of get_array_double
	
	@SuppressWarnings("unchecked")
	public List<String> get_list_string(Object key) {
		if(is_empty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof String) {
			return Arrays.asList("" + value);
		}
		if(value instanceof String[]) {
			return Arrays.asList((String[]) value);
		}
		if(value instanceof List) {
			return (List<String>) value;
		}
		return null;
	}// end of get_list_string
	
	public byte[] get_byte_array(Object key) {
		if(is_empty(key)) {
			return null;
		}
		Object value = get(key);
		if(value instanceof byte[]) {
			return (byte[]) value;
		}
		return null;
	}// end of get_byte_array
	
	@SuppressWarnings("unchecked")
	public SLinkedHashMap add(Object key, Object value) {
		if(key != null) {
			super.put(key, value);
		}
		return this;
	}// end of add
	
	@SuppressWarnings("unchecked")
	public SLinkedHashMap merge(Map data) {
		if(data != null) {
			super.putAll(data);
		}
		return this;
	}// end of merge
	@SuppressWarnings("unchecked")
	public SLinkedHashMap merge(String data) throws JsonMappingException, JsonProcessingException {
		this.putAll(SJson.parse(data));
		return this;
	}// end of merge
	@SuppressWarnings("unchecked")
	public SLinkedHashMap merge(Object data) throws IllegalArgumentException, IllegalAccessException {
		String field_name = "";
		for(Field field : data.getClass().getSuperclass().getDeclaredFields()) {
			field.setAccessible(true);
			field_name = field.getName();
			if(_S_EXCLUDE_REFLECTION_FIELDS.contains(field_name)) {
				continue;
			}
			this.put(field_name, field.get(data));
		}
		for(Field field : data.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			field_name = field.getName();
			if(_S_EXCLUDE_REFLECTION_FIELDS.contains(field_name)) {
				continue;
			}
			this.put(field_name, field.get(data));
		}
		return this;
	}// end of merge
	@SuppressWarnings("unchecked")
	public SLinkedHashMap merge(List data) {
		if(data != null) {
			for(int index = 0; index < data.size(); index++) {
				this.put(index, data.get(index));
			}
		}
		return this;
	}// end of merge
	
}
