import java.util.logging.Level;
import java.util.logging.Logger;

import array.Condition;
import array.Tasq1;
import array.UseCondition;

import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntBinaryOperator;

public class Main {
	private static final Logger s_logger = Logger.getLogger(Main.class.getCanonicalName());

	public static void main(String[] args) {
		s_logger.log(Level.INFO, "main thread in main");
		
		tasq1();
		
	}
	
	

	private static void sumArrayTasks() {
		int size = 1_000_001;
		int[] arr = randomIntArr(size);
		int[] anser = { 0, 0 };

		Thread threadHelfOne = new Thread(() -> sumArrPart(arr, 0, size / 2, anser, 0));
		Thread threadHelfToo = new Thread(() -> sumArrPart(arr, size / 2, size, anser, 1));

		threadHelfOne.start();
		threadHelfToo.start();

		try {
			threadHelfOne.join();
			threadHelfToo.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		s_logger.log(Level.INFO, String.valueOf(anser[0] + anser[1]));

		int sum = sumArr(arr);
		s_logger.log(Level.INFO, String.valueOf(sum));
	}

	private static void sumArrPart(int[] arr, int start, int end, int[] anser, int anserIndex) {
		for (; start < end; start++) {
			anser[anserIndex] += arr[start];
		}
	}

	private static int sumArr(int[] arr) {
		int toReturn = 0;
		for (int i : arr) {
			toReturn += i;
		}
		return toReturn;
	}

	private static int[] randomIntArr(int size) {

		int[] arrReturn = new int[size];
		for (int i = 0; i < size; ++i) {
			arrReturn[i] = ThreadLocalRandom.current().nextInt(0, 10);
		}
		return arrReturn;
	}
	private static double[] randomDuobleArr(int size) {

		double[] arrReturn = new double[size];
		for (int i = 0; i < size; ++i) {
			arrReturn[i] = ThreadLocalRandom.current().nextDouble(0,14);
		}
		return arrReturn;
	}
	private static void tasq1() {
		int size =1_000_000;
		int min =0;
		int max = 5;		
		Tasq1.q1(size, 0, 3, (a) -> a < 1);		
	}
}


