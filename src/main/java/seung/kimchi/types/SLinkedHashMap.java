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

import seung.kimchi.SJson;

@SuppressWarnings("rawtypes")
public class SLinkedHashMap extends LinkedHashMap {

	private static final long serialVersionUID = 8004935297956073280L;
	
	private static final List<String> _S_EXCLUDE_REFLECTION_FIELDS = Arrays.asList("log", "serialVersionUID");
	
	public SLinkedHashMap() {
	}
	public SLinkedHashMap(Map data) {
		merge(data);
	}
	public SLinkedHashMap(String data) throws SException {
		merge(data);
	}
	public SLinkedHashMap(Object data) throws SException {
		merge(data);
	}
	public SLinkedHashMap(List data) {
		merge(data);
	}
	
	public String stringify(
			boolean is_pretty
			) throws SException {
		return SJson.stringify(this, is_pretty);
	}
	public String stringify() throws SException {
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
	
	public Object get(
			Object key
			, Object default_value
			) {
		
		if(is_null(key)) {
			return default_value;
		}
		
		return get(key);
	}
	
	public String get_text(
			Object key
			, String default_value
			) {
		
		if(is_null(key)) {
			return default_value;
		}
		
		Object value = get(key);
		
		if(value instanceof String) {
			return String.valueOf(value);
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
		
		if(value instanceof BigDecimal) {
			return ((BigDecimal) value).toPlainString();
		}
		
		return String.valueOf(value);
	}
	public String get_text(Object key) {
		return get_text(key, null);
	}
	
	public Boolean get_bool(
			Object key
			, Boolean default_value
			) throws SException {
		
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
		
		throw new SException("Something went wrong.");
	}
	public Boolean get_bool(Object key) throws SException {
		return get_bool(key, null);
	}
	
	public Integer get_int(
			Object key
			, Integer default_value
			) throws SException {
		
		if(is_empty(key)) {
			return default_value;
		}
		
		String value = get_text(key);
		
		if(Pattern.matches("[0-9+-]+", value)) {
			return Integer.parseInt(value);
		}
		
		throw new SException("Something went wrong.");
	}
	public Integer get_int(Object key) throws SException {
		return get_int(key, null);
	}
	
	public Long get_long(
			Object key
			, Long default_value
			) throws SException {
		
		if(is_empty(key)) {
			return default_value;
		}
		
		String value = get_text(key);
		
		if(Pattern.matches("[0-9+-]+", value)) {
			return Long.parseLong(value);
		}
		
		throw new SException("Something went wrong.");
	}
	public Long get_long(Object key) throws SException {
		return get_long(key, null);
	}
	
	public Double get_double(
			Object key
			, Double default_value
			) throws SException {
		
		if(is_empty(key)) {
			return default_value;
		}
		
		String value = get_text(key);
		
		if(Pattern.matches("[0-9+-.]+", value)) {
			return Double.parseDouble(value);
		}
		
		throw new SException("Something went wrong.");
	}
	public Double get_double(Object key) throws SException {
		return get_double(key, null);
	}
	
	public BigDecimal get_decimal(
			Object key
			, BigDecimal default_value
			) throws SException {
		
		if(is_empty(key)) {
			return default_value;
		}
		
		String value = get_text(key);
		
		if(Pattern.matches("[0-9+-.]+", value)) {
			return BigDecimal.valueOf(get_double(key));
		}
		
		throw new SException("Something went wrong.");
	}
	public BigDecimal get_decimal(Object key) throws SException {
		return get_decimal(key, null);
	}
	
	public BigInteger get_bigint(
			Object key
			, BigInteger default_value
			) throws SException {
		
		if(is_empty(key)) {
			return default_value;
		}
		
		String value = get_text(key);
		
		if(Pattern.matches("[0-9+-]+", value)) {
			return BigInteger.valueOf(get_long(key));
		}
		
		throw new SException("Something went wrong.");
	}
	public BigInteger get_bigint(Object key) throws SException {
		return get_bigint(key, null);
	}
	
	public BigDecimal get_bigdecimal(
			Object key
			, BigDecimal default_value
			) throws SException {
		
		if(is_empty(key)) {
			return default_value;
		}
		
		String value = get_text(key);
		
		if(Pattern.matches("[0-9+-.]+", value)) {
			return new BigDecimal(value);
		}
		
		throw new SException("Something went wrong.");
	}
	public BigDecimal get_bigdecimal(
			Object key
			, int default_value
			) throws SException {
		return get_bigdecimal(key, new BigDecimal(default_value));
	}
	public BigDecimal get_bigdecimal(Object key) throws SException {
		return get_bigdecimal(key, null);
	}
	
	public Map get_map(
			Object key
			) throws SException {
		
		if(is_null(key)) {
			return null;
		}
		
		Object value = get(key);
		
		if(value instanceof Map) {
			return (Map) value;
		}
		
		throw new SException("Something went wrong.");
	}
	
	public SLinkedHashMap get_slinkedhashmap(
			Object key
			) throws SException {
		
		if(is_null(key)) {
			return null;
		}
		
		Object value = get(key);
		
		if(value instanceof SLinkedHashMap) {
			return (SLinkedHashMap) value;
		}
		
		return new SLinkedHashMap(value);
	}// end of get_slinkedhashmap
	
	public List get_list(
			Object key
			) throws SException {
		
		if(is_null(key)) {
			return null;
		}
		
		Object value = get(key);
		
		if(value instanceof List) {
			return (List) value;
		}
		
		throw new SException("Something went wrong.");
	}// end of get_list
	
	@SuppressWarnings("unchecked")
	public List<SLinkedHashMap> get_list_slinkedhashmap(
			Object key
			) throws SException {
		
		if(is_null(key)) {
			return null;
		}
		
		Object value = get(key);
		
		if(value instanceof List) {
			return (List<SLinkedHashMap>) value;
		}
		
		throw new SException("Something went wrong.");
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
	public long[] get_array_long(
			Object key
			) {
		
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
	public double[] get_array_double(
			Object key
			) {
		
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
	public List<String> get_list_string(
			Object key
			) {
		
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
	
	public byte[] get_byte_array(
			Object key
			) {
		
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
	public SLinkedHashMap add(
			Object key
			, Object value
			) {
		
		if(key != null) {
			super.put(key, value);
		}
		
		return this;
	}// end of add
	
	@SuppressWarnings("unchecked")
	public SLinkedHashMap merge(
			Map data
			) {
		
		if(data != null) {
			super.putAll(data);
		}
		
		return this;
	}// end of merge
	@SuppressWarnings("unchecked")
	public SLinkedHashMap merge(
			String data
			) throws SException {
		this.putAll(SJson.to_slinkedhashmap(data));
		return this;
	}// end of merge
	@SuppressWarnings("unchecked")
	public SLinkedHashMap merge(
			Object data
			) throws SException {
		
		String field_name = "";
		
		try {
			
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
			
		} catch (IllegalArgumentException e) {
			throw new SException(e, "Something went wrong.");
		} catch (IllegalAccessException e) {
			throw new SException(e, "Something went wrong.");
		}
		
		return this;
	}// end of merge
	@SuppressWarnings("unchecked")
	public SLinkedHashMap merge(
			List data
			) {
		
		if(data != null) {
			for(int index = 0; index < data.size(); index++) {
				this.put(index, data.get(index));
			}
		}
		
		return this;
	}// end of merge
	
}
