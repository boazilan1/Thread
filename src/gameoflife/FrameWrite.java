package gameoflife;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FrameWrite<T> {
	private Path m_dirPath;
	public FrameWrite(Path dirPath) {
		dirPath= m_dirPath;
	}
	
	private void saveFrame(Grid<T> grid,int frameNum) {

		try {
			Path imgPath=Paths.get("imeges/frame" + String.format("%03d", frameNum++) + ".pbm");
			try (BufferedWriter writer = Files.newBufferedWriter(imgPath, Charset.forName("UTF-8"))) {
				writer.write("P1" + "\n");
				writer.write(String.valueOf(grid.getHeight()) + " " + String.valueOf(grid.getWidth()) + "\n");
				for (int i = 0; i < grid.getHeight(); i++) {
					for (int j = 0; j < grid.getWidth(); j++) {
						String isAlive = (boolean) grid.get(i, j) ? "1" : "0";
						writer.write(isAlive + " ");
					}
					writer.write("\n");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
