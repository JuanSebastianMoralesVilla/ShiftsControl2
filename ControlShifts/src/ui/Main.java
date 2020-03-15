package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import customExceptions.EmptyFieldException;
import customExceptions.ExistUserExceptions;
import customExceptions.SingleShiftException;
import model.Shift;
import model.ShiftControl;
import model.User;

public class Main {


	static Scanner reader = new Scanner(System.in);
	static ShiftControl sc = new ShiftControl();
	public final static String FILE_USERS="data/Datos.txt";
	
	public static void main(String[] args) throws IOException {

		int option = 0;
		String id;
		int typeId;
		sc.loadUsers();
		sc.loadShiftsHist();

		do {
			System.out.println("");
			System.out.println(" SISTEMA DE CONTROL DE TURNOS ");
			sc.getDate().showDate();
			System.out.println("");
			System.out.println("1. Asignar turno");
			System.out.println("2. Agregar usuario");
			System.out.println("3. Atender turno: ");
			System.out.println("4. Turno actual");
			System.out.println("5. Mostar lista de usuarios:");
			System.out.println("6.Mostar fecha y hora");
			System.out.println("7.Actualizar fecha y hora");

		System.out.println("8.Agregar los tipos de turnos ");
		System.out.println("9. Mostrar tipos de turnos:");
		System.out.println("10. Atender turnos Automaticamente:");
		System.out.println("11. Generar reporte turnos de un usuario");
		System.out.println("12. Generar usuarios aleatorios");
		System.out.println("13.Generar reporte de usuarios por turno");
		System.out.println("14.Suspender usuarios");
		System.out.println("15. Guardar programa y SALIR");
			option = Integer.parseInt(reader.nextLine());

			switch (option) {
			case 1:
				System.out.println("Ingrese el tipo de identificacion:" + "\n 1.Cedula de ciudadania "
						+ "\n 2.Tarjeta de identidad" + "\n 3.Registro civil" + "\n 4.Pasaporte"
						+ "\n 5.cedula Extranjera");
				typeId = Integer.parseInt(reader.nextLine());
				System.out.println("Ingrese su id  : ");
				System.out.println(" ");
				id = reader.nextLine();

				User user = sc.findUserbyID(id, typeId);
				if (user == null) {
					System.out.println(
							"El usuario no esta registrado en el sistema  por lo tanto debe agregar un usuario "
									+ "\n");
					addUserInMain();

				} else {

					try {
						sc.AssignementShift(id, typeId);
					} catch (SingleShiftException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Se asigno el turno " + user.getShift().toString() + " a: " + user.toString());

				}

				break;

			case 2:
				addUserInMain();
				break;

			case 3:
				int optionState;
				String state;
				System.out.println("TURNO ACTUAL" + "\n");
				System.out.println(sc.showCurrentShift());

				System.out.println("Ingrese : " + "\n 1. El usuario fue atendido " + "\n 2. El usuario no esta ");
				optionState = Integer.parseInt(reader.nextLine());

				if (optionState == 1) {

					state = Shift.ATTENDED;
				} else {

					state = Shift.NO_ATTENDED;
				}

				sc.serveShift(state);
				break;

			case 4:
				System.out.println("TURNO ACTUAL" + "\n");
				System.out.println(sc.showCurrentShift());
				break;
			case 5:
				sc.showUser();
				break;
			case 6:
				sc.getDate().showDate();
				break;

			case 7:
				int optionDte;
				System.out.println("Como desea actualizar la hora:");
				System.out.println("1.Manual");
				System.out.println("2. automatica");
				optionDte = Integer.parseInt(reader.nextLine());

				if (optionDte == 1) {
					int month, day, year, hour, min, sec;

					System.out.println("Digite mes [1/12]?");
					month = Integer.parseInt(reader.nextLine());
					System.out.println("Digite dia [1/31]?");
					day = Integer.parseInt(reader.nextLine());
					System.out.println("Digite anio [>]?");
					year = Integer.parseInt(reader.nextLine());
					System.out.println("Digite hora en formato 24 ?");
					hour = Integer.parseInt(reader.nextLine());
					System.out.println("Digite minutos [1/60] ?");
					min = Integer.parseInt(reader.nextLine());
					System.out.println("Digite segundos [1/60] ?");
					sec = Integer.parseInt(reader.nextLine());

					sc.manualUpdate(month, day, year, hour, min, sec);

				} else if (optionDte == 2) {

					sc.autoUpdate();

				}
				break;

			case 8:
				String nameShift;
				float duration = 0;
				System.out.println("Ingresa el nombre del tipo de turno:");
				nameShift = reader.nextLine();
                System.out.println("Ingresa la duracion del tipo de turno en segundos:");
				duration = Float.parseFloat(reader.nextLine());
                sc.addTypeShift(nameShift, duration);
                System.out.println("Turno agregado correctamente");
                
                break;
                
			case 9: 
				
				sc.showTypesShifts();
				break;
				

			case 10:
				sc.serveShiftAuto();
				break;
			
			case 11:
				sc.showUser();
				System.out.println("digite el usuario al cual le quiere hacer un reporte:");
				int usReport=Integer.parseInt(reader.nextLine()) ;
				System.out.println("Como desea generar el reporte?: ");
				System.out.println("0. Consola");
				System.out.println("1. Archivo de texto");

				int mode=Integer.parseInt(reader.nextLine());
				
				System.out.println("Ascendente o descendente: ");
				System.out.println("0.Descendente ");
				System.out.println("1.Ascendente ");

				int order0= Integer.parseInt(reader.nextLine());
				try {
					sc.userShiftsReport(sc.getUsers().get(usReport),mode,order0);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case 12:
				System.out.println("Cuantas personas desea registrar en el sistema: ");
				int cantUsers = Integer.parseInt(reader.nextLine());
				sc.loadRandomUsers(FILE_USERS, cantUsers);
				break;
				
			case 13:
				
				System.out.println("Ingresa la letra del turno:");
				char letter=reader.nextLine().charAt(0);
				System.out.println(letter);
				System.out.println("Ingresa el numero del turno:");
				int number= Integer.parseInt(reader.nextLine());
				
				System.out.println("Como desea generar el reporte?: ");
				System.out.println("0. Consola");
				System.out.println("1. Archivo de texto");

				int mode2=Integer.parseInt(reader.nextLine());
				
				System.out.println("Ascendente o descendente: ");
				System.out.println("0.Descendente ");
				System.out.println("1.Ascendente ");

				int order= Integer.parseInt(reader.nextLine());

				try {
					Shift s= sc.getShifts().get(sc.findShiftBinary(new Shift(letter,number,null)));
					sc.UserByShiftReport(s,mode2,order);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
				
			case 14:
				sc.suspendUsers();
				break;
				
			}

		} while (option != 15);
		sc.saveUsers(sc.getUsers());

	}

	public static void addUserInMain() {

		String id;
		int typeId = 0;
		String name;
		String lastName;
		String telephone;
		String adress;

		System.out.println("Ingrese el tipo de identificacion:" + "\n 1.Cedula de ciudadania "
				+ "\n 2.Tarjeta de identidad" + "\n 3.Registro civil" + "\n 4.Pasaporte" + "\n 5.cedula Extranjera");
		typeId = Integer.parseInt(reader.nextLine());
		System.out.println("Ingrese su id  : ");
		id = reader.nextLine();
		System.out.println("Ingrese su nombre  : ");
		name = reader.nextLine();
		System.out.println("Ingrese su apellido  : ");
		lastName = reader.nextLine();
		System.out.println("Ingrese su telefono : ");
		telephone = reader.nextLine();
		System.out.println("Ingrese su direccion : ");
		adress = reader.nextLine();

		try {
			System.out.println(sc.addUser(typeId, id, name, lastName, telephone, adress));
		} catch (ExistUserExceptions | EmptyFieldException e) {
			// TODO Auto-generated catch block
			System.err.print(e.getMessage());
		}

	}

}
