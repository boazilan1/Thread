package array;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.DoublePredicate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Compution.DoublsComputation;

import static array.Tasq1.randomDuobleArr;
import static array.Tasq1.*;

class Tasq1Test {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testQ1() {
		int size = 1_000_000;
		int min = 0;
		int max = Integer.MAX_VALUE;

		Tasq1.q1(size, min, max, (a) -> a < 1);
		//q1thread(size, min, max, (a) -> a < 1);
		
		Tasq1.q1(size, Integer.MIN_VALUE, Integer.MAX_VALUE, (a) -> a > 0);
		//q1thread(size, Integer.MIN_VALUE, Integer.MAX_VALUE, (a) -> a > 0);
		
		Tasq1.q1(size, Integer.MIN_VALUE, Integer.MAX_VALUE, (a) -> a < 0);
		//q1thread(size, Integer.MIN_VALUE, Integer.MAX_VALUE, (a) -> a < 0);

	}
	@Test
	void testQ3() {
		int size = 1_000_000;
		int min = 1;
		int max = 2;

		double[] arrDuoble = randomDuobleArr(size, min, max);
		DoublsComputation computation = (firstElement, secendElement) -> 
			{return secendElement !=0 ? (firstElement + secendElement):firstElement ;};
		
		q3(arrDuoble, computation, elment -> elment > 0);
		
		computation = (firstElement, secendElement) -> 
		{return secendElement != 0 ? (firstElement * secendElement):firstElement ;};
		
		q3(arrDuoble, computation, elment -> elment !=0 );
		
	}

}
