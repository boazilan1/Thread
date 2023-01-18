package gameoflife;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FrameWriter<T> {
	private Path m_dirPath;
	public FrameWriter(Path dirPath) {
		m_dirPath = dirPath;
		try {
			if(!Files.isDirectory(m_dirPath)){
				Files.createDirectory(m_dirPath);
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void saveFrame(Grid<T> grid,int frameNum) {

		try {
			Path imgPath=Paths.get("imeges/frame" + String.format("%03d", frameNum) + ".pbm");
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
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
