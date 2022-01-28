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
        //removing empty props
        prop = removeEmptyProps(prop);
        return prop;
    }

    private static Properties removeEmptyProps(Properties prop) {
        List<Object> emptyProps = new ArrayList<>();
        prop.forEach((k, v) -> {
            if(v == null){
                emptyProps.add(k);
            }
        });

        for(var emptyProp:emptyProps){
            prop.remove(emptyProp);
        }
        return prop;
    }
}