import java.io.*;
import java.util.*;
public class ReadWatchlistFile {
    /**
     * reads a propertyfile and asumes it's a watchlist
     * @param fileName the name of the file you want to read
     * @return the Data as Propertyvariable
     */
    public static Properties readWatchlistFile(String fileName){
        InputStream fis = null;
        Properties prop = null;
        try {
            ClassLoader classLoader = ReadWatchlistFile.class.getClassLoader();
            fis = classLoader.getResourceAsStream(fileName);

            if (fis == null) {
                throw new IllegalArgumentException("file not found! " + fileName);
            }

            prop = new Properties();
            prop.load(fis);
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop;
    }
}