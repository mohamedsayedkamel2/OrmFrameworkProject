package framework.generator.exceptions;

public class PrimaryKeyNotFoundException extends RuntimeException {
    public PrimaryKeyNotFoundException(String s) {
        super(s);
    }
}
