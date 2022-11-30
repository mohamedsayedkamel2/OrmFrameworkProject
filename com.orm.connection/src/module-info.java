module com.orm.connection {
    exports session;
    exports establisher;
    requires java.sql;
    requires com.orm.query;
    requires com.orm.executor;
}