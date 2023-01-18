package gameoflife;

public interface GameRules<T> {

	public boolean isNextAlive(int i, int j, Grid<T> grid);

}
