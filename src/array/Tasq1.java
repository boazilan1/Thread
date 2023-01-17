package array;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoublePredicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import Compution.DoublsComputation;

public class Tasq1 {
	private static final Logger s_logger = Logger.getLogger(Tasq1.class.getCanonicalName());

	public static void q1(int size, int min, int max, DoublePredicate condition) {
		
		double[] arrDuoble = randomDuobleArr(size, min, max);
		q1thread(arrDuoble,condition);
		double result = UseCondition.sum(arrDuoble, condition);
		System.out.println(result);
		
	}
	public static void q1thread(double[] arrDuoble, DoublePredicate condition) {
		
		double[] anser = { 0, 0 };
		Thread threadHelfOne = new Thread(() ->anser[0]= Arrays.stream(arrDuoble).skip(arrDuoble.length/2).filter(condition).sum());
		Thread threadHelfToo = new Thread(() ->anser[1]= Arrays.stream(arrDuoble).limit(arrDuoble.length/2).filter(condition).sum());
				
		threadHelfOne.start();
		threadHelfToo.start();

		try {
			threadHelfOne.join();
			threadHelfToo.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		double threadresult= Arrays.stream(anser).sum();
		System.out.println(threadresult);
	}
	
	public static double[] randomDuobleArr(int size, int min, int max) {
		double[] arrReturn = new double[size];
		for (int i = 0; i < size; ++i) {
			arrReturn[i] = ThreadLocalRandom.current().nextDouble(min, max);
		}
		return arrReturn;
	}

	public static void q3(double[] arrDuoble, DoublsComputation computation, DoublePredicate condition) {
		DoubleStream stream = Arrays.stream(arrDuoble).filter(condition);
				
		arrDuoble = stream.toArray();
		double sum = 0;
		boolean firstFlag = true;
		for (double d : arrDuoble) {			
			if(firstFlag) {
				sum=d;
				firstFlag=false;
			}else {
				sum = computation.comput(sum, d);
			}			
		}
		System.out.println(sum);
	}

}
