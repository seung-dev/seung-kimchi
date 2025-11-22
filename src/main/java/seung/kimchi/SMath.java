package seung.kimchi;

import java.util.Map;
import java.util.Set;

import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

public class SMath {

	/**
	 * operators: +, -, *, /, %, ^, (, )
	 * functions: sqrt(x), abs(x), ceil(x), floor(x), round(x), signum(x), random(x)
	 *            sin(x), cos(x), tan(x), asin(x), acos(x), atan(x), sinh(x), cosh(x), tanh(x)
	 *            log(x), log10(x), exp(x) = e^x
	 * constants: pi, e
	 */
	public static double evaluate(
			String expression
			, String[] variables
			, Map<String, Double> values
			) {
		
		return new ExpressionBuilder(expression)
				.variables(variables)
				.build()
				.setVariables(values)
				.evaluate()
				;
	}// end of evaluate
	
	public static double evaluate(
			String expression
			, Set<String> variables
			, Map<String, Double> values
			) {
		
		return new ExpressionBuilder(expression)
				.variables(variables)
				.build()
				.setVariables(values)
				.evaluate()
				;
	}// end of evaluate
	
	public static double avg(
			String expression
			, Set<String> variables
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
