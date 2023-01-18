package gameoflife;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class ClasicGrid<T> implements Grid<T> {
	private int m_width;
	private int m_height;
	private List<T> m_tList;
	private T[] m_tArr;

	public ClasicGrid(int height , int width) {
		m_width = width;
		m_height = height;
		System.out.println(m_width*m_height);
		m_tArr = (T[]) new Object[m_height*m_width];
				
		//inslizeArr(()->(T)(ThreadLocalRandom.current().nextInt(0, 2)==1));
	}

	
	@Override
	public T get(int i, int j) {
		return m_tArr[i * m_width + j];

	}

	@Override
	public void set(int i, int j, T t) {
		m_tArr[i * m_width + j] = t;
	}


	@Override
	public int getHeight() {
		
		return m_height;
	}


	@Override
	public int getWidth() {
		return m_width;
	}

}
