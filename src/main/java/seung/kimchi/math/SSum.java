package seung.kimchi.math;

import net.objecthunter.exp4j.function.Function;

public class SSum extends Function {

	public SSum() {
		super("sum", 10);
	}
	
	@Override
	public double apply(double... args) {
		double sum = 0;
		for (double v : args) {
			sum += v;
		}
		return sum;
	}// end of apply
	
}
