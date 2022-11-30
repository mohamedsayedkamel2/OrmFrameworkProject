package establisher;

import session.DatabaseSession;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public class DbConnectionEstablisher {

    private DbConnectionEstablisher() {}
    public static DatabaseSession createConnection(String jdbcInfoFileUrl)  {

        ConnectionInfo connection = DbConnectionInfoConfigure.setConnectionInfo(jdbcInfoFileUrl);
        Constructor<DatabaseSession> constructorObject = null;

        try {
            constructorObject = DatabaseSession.class.getDeclaredConstructor(Connection.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("A session.DatabaseSession constructor with Connection.class arugments does not exist, please use your mind and fix this error.");
        }

        constructorObject.setAccessible(true);
        DatabaseSession databaseSession = null;

        try {
            databaseSession = constructorObject.newInstance(connection.dbConnection);
        } catch (InstantiationException e) {
            throw new RuntimeException("Error happend during creation of database session object using reflection.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't access a package-private/private property using reflection, please set the ability to access this property to true.");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Can't invoke this method, please check if the arguments are valid and try again.");
        }

        return databaseSession;
    }
}