package conway.universe;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class App {
    public static void main( String[] args ) {
    	//Get data
        Scanner s = new Scanner(System.in);
        System.out.println("Enter width:");
        int width = s.nextInt();
        System.out.println("Enter height:");
        int height = s.nextInt();
        System.out.println("Enter frame rate:");
        int fps = s.nextInt();
        System.out.println("Enter video length in seconds:");
        int frames = s.nextInt() * fps;
        System.out.println("Enter directory and file name:");
        String path = s.next();
        System.out.println("Enter seed, or 0 if a random seed:");
        long seed = s.nextLong();
        Random random = seed == 0 ? new Random() : new Random(seed);
        s.close();
        
        boolean[][][] randomData = getRandomData(width, height, random);
        boolean[][] data = randomData[0];
        boolean[][] nextData = randomData[1];
		
        for (int i = 0; i < frames; i++) {
        	System.out.println("Starting frame " + (i + 1) + " of frame " + frames);
        	BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        	
        	//For each pixel, see if conway's game of life and change accordingly
        	for (int x = 0; x < frame.getWidth(); x++) {
        		for (int y = 0; y < frame.getHeight(); y++) {
        			if (follows(data, x, y, width, height)) {
        				nextData[x][y] = true;
        				frame.setRGB(x, y, 16777215);
        			} else {
        				nextData[x][y] = false;
        				frame.setRGB(x, y, 0);
        			}
        		}
        	}
        	
        	try {
        		ImageIO.write(frame, "PNG", new File(path + i + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
        	
        	//Copy nextData to data
        	for (int x = 0; x < frame.getWidth(); x++) {
        		for (int y = 0; y < frame.getHeight(); y++) {
        			data[x][y] = nextData[x][y];
        		}
        	}
        }
        System.out.println("Finished!");
    }

	private static int fix(int n1, int n2) {
    	if (n1 == -1) {
    		return n2 - 2;
    	} else if (n1 == n2) {
    		return 0;
    	} else {
    		return n1;
    	}
    }

    //Check if a cell follows Conway's game of life
	private static boolean follows(boolean[][] data, int x, int y, int width, int height) {
		byte c = 0;
		boolean in = false;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i == x && j == y) {
					in = data[i][j];
				} else if (data[fix(i, width)][fix(j, height)]) {
					c++;
				}
			}
		}
		return (in & (c == 2 | c == 3)) | (!in & c == 3);
	}

	private static boolean[][][] getRandomData(int width, int height, Random random) {
		boolean[][] tr1 = new boolean[width][height];
		boolean[][] tr2 = new boolean[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				boolean is = random.nextInt(2) == 0;
				tr1[x][y] = is;
				tr2[x][y] = is;
			}
		}
		return new boolean[][][]{tr1, tr2};
	}
}
