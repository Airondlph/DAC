package DAC.graphic.tools;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Fonts {
    public static final int DEFAULT_BUFFER_SIZE = 1024;

	private final static String fontsPath = "fonts/";
	
	private final static String comicSansMSFile = "comic.ttf";
	private static Font comicSansMS;
	
	private static boolean copyFile(InputStream inputStream, File file) {	    
	    try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            
            try {
				while ((read = inputStream.read(bytes)) != -1) {
				    outputStream.write(bytes, 0, read);
				}
			} catch (IOException e) {
				// e.printStackTrace();
				return false;
			}
        } catch (IOException e) {
			// e.printStackTrace();
			return false;
		}
	    
	    return true;
	}
	
	public static Font loadFont(String fontsPath, String fontFile) {
		// System.out.println("Working Directory = " + System.getProperty("user.dir"));
		
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new File(fontsPath + fontFile)).deriveFont(Font.PLAIN, 20.0f);
			
			// InputStream is = new FileInputStream(new File(fontsPath + fontFile));
			// File tmpFile = new File(System.getProperty("java.io.tmpdir") + "/DACFonts.tmp");
			// if(!copyFile(is, tmpFile)) return null;
			// return Font.createFont(Font.TRUETYPE_FONT, tmpFile).deriveFont(Font.PLAIN, 20.0f);

			
		} catch (FontFormatException | IOException e) {
			System.err.println("Error al cargar la fuente: Comic Sans MS");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Font loadFont(String fontsPath, String fontFile, int size) {
		// System.out.println("Working Directory = " + System.getProperty("user.dir"));
		
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new File(fontsPath + fontFile)).deriveFont(Font.PLAIN, size);
			
			// InputStream is = new FileInputStream(new File(fontsPath + fontFile));
			// File tmpFile = new File(System.getProperty("java.io.tmpdir") + "/DACFonts.tmp");
			// if(!copyFile(is, tmpFile)) return null;
			// return Font.createFont(Font.TRUETYPE_FONT, tmpFile).deriveFont(Font.PLAIN, 20.0f);

			
		} catch (FontFormatException | IOException e) {
			System.err.println("Error al cargar la fuente: Comic Sans MS");
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public static Font getComicSansMS() {
	    comicSansMS = loadFont(fontsPath, comicSansMSFile);
		
		return comicSansMS;
	}
	
	
	public static Font getComicSansMS(int size) {
	    comicSansMS = loadFont(fontsPath, comicSansMSFile, size);
		
		return comicSansMS;
	}
}
