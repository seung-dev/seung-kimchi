package seung.kimchi;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import seung.kimchi.core.types.SException;
import seung.kimchi.core.types.SLinkedHashMap;

public class SMath {

	/**
	 * operators: +, -, *, /, %, ^, (, )
	 * functions: sqrt(x), abs(x), ceil(x), floor(x), round(x), signum(x), random(x)
	 *            sin(x), cos(x), tan(x), asin(x), acos(x), atan(x), sinh(x), cosh(x), tanh(x)
	 *            log(x), log10(x), exp(x) = e^x
	 * constants: pi, e
	 * 
	 * expression: ${a}+${b}+${c}
	 */
	public static double evaluate(
			String expression
			, Set<String> variables
			, Map<String, Double> values
			, List<Function> functions
			) {
		
		return new ExpressionBuilder(expression)
				.variables(variables)
				.functions(functions)
				.build()
				.setVariables(values)
				.evaluate()
				;
	}// end of evaluate
	
	public static String expression(
			String formula
			) {
		return formula.replaceAll("\\$\\{([^}]+)}", "$1");
	}// end of variables
	
	public static Set<String> variables(
			String formula
			) {
		Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");
		Matcher matcher = pattern.matcher(formula);
		Set<String> variables = new HashSet<>();
		while(matcher.find()) {
			variables.add(matcher.group(1));
		}// end of while
		return variables;
	}// end of variables
	
	public static double evaluate(
			String formula
			, SLinkedHashMap values
			) throws SException {
		
		String expression = expression(formula);
		
		Set<String> variables = variables(formula);
		
		Map<String, Double> _values = new HashMap<>();
		
		for(String k : variables) {
			_values.put(k, values.get_double(k));
		}
		
		return evaluate(
				expression
				, variables
				, _values
				, Collections.emptyList()
				);
	}// end of evaluate
	
	/**
	 * e.g. expression: sum(a,b,c,d)
	 *      variables: [a,b,c,d]
	 *      values: {a:1,b:1,c:1,d:1}
	 *      item_size: 4
	 */
	public static double avg(
			String expression
			, String[] variables
			, Map<String, Double> values
			, int item_size
			) {
		
		Function avg = new Function("avg", item_size) {
			@Override
			public double apply(double... args) {
				double sum = 0;
				for (double v : args) {
					sum += v;
				}
				return sum / args.length;
			}
		};
		
		return new ExpressionBuilder(expression)
				.variables(variables)
				.function(avg)
				.build()
				.setVariables(values)
				.evaluate()
				;
	}// end of evaluate
	
}
