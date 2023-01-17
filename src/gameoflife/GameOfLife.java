package gameoflife;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class GameOfLife {

	private int m_width = 800;
	private int m_height = 900;
	private int m_frameNum = 0;
	private int m_frameNumMax = 20;
	private int m_numberOfThreads = 2;

	private int[][] m_greed;
	private int[][] m_nextGreed;

	private final CyclicBarrier m_barrier;
	private boolean m_isMoreFrameToCraete = true;

	public GameOfLife() {
		m_greed = new int[m_height][m_width];
		m_nextGreed = new int[m_height][m_width];
		m_barrier = new CyclicBarrier(m_numberOfThreads, () -> {
			m_greed = m_nextGreed;
			addFrame();
			if (m_frameNum == m_frameNumMax) {
				m_isMoreFrameToCraete = false;
			}
		});
		randomGreed();

	}

	public GameOfLife(int frameNumMax, int width, int height, int numberOfThreads) {
		m_frameNumMax = frameNumMax;
		m_width = width;
		m_height = height;
		m_numberOfThreads = numberOfThreads;
		m_greed = new int[m_height][m_width];
		m_nextGreed = new int[m_height][m_width];
		m_barrier = new CyclicBarrier(m_numberOfThreads, () -> {
			m_greed = m_nextGreed;
			addFrame();
			if (m_frameNum == m_frameNumMax) {
				m_isMoreFrameToCraete = false;
			}
		});
		randomGreed();
	}

	private void randomGreed() {
		for (int i = 0; i < m_height; i++) {
			for (int j = 0; j < m_width; j++) {
				m_greed[i][j] = ThreadLocalRandom.current().nextInt(0, 2);
			}
		}
	}

	public void makeFrames() {
		for (int i = 0; i < m_frameNumMax; i++) {
			nextFrameTraed();
			
//			nextFrame();
//			addFrame();
		}
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
				m_nextGreed[i][j] = isNextlive(i, j);
			}
		}
	}

	private void nextFrameTraed() {
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < m_numberOfThreads; i++) {
			int start = (i) * (m_height / m_numberOfThreads);
			int end = (i + 1) * (m_height / m_numberOfThreads);
			// end += (i==m_numberOfThreads-1) ?0:-1;
			// System.out.println(start +" "+end);

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

	public boolean isMoreFrameToCraete() {
		return m_isMoreFrameToCraete;
	}

	private void nextFrame() {
		for (int i = 0; i < m_height; i++) {
			for (int j = 0; j < m_width; j++) {
				m_nextGreed[i][j] = isNextlive(i, j);
			}
		}
		m_greed = m_nextGreed;
	}

	private int isNextlive(int i, int j) {
		int numOfNabeur = numOfNabeurs(i, j);
		if (m_greed[i][j] == 1) {
			return (numOfNabeur == 2 || numOfNabeur == 3) ? 1 : 0;
		} else {
			return (numOfNabeur == 3) ? 1 : 0;
		}

	}

	private int numOfNabeurs(int height, int width) {

		int numOfNabeurs = 0;
		if (valid(height + 1, width) && m_greed[height + 1][width] == 1) {
			numOfNabeurs++;
		}
		if (valid(height + 1, width + 1) && m_greed[height + 1][width + 1] == 1) {
			numOfNabeurs++;
		}
		if (valid(height, width + 1) && m_greed[height][width + 1] == 1) {
			numOfNabeurs++;
		}
		if (valid(height - 1, width + 1) && m_greed[height - 1][width + 1] == 1) {
			numOfNabeurs++;
		}
		if (valid(height + 1, width - 1) && m_greed[height + 1][width - 1] == 1) {
			numOfNabeurs++;
		}
		if (valid(height - 1, width - 1) && m_greed[height - 1][width - 1] == 1) {
			numOfNabeurs++;
		}
		if (valid(height - 1, width) && m_greed[height - 1][width] == 1) {
			numOfNabeurs++;
		}
		if (valid(height, width - 1) && m_greed[height][width - 1] == 1) {
			numOfNabeurs++;
		}
		return numOfNabeurs;
	}

	private boolean valid(int height, int width) {
		if (0 <= height && height < m_height && 0 <= width && width < m_width) {
			return true;
		}
		return false;
	}

	private void addFrame() {

		try {
			try (BufferedWriter writer = Files.newBufferedWriter(
					Paths.get("imeges/frame" + String.format("%03d", m_frameNum++) + ".pbm"), Charset.forName("UTF-8"))) {
				writer.write("P1" + "\n");
				writer.write(String.valueOf(m_height) + " " + String.valueOf(m_width) + "\n");
				for (int i = 0; i < m_height; i++) {
					for (int j = 0; j < m_width; j++) {
						writer.write(String.valueOf(m_greed[i][j]) + " ");
					}
					writer.write("\n");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
