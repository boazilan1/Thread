package gameoflife;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class GameOfLife {
	
	private static final List<int[]> CLASSIC_POSSIBLE_NEIGHBORS = new ArrayList<int[]>();
	static {
		CLASSIC_POSSIBLE_NEIGHBORS.add(new int[] { 1, 1 });
		CLASSIC_POSSIBLE_NEIGHBORS.add(new int[] { 1, 0 });
		CLASSIC_POSSIBLE_NEIGHBORS.add(new int[] { 1, -1 });
		CLASSIC_POSSIBLE_NEIGHBORS.add(new int[] { -1, 1 });
		CLASSIC_POSSIBLE_NEIGHBORS.add(new int[] { -1, 0 });
		CLASSIC_POSSIBLE_NEIGHBORS.add(new int[] { -1, -1 });
		CLASSIC_POSSIBLE_NEIGHBORS.add(new int[] { 0, 1 });
		CLASSIC_POSSIBLE_NEIGHBORS.add(new int[] { 0, -1 });
	}

	private static final BiPredicate<Integer, Boolean> CLASSIC_RULE = (numOfNeighbor, imAlive) -> {

		if (imAlive) {
			return (numOfNeighbor == 2 || numOfNeighbor == 3);
		} else {
			return (numOfNeighbor == 3);
		}
	};

	private int m_width;
	private int m_height;
	private int m_frameNum = 0;
	private int m_frameNumMax;
	private int m_numberOfThreads;

	private Grid<Boolean> m_grid;
	private Grid<Boolean> m_nextGrid;

	private GameRules<Boolean> m_gameRule;

	private final CyclicBarrier m_barrier;
	private boolean m_isMoreFrameToCraete = true;

	public GameOfLife() {
		this(20, 800, 900, 2);

	}

	public GameOfLife(int frameNumMax, int width, int height, int numberOfThreads) {
		m_frameNumMax = frameNumMax;
		m_width = width;
		m_height = height;
		m_numberOfThreads = numberOfThreads;

		FrameWriter<Boolean> frameWriter = new FrameWriter<Boolean>(Paths.get("imeges"));
		
		m_grid = new ClasicGrid<Boolean>(m_height, m_width);
		m_nextGrid = new ClasicGrid<Boolean>(m_height, m_width);

		m_gameRule = new ClassicLifeRules<Boolean>(CLASSIC_POSSIBLE_NEIGHBORS, CLASSIC_RULE);

		m_barrier = new CyclicBarrier(m_numberOfThreads, () -> {
			Grid<Boolean> tempGrid = m_grid;
			m_grid = m_nextGrid;
			m_nextGrid = tempGrid;
			frameWriter.saveFrame(m_grid, m_frameNum++);
			//addFrame();
			if (m_frameNum == m_frameNumMax) {
				m_isMoreFrameToCraete = false;
			}
		});

		randomGreed();
//		addFrame();
	}

	private void randomGreed() {
		for (int i = 0; i < m_height; i++) {
			for (int j = 0; j < m_width; j++) {
				boolean b = ThreadLocalRandom.current().nextInt(0, 2) == 1;

				m_grid.set(i, j, b);// [i][j] = ThreadLocalRandom.current().nextInt(0, 2);
			}
		}

	}

	public void makeFrames() {
		nextFrameTraed();
	}

	class ChingePartOfImg implements Runnable {
		int m_start;
		int m_end;

		ChingePartOfImg(int start, int end) {
			m_start = start;
			m_end = end;
		}

		public void run() {
			while (isMoreFrameToCraete()) {
				partOFNextFrame(m_start, m_end);

				try {
					m_barrier.await();
				} catch (InterruptedException ex) {
					return;
				} catch (BrokenBarrierException ex) {
					return;
				}
			}
		}
	}

	private void partOFNextFrame(int start, int end) {
		for (int i = start; i < end; i++) {
			for (int j = 0; j < m_width; j++) {
				m_nextGrid.set(i, j, m_gameRule.isNextAlive(i, j, m_grid));
			}
		}
	}

	private void nextFrameTraed() {
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < m_numberOfThreads; i++) {

			int start = (i) * (m_height / m_numberOfThreads);
			int end = (i + 1) * (m_height / m_numberOfThreads);
			end = (i == m_numberOfThreads - 1) ? m_height : end;
			//System.out.println(start + " " + end);

			threads.add(new Thread(new ChingePartOfImg(start, end)));
			threads.get(i).start();

		}
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// ChingePartOfImg mainThredTask =new ChingePartOfImg((m_numberOfThreads) *
		// (m_numberOfThreads / m_height), (i + 1) * (m_numberOfThreads / m_height))
	}

	private boolean isMoreFrameToCraete() {
		return m_isMoreFrameToCraete;
	}

	private void nextFrame() {
		for (int i = 0; i < m_height; i++) {
			for (int j = 0; j < m_width; j++) {
				m_nextGrid.set(i, j, m_gameRule.isNextAlive(i, j, m_grid));
			}
		}
		m_grid = m_nextGrid;
	}

//	private boolean isNextlive(int i, int j) {
//		int numOfNabeur = numOfNabeurs(i, j);
//		if (m_grid.get(i, j)) {
//			return (numOfNabeur == 2 || numOfNabeur == 3);
//		} else {
//			return (numOfNabeur == 3);
//		}
//	}

//	private int numOfNabeurs(int height, int width) {
//
//		int numOfNabeurs = 0;
//		if (valid(height + 1, width) && m_grid.get(height + 1, width)) {
//			numOfNabeurs++;
//		}
//		if (valid(height + 1, width + 1) && m_grid.get(height + 1, width + 1)) {
//			numOfNabeurs++;
//		}
//		if (valid(height, width + 1) && m_grid.get(height, width + 1)) {
//			numOfNabeurs++;
//		}
//		if (valid(height - 1, width + 1) && m_grid.get(height - 1, width + 1)) {
//			numOfNabeurs++;
//		}
//		if (valid(height + 1, width - 1) && m_grid.get(height + 1, width - 1)) {
//			numOfNabeurs++;
//		}
//		if (valid(height - 1, width - 1) && m_grid.get(height - 1, width - 1)) {
//			numOfNabeurs++;
//		}
//		if (valid(height - 1, width) && m_grid.get(height - 1, width)) {
//			numOfNabeurs++;
//		}
//		if (valid(height, width - 1) && m_grid.get(height, width - 1)) {
//			numOfNabeurs++;
//		}
//		return numOfNabeurs;
//	}

//	private boolean valid(int height, int width) {
//		if (0 <= height && height < m_height && 0 <= width && width < m_width) {
//			return true;
//		}
//		return false;
//	}
//
//	private void addFrame() {
//
//		try {
//			try (BufferedWriter writer = Files.newBufferedWriter(
//					Paths.get("imeges/frame" + String.format("%03d", m_frameNum++) + ".pbm"), Charset.forName("UTF-8"))) {
//				writer.write("P1" + "\n");
//				writer.write(String.valueOf(m_height) + " " + String.valueOf(m_width) + "\n");
//				for (int i = 0; i < m_height; i++) {
//					for (int j = 0; j < m_width; j++) {
//						String isAlive = m_grid.get(i, j) ? "1" : "0";
//						writer.write(isAlive + " ");
//					}
//					writer.write("\n");
//				}
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
