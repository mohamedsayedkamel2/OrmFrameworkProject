package blabla;

import annotations.Field;
import annotations.PrimaryKey;
import annotations.Table;
import annotations.Transient;
import establisher.DbConnectionEstablisher;
import session.DatabaseSession;

public class Main {
    public static void main(String[] args) {
        DatabaseSession session = DbConnectionEstablisher.createConnection(args[0]);
        Person person = new Person();
        person.name = "Mohamed";
        person.age = 19;
        session.insert(person);
    }
}


@Table
class Person {

    @PrimaryKey
    @Field
    public int primaryKey;
    @Field
    public String name;
    @Field
    @Transient
    public int age;
}
