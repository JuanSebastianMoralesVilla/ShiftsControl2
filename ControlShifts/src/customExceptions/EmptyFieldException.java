package customExceptions;

public class EmptyFieldException extends Exception {

	public EmptyFieldException() {
		super("Dilengia todos los campos, no dejes ninguno vacio");
	}
}
