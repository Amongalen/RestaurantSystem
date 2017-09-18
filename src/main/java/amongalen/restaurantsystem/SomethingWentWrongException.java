package amongalen.restaurantsystem;

/**
 *
 * @author Adam Parys
 */
public class SomethingWentWrongException extends Exception {

    /**
     * Creates a new instance of <code>SomethingWentWrongException</code>
     * without detail message.
     */
    public SomethingWentWrongException() {
    }

    /**
     * Constructs an instance of <code>SomethingWentWrongException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public SomethingWentWrongException(String msg) {
        super(msg);
    }
}
