package gameoflife;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class ClassicLifeRules<T> implements GameRules<T> {
	private final BiPredicate<Integer, Boolean> m_rule;
	private final List<int[]> m_possibleNeighbor;

	public ClassicLifeRules(List<int[]> possibleNeighbor, BiPredicate<Integer, Boolean> rule) {
		m_possibleNeighbor = possibleNeighbor;
		m_rule = rule;
	}

	@Override
	public boolean isNextAlive(int i, int j, Grid<T> grid) {
		int numOfNeighbor = numOfNabeurs(i, j, grid);				
		return m_rule.test(numOfNeighbor,(Boolean) grid.get(i, j));
	}

	private int numOfNabeurs(int i, int j, Grid<T> grid) {
		int heightCheck;
		int WidthtCheck;
		int numOfNabeurs = 0;
		for (int[] nabeurPixal : m_possibleNeighbor) {
			heightCheck = i + nabeurPixal[0];
			WidthtCheck = j + nabeurPixal[1];
			//System.out.println(heightCheck+" "+WidthtCheck);
			if (valid(heightCheck, WidthtCheck, grid) && (boolean) grid.get(heightCheck, WidthtCheck) == true) {
				numOfNabeurs++;
			}
		}
		//System.out.println(numOfNabeurs);
		return numOfNabeurs;
	}

	private boolean valid(int i, int j, Grid<T> grid) {
		if (0 <= i && i < grid.getHeight() && 0 <= j && j < grid.getWidth()) {
			return true;
		}
		return false;
	}
}
