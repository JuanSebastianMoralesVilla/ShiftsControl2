package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import customExceptions.EmptyFieldException;
import customExceptions.ExistUserExceptions;
import customExceptions.SingleShiftException;

public class ShiftControl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Shift> shifts;
	private ArrayList<User> users;
	// new
	private ArrayList<TypeShift> types;
	private ArrayList<Shift> historyShifts = new ArrayList<Shift>();
	private char letters[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	// nuevo
	private DateHour date;

	public final String FILE_USERS = "data/Datos.txt";

	public ShiftControl() {
		super();
		shifts = new ArrayList<Shift>();
		users = new ArrayList<User>();
		types = new ArrayList<TypeShift>();
		addShifts();
		findShift('A', 0).setCurrent(true);
		date = new DateHour(0);
	}

	public ArrayList<TypeShift> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<TypeShift> types) {
		this.types = types;
	}

	public DateHour getDate() {
		return date;
	}

	public void setDate(DateHour date) {
		this.date = date;
	}

	public ArrayList<Shift> getShifts() {
		return shifts;
	}

	public void setShifts(ArrayList<Shift> shifts) {
		this.shifts = shifts;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public char[] getLetters() {
		return letters;
	}

	public void setLetters(char[] letters) {
		this.letters = letters;
	}

	public void addShifts() {

		for (int i = 0; i < letters.length; i++) {
			for (int j = 0; j < 100; j++) {

				Shift shift = new Shift(letters[i], j, null);
				shifts.add(shift);
			}
		}

	}

	public String addUser(int typeId, String id, String name, String lastName, String telephone, String adress)
			throws ExistUserExceptions, EmptyFieldException {
		String mgs = "";
		String type = "";

		if (String.valueOf(typeId) != null && id != null && name != null && lastName != null && telephone != null
				&& adress != null) {
			if (typeId == User.CEDULA_CIUDADANIA) {
				type = "Cedula de ciudadania";
			}
			if (typeId == User.TARJETA_IDENTIDAD) {
				type = "Tarjeta de identidad";
			}
			if (typeId == User.CEDULA_EXTRANJERA) {
				type = "Cedula extranjera";
			}
			if (typeId == User.PASAPORTE) {
				type = "Pasaporte";
			}
			if (typeId == User.REGISTROCIVIL) {
				type = "Registro civil";
			}

			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getId().equals(id) && users.get(i).getTypeId() == typeId) {
					throw new ExistUserExceptions(id, type);

				}
			}
			users.add(new User(typeId, id, name, lastName, telephone, adress));
			mgs = "El usuario fue agregado con exito";
		} else {
			throw new EmptyFieldException();
		}
		return mgs;
	}

	public User findUserbyID(String id, int typeId) {

		User user = null;

		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getId().equals(id) && users.get(i).getTypeId() == typeId) {
				user = users.get(i);

			}

		}
		return user;
	}

	public int getIndexOfCurrent() {
		for (int i = 0; i < shifts.size(); i++) {
			if (shifts.get(i).isCurrent()) {
				return i;
			}
		}
		return 0;
	}

	public void AssignementShift(String id, int typeId) throws SingleShiftException {

		User user = findUserbyID(id, typeId);

		if (user != null && user.getShift() == null) {
			for (int i = getIndexOfCurrent(); i < shifts.size(); i++) {
				if (shifts.get(i).isAvailable()) {
					if (user.getShift() == null) {
						user.setShift(shifts.get(i));
						shifts.get(i).setUser(user);
						i = shifts.size();
					} else {
						throw new SingleShiftException(user.getId(), user.getTypeId());
					}
				}
			}
		}

	}

	public void restartShift(Shift s) {
		if (s.getUser() != null) {
			s.getUser().setShift(null);
			s.setUser(null);
		}
	}

	public void restartAllShifts() {
		for (int i = 0; i < shifts.size(); i++) {
			restartShift(shifts.get(i));
		}
	}

	public void serveShift(String state) {

		if (shifts.get(shifts.size() - 1).isCurrent()) {
			shifts.get(shifts.size() - 1).setCurrent(false);
			shifts.get(shifts.size() - 1).setState(state);
			restartShift(shifts.get(shifts.size() - 1));
			shifts.get(0).setCurrent(true);
		} else {
			for (int i = 0; i < shifts.size(); i++) {
				if (shifts.get(i).isCurrent()) {
					shifts.get(i + 1).setCurrent(true);
					shifts.get(i).setCurrent(false);
					shifts.get(i).setState(state);
					restartShift(shifts.get(i));
					i = shifts.size();
				}
			}

		}
	}
	// nuevo

	public void serveShiftAuto() {
		Scanner reader = new Scanner(System.in);
		for (int i = 0; i < shifts.size(); i++) {
			if (shifts.get(i).isCurrent()) {
				System.out.println(
						"Por favor escoja un tipo de turno, si no lo encuentra en la lista por favor agreguelo");
				showTypesShifts();
				System.out.println("-1. Para volver");
				int index = reader.nextInt();
				if (index > -1) {
					shifts.get(i).setType(types.get(index));
					System.out.println("Por favor escoja un usuario al cual asignarle el turno");
					showUser();
					int userIndex = reader.nextInt();
					Calendar actual = Calendar.getInstance();
					actual.setTimeInMillis(Calendar.getInstance().getTimeInMillis() - date.getTime());
					if (users.get(userIndex).getPenalty() <= actual.getTimeInMillis()) {
						users.get(userIndex).setShift(shifts.get(i));
						shifts.get(i).setUser(users.get(userIndex));
						Calendar finalDate = Calendar.getInstance();
						int duration = (int) shifts.get(i).getType().getDuration();
						finalDate.add(Calendar.SECOND, duration);
						System.out.println("Atendiendo turno: " + shifts.get(i).toString());
						// esta esperando que corra el turno
						while (Calendar.getInstance().before(finalDate)) {
						}

						System.out.println("Estaba el usuario?");
						System.out.println("0. NO");
						System.out.println("1. SI");
						int userState = reader.nextInt();
						if (userState == 1) {
							shifts.get(i).setState(Shift.ATTENDED);
						} else {
							shifts.get(i).setState(Shift.NO_ATTENDED);
						}
						Calendar attentionTime = Calendar.getInstance();
						attentionTime.setTimeInMillis(Calendar.getInstance().getTimeInMillis() - date.getTime());
						int hour = attentionTime.get(Calendar.HOUR);
						int min = attentionTime.get(Calendar.MINUTE);
						int sec = attentionTime.get(Calendar.SECOND);
						shifts.get(i).setTime(new DateHour(attentionTime.getTimeInMillis()));
						System.out.println("El turno " + shifts.get(i).toString() + " fue atendido a las "
								+ (hour < 10 ? "0" + hour : hour) + ":" + (min < 10 ? "0" + min : min) + ":"
								+ (sec < 10 ? "0" + sec : sec));
						users.get(userIndex).getShiftsHistory().add(shifts.get(i));
						historyShifts.add(shifts.get(i));
					} else {
						System.out.println("Este usuario esta penalizado");
					}
					shifts.get(i).setCurrent(false);
					if (i < shifts.size() - 2) {
						shifts.get(i + 1).setCurrent(true);
					}

					Calendar waitDate = Calendar.getInstance();
					waitDate.add(Calendar.SECOND, 15);
					System.out.println("Cambio de turno en 15 segundos");
					while (Calendar.getInstance().before(waitDate)) {
					}
				}
			}

		}
		try {
			saveShiftsHist(historyShifts);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String showCurrentShift() {

		String current = "";

		if (shifts.isEmpty()) {
			current = " NO HAY TURNOS ACTUALES";

		} else {
			for (int i = 0; i < shifts.size(); i++) {

				if (shifts.get(i).isCurrent()) {
					current = shifts.get(i).toString();
				}
			}
		}
		return current;
	}

	public void showUser() {
		String shift = "";

		for (int i = 0; i < users.size(); i++) {

			if (users.get(i) != null) {
				if (users.get(i).getShift() != null) {
					shift = users.get(i).getShift().toString();
				} else {
					shift = "No tiene asignado un turno";
				}
				System.out.println(
						i + ". nombre :  " + users.get(i).getName() + " apellido :  " + users.get(i).getLastName()
								+ " identificacion :  " + users.get(i).getId() + " Turno :" + shift);

			}
		}

	}

	public Shift findShift(char letter, int num) {

		for (int i = 0; i < shifts.size(); i++) {
			if (shifts.get(i).getLetter() == letter && shifts.get(i).getNum() == num) {
				return shifts.get(i);

			}

		}
		return null;
	}

	public void restartCurrent() {
		for (int i = 0; i < shifts.size(); i++) {
			shifts.get(i).setCurrent(false);

		}
	}

	// nuevo

	public void autoUpdate() {
		date.setTime(0);
		restartAllShifts();
	}

	public void manualUpdate(int month, int day, int year, int hour, int min, int sec) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day, hour, min, sec);
		if (c.before(Calendar.getInstance())) {
			System.out.println("No es posible actualizar la fecha a una menor");
		} else
			date.setTime(Calendar.getInstance().getTimeInMillis() - c.getTimeInMillis());
		restartAllShifts();

	}

	public void addTypeShift(String name, float duration) {
		TypeShift ts = new TypeShift(name, duration);
		if (!existTypeShift(name))
			types.add(ts);
	}

	public boolean existTypeShift(String name) {
		boolean exist = false;

		for (int i = 0; i < types.size(); i++) {

			if (name.equalsIgnoreCase(types.get(i).getName())) {

				exist = true;
			}

		}
		return exist;
	}

	public void showTypesShifts() {
		for (int i = 0; i < types.size(); i++) {
			if (types.get(i) != null) {
				System.out.println(i + ". " + types.get(i).getName());

			}

		}

	}

	public void userShiftsReport(User user, int opt,int order) throws FileNotFoundException {
		User u = user;
		ArrayList<Shift> shiftsCopy= user.getShiftsHistory();
		if(order==1) {
			selectionSortShiftById(shiftsCopy);			
		}
		else if(order==0) {
			selectionSortShiftById(shiftsCopy);
			Collections.reverse(shiftsCopy);
		}
		if (u != null && opt == 0) {
			System.out.println("//////////////////////////////////////////////////////");
			System.out.println("/////Historial de turnos del usuario " + u.getName() + " " + u.getLastName() + "/////");
			System.out.println("/////////////////////////////////////////////////////");
			for (int i = 0; i < shiftsCopy.size(); i++) {
				System.out.println("Codigo: " + shiftsCopy.get(i).toString() + " Estaba presente?: "
						+ (shiftsCopy.get(i).getState() != null ? shiftsCopy.get(i).getState()
								: "No ha sido atendido"));
			}

		}

		else {
			PrintWriter pw = new PrintWriter("data/Shifts_Report_" + user.getId() + ".txt");
			for (int i = 0; i < user.getShiftsHistory().size(); i++) {
				pw.write("Codigo: " + u.getShiftsHistory().get(i).toString() + " "
						+ (u.getShiftsHistory().get(i).getUser() != null ? "No ha sido atendido " : "Atendido ")
						+ "Estaba presente?: "
						+ (u.getShiftsHistory().get(i).getState() != null ? u.getShiftsHistory().get(i).getState()
								: "No ha sido atendido"));
			}

			pw.close();
		}
	}

	public void UserByShiftReport(Shift shift, int opt, int order) throws IOException {

		Shift sh = shift;
		ArrayList<User> usersCopy = users;
		if (order == 0) {
			insertionSortUsersByLastNameDesc(usersCopy);
		} else if (order == 1) {
			bubbleSortUsersByLastName(usersCopy);
		}
		if (sh != null && opt == 0) {
			System.out.println("//////////////////////////////////////////////////////");
			System.out.println("/////Historial de usuarios que han utilizado el turno " + sh.toString() + "/////");
			System.out.println("/////////////////////////////////////////////////////");
			for (int i = 0; i < usersCopy.size(); i++) {
				for (int j = 0; j < usersCopy.get(i).getShiftsHistory().size(); j++) {
					if (usersCopy.get(i).getShiftsHistory().get(j).toString().equalsIgnoreCase(sh.toString())) {
						System.out.println("id" + usersCopy.get(i).getId() + " Nombre: " + usersCopy.get(i).getName()
								+ " Apellido: " + usersCopy.get(i).getLastName());
						j = usersCopy.get(i).getShiftsHistory().size();
					}
				}
			}
		} else {
			PrintWriter pw = new PrintWriter("data/Users_Report" + shift.toString() + ".txt");
			for (int i = 0; i < usersCopy.size(); i++) {
				for (int j = 0; j < usersCopy.get(i).getShiftsHistory().size(); j++) {
					if (usersCopy.get(i).getShiftsHistory().get(j).toString().equalsIgnoreCase(sh.toString())) {
						pw.write("id" + usersCopy.get(i).getId() + " Nombre: " + usersCopy.get(i).getName()
								+ " Apellido: " + usersCopy.get(i).getLastName());
						j = usersCopy.get(i).getShiftsHistory().size();
					}
				}
			}
			pw.close();
		}

	}

	public int findShiftBinary(Shift shift) {
		Comparator<Shift> comp = new ShiftIDComparator();
		return Collections.binarySearch(shifts, shift, comp);
	}

	public void suspendUsers() {
		for (int i = 0; i < users.size(); i++) {
			ArrayList<Shift> history = users.get(i).getShiftsHistory();
			Comparator<Shift> c = new ShiftTimeComparator();
			history.sort(c);
			Collections.reverse(history);
			Calendar time = Calendar.getInstance();
			time.setTimeInMillis(Calendar.getInstance().getTimeInMillis() - date.getTime());
			if (history.size() >= 2 && (time.getTimeInMillis()) > users.get(i).getPenalty()) {
				if (history.get(0).getState().equals(Shift.NO_ATTENDED)
						&& history.get(1).getState().equals(Shift.NO_ATTENDED)) {
					time.add(Calendar.HOUR, 48);
					users.get(i).setPenalty(time.getTimeInMillis());
					System.out.println("El usuario " + users.get(i).getName() + " " + users.get(i).getLastName()
							+ " fue suspendido hasta " + time.get(Calendar.DAY_OF_MONTH) + "/"
							+ time.get(Calendar.MONTH) + "/" + time.get(Calendar.YEAR));
				}
			}
		}
	}

	public void saveUsers(ArrayList<User> u) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter("data/Users.txt");
		for (int i = 0; i < u.size(); i++) {
			pw.write(u.get(i).getTypeId() + "," + u.get(i).getId() + "," + u.get(i).getName() + ","
					+ u.get(i).getLastName() + "," + u.get(i).getTelephone() + "," + u.get(i).getAdress() + ","
					+ u.get(i).getPenalty());
			pw.write("\n");
		}
		pw.close();
	}

	public void saveShiftsHist(ArrayList<Shift> s) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter("data/Shifts.txt");
		for (int i = 0; i < s.size(); i++) {
			pw.write(s.get(i).toString() + "," + s.get(i).getUser().getId() + "," + s.get(i).getUser().getTypeId() + ","
					+ s.get(i).getState() + "," + s.get(i).getTime().getTime());
			pw.write("\n");
		}
		Comparator<Shift> c = new ShiftIDComparator();
		s.sort(c);
		pw.close();
	}

	public void loadShiftsHist() throws IOException {
		File file = new File("data/Shifts.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String str = br.readLine();
		while (str != null) {

			String data[] = str.split(",");
			String shiftID = data[0];
			String userID = data[1];
			int typeID = Integer.parseInt(data[2]);
			String shiftState = data[3];
			long time = Long.parseLong(data[4]);
			Shift s = new Shift(shiftID.charAt(0), Integer.parseInt(shiftID.substring(1)), null);
			s.setState(shiftState);
			s.setTime(new DateHour(time));
			User u = findUserbyID(userID, typeID);
			s.setUser(u);
			historyShifts.add(s);
			u.getShiftsHistory().add(s);

			str = br.readLine();
		}
	}

	public void loadUsers() throws IOException {
		File file = new File("data/Users.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String str = br.readLine();
		while (str != null) {

			String data[] = str.split(",");
			int typeId = Integer.parseInt(data[0]);
			String id = data[1];
			String name = data[2];
			String lastName = data[3];
			String phone = data[4];
			String adress = data[5];
			long penalty = Long.parseLong(data[6]);
			User u = new User(typeId, id, name, lastName, phone, adress);
			u.setPenalty(penalty);
			users.add(u);
			str = br.readLine();
		}
		br.close();
	}

	public void loadRandomUsers(String fileName, int q) throws IOException {

		File file = new File(fileName);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String str = br.readLine();
		int cont = 0;
		while (str != null && cont < q) {

			String data[] = str.split(",");
			int typeId = Integer.parseInt(data[0]);
			String id = data[1];
			String name = data[2];
			String lastName = data[3];
			String phone = data[4];
			String adress = data[5];

			users.add(new User(typeId, id, name, lastName, phone, adress));
			str = br.readLine();
			cont++;
		}

	}

	public void insertionSortUsersByLastNameDesc(ArrayList<User> arr) {
		int n = arr.size();

		for (int i = 1; i < n; i++) {
			System.out.println("Sort Pass Number " + (i));
			User key = arr.get(i);
			int j = i - 1;

			while ((j > -1) && (arr.get(j).getLastName().compareTo(key.getLastName()) < 0)) {
				arr.set(j + 1, arr.get(j));
				j--;
			}
			arr.set(j + 1, key);
		}
	}

	public void bubbleSortUsersByLastName(ArrayList<User> array) {
		int n = array.size();
		User temp = null;
		for (int i = 0; i < n; i++) {
			System.out.println("Sort Pass Number " + (i + 1));
			for (int j = 1; j < (n - i); j++) {
				if (array.get(j - 1).getLastName().compareTo(array.get(j).getLastName()) > 0) {
					temp = array.get(j - 1);
					array.set((j - 1), array.get(j));
					array.set(j, temp);
				}

			}
		}
	}

	public static void selectionSortShiftById(ArrayList<Shift> array) {
		for (int i = 0; i < array.size() - 1; i++) {
			int index = i;
			for (int j = i + 1; j < array.size(); j++) {
				if (array.get(j).toString().compareTo(array.get(index).toString()) < 0) {
					index = j;
				}
			}
			Shift shift = array.get(index);
			array.set(index, array.get(i));
			array.set(i, shift);
		}
	}

}// fin clase