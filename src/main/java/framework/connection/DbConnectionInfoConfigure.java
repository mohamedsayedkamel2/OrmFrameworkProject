package framework.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class DbConnectionInfoConfigure {

    static ConnectionInfo setConnectionInfo(String jdbcInfoFileUrl)  {

        File file = new File(jdbcInfoFileUrl);
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The file is no where to be found in the path you have sent, please enter a valid path to the file.");
        }

        Properties properties = new Properties();

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("An I/O error occured, please check if your file is damaged.");
        }

        ConnectionInfo connectionInfo = null;
        connectionInfo = new ConnectionInfo(properties);
        return connectionInfo;
    }

}
