package customExceptions;


public class ExistUserExceptions extends Exception {
	public ExistUserExceptions(String id, String typeId) {
		super("El usuario con el id " + id + " y tipo de documento "+ typeId  + " ya existe");
		
	}
	

}
