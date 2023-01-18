package gameoflife;

public interface Grid<T> {

	public T get(int i,int j);
	public void set(int i,int j,T t);
	public int getHeight();
	public int getWidth();
}
