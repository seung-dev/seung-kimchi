package seung.kimchi.math;

import net.objecthunter.exp4j.function.Function;

public class SAverage extends Function {

	public SAverage() {
		super("avg", 10);
	}
	
	@Override
	public double apply(double... args) {
		double sum = 0;
		for (double v : args) {
			sum += v;
		}
		return sum / args.length;
	}// end of apply
	
}
