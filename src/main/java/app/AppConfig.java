package app;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class AppConfig {
    private String db_url;
    private String username;
    private String password;

    public AppConfig(){
        final Properties prop = new Properties();
        try (final InputStream input = new FileInputStream("config.properties")){
            prop.load(input);

            this.db_url = prop.getProperty("DB_URL");
            this.username = prop.getProperty("DB_USER");
            this.password = prop.getProperty("DB_PASSWORD");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDb_url() {
        return this.db_url;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }
}
