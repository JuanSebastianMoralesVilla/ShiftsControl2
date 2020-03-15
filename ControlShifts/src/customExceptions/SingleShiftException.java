package customExceptions;

public class SingleShiftException extends Exception{


	public SingleShiftException(String id, int typeId) {
		super("El usuario con tipo de documento " + typeId+" y numero: "+ id+" ya tiene un turno asignado");
	}
}
