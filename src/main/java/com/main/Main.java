package com.main;

import framework.connection.DatabaseSession;
import framework.connection.DbConnectionEstablisher;

public class Main {
    public static void main(String[] args) {
        // In the future it's supposed to be like this
        // DatabaseSession session = DbConnectionEstablisher.createConnection(args[0]);

        DatabaseSession session = DbConnectionEstablisher.createConnection(args[0]);
        Names names = new Names("asdasd");
        names.name = "Abdo";

        session.insert(names);
    }
}


