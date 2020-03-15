package Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.channels.ShutdownChannelGroupException;

import org.junit.jupiter.api.Test;

import customExceptions.EmptyFieldException;
import customExceptions.ExistUserExceptions;
import customExceptions.SingleShiftException;
import model.Shift;
import model.ShiftControl;
import model.User;

public class ShiftControlTest {

	private  ShiftControl sc = new ShiftControl();

//escenarios
	public ShiftControl setup1() {
		try {
			sc.addUser(1, "123456", " julian", "perez", "553131", "calle 6 # 21-21");
			sc.addUser(1, "1234567", "juan", "morales", "52123642", "calle 1 # 1-11");
			sc.addUser(2, "1234567", "pedro", "perez", "2126732", "calle 5 # 19-2");
			sc.addUser(3, "1234568", "david", "ramirez", "2126732", "calle 5 # 19-2");
			sc.addUser(1, "1234561", "ana ", "sebastian", "2324232", "calle 3 # 22-2");

		} catch (ExistUserExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmptyFieldException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return sc;

	}

	// escenarios
	public void setup2() {
		try {
			sc.addUser(1, "12345", "James", "Rodriguez", "31333313", "ST 40 Madrid");
		} catch (ExistUserExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmptyFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setup3() {
		sc.restartCurrent();
		sc.findShift('A', 0).setCurrent(true);
	}

	public void setup4() {
		sc.restartCurrent();
		sc.findShift('D', 99).setCurrent(true);;
	}

	public void setup5() {
		sc.restartCurrent();
		sc.findShift('Z', 99).setCurrent(true);
	}
	
	public void setup6() {
		
		setup1();
		sc.restartCurrent();
		sc.findShift('E', 19).setCurrent(true);
		try {
		sc.AssignementShift("123456", 1);
		sc.AssignementShift("1234567", 1);
		}catch(SingleShiftException e) {
			
		}
	}
	

	// prueba que agrega un usuario cuando el arreglo esta vacio
	@Test
	public void addUsertest() {

		try {
			sc.addUser(1, "1234", "James", "Rodriguez", "31333313", "ST 40 Madrid");
			assertTrue(sc.getUsers().get(0).getId().equals("1234"));
		} catch (ExistUserExceptions | EmptyFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// prueba si el usuario no existe y hay usuarios
	@Test
	public void addUsertest2() {
		try {
			setup1();
			sc.addUser(1, "12345", "James", "Rodriguez", "31333313", "ST 40 Madrid");
			assertTrue(sc.getUsers().get(4).getId().equals("1234561"));
		} catch (ExistUserExceptions | EmptyFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// prueba si el usuario existe

	@Test
	public void addUsertest3() {
		try {
			setup2();
			sc.addUser(1, "12345", "James", "Rodriguez", "31333313", "ST 40 Madrid");
			assertEquals(1, sc.getUsers().size());
		} catch (ExistUserExceptions e1) {
			// TODO Auto-generated catch block
		} catch (EmptyFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// pruebas B

	// Si ya existe el usuario a buscar y hay una cantidad de usuarios
	@Test
	public void findUserbyIDTest() {
		setup1();
		User user = sc.findUserbyID("1234567", 1);
		User user2 = sc.findUserbyID("1234567", 2);
		assertEquals("juan", user.getName());
		assertEquals("perez", user2.getLastName());
	}

	// Si no existe el usuario a buscar && Si no hay usuarios en el programa

	@Test
	public void findUserbyIDTest2() {
		assertNull(sc.findUserbyID("123456", 2));

	}

	/*
	 * c Asigna un turno a un usuario: Si el usuario tiene un turno activo, entonces
	 * debe probar que retorne el turno activo y no genere otro Si el usuario no
	 * tiene un turno activo entonces le genera uno nuevo
	 */

	@Test
	public void AssignementShiftTest1() {

		setup1();
		try {
		sc.AssignementShift("123456", 1);
		sc.AssignementShift("123456", 1);
		sc.AssignementShift("123456", 1);
		sc.AssignementShift("123456", 1);
		sc.AssignementShift("1234567", 2);
		sc.AssignementShift("1234567", 2);
		}catch(SingleShiftException e) {
			
		}
		assertEquals("A0", String.valueOf(sc.findUserbyID("123456", 1).getShift().getLetter())
				+ String.valueOf(sc.findUserbyID("123456", 1).getShift().getNum()));
		assertEquals("A1", String.valueOf(sc.findUserbyID("1234567", 2).getShift().getLetter())
				+ String.valueOf(sc.findUserbyID("1234567", 2).getShift().getNum()));

	}

	@Test

	public void AssignementShiftTest2() {
		setup1();
		try {
			sc.AssignementShift("123456", 1);
		} catch (SingleShiftException e) {
			// TODO Auto-generated catch block
		}
		assertEquals("A0", String.valueOf(sc.findUserbyID("123456", 1).getShift().getLetter())
				+ String.valueOf(sc.findUserbyID("123456", 1).getShift().getNum()));
	}

	/*
	 * Genera un nuevo turno: Consecutivo al último turno asignado Si es el primer
	 * turno genera el turno A00 Si el último turno generado es el D99 entonces
	 * genera el turno E00 Si el último turno generado es el Z99 entonces genera el
	 * turno A00
	 */
	@Test
	public void serveShiftTest() {
		setup3();
		assertEquals("A00",sc.showCurrentShift());
	}
	/*
	 * D99 > E00
	 */
	@Test
	public void serveShiftTest2() {
		setup4();
		sc.serveShift(Shift.ATTENDED);
		assertEquals("E00", sc.showCurrentShift());
	}
	/*
	 * Z99 >A00
	 */
	@Test
	public void serveShiftTest3() {
		setup5();
		sc.serveShift(Shift.NO_ATTENDED);
		assertEquals("A00",sc.showCurrentShift());
	}

	/*
	 * e. Consulta correctamente el siguiente turno por atender: i. Habiendo turnos
	 * por atender,
	 */
	@Test
	public void serverShiftTest4() {
		setup6();
		assertEquals("E19", sc.showCurrentShift());
	}
	/*
	 *  f. Atiende un turno asignado a una persona: i. Habiendo turnos por atender
	 */
	@Test
	public void serveShiftTest5() {
		setup6();
		assertNotNull(sc.findUserbyID("123456", 1).getShift());
		assertNotNull(sc.findUserbyID("1234567", 1).getShift());
		sc.serveShift(Shift.ATTENDED);
		assertEquals("E20", sc.showCurrentShift());
		sc.serveShift(Shift.NO_ATTENDED);
		assertEquals("E21", sc.showCurrentShift());
		assertNull(sc.findUserbyID("123456", 1).getShift());
		assertNull(sc.findUserbyID("1234567", 1).getShift());

	}

}
