package pdf.formatter.pdf_formatter;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

public class PropertiesExtractor {
    // Normally, spring boot should read application.properties automatically, but
    // it does not work from any of the given locations in the docs
    private static Properties properties;
    static {
        properties = new Properties();
        URL url = new PropertiesExtractor().getClass().getClassLoader().getResource("application.properties");
        try {
            properties.load(new FileInputStream(url.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}