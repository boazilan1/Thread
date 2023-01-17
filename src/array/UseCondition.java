package array;

import java.util.Arrays;
import java.util.function.DoublePredicate;

public class UseCondition {
	public static double sum(double[] array , DoublePredicate condition) {
	    return Arrays.stream(array).filter(condition).sum();  // caller.caller(1.0,1.0);
	}
}
