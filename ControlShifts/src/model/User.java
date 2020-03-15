package model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Shift shift;
	private ArrayList<Shift> shiftsHistory;
	
	public final static int CEDULA_CIUDADANIA= 1;
	public final static int TARJETA_IDENTIDAD= 2;
	public final static int  REGISTROCIVIL= 3;
	public final static int PASAPORTE= 4;
	public final static int CEDULA_EXTRANJERA= 5;
	private int typeId;
	private String id;
	private String name;
	private String lastName ;
	private String telephone;
	private String adress;	
	private long penalty;
	
	public Shift getShift() {
		return shift;
	}
	public void setShift(Shift shift) {
		this.shift = shift;
	}
	public User(int typeId, String id, String name, String lastName, String telephone, String adress) {
		super();
		shiftsHistory= new ArrayList<Shift>();
		this.typeId = typeId;
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.telephone = telephone;
		this.adress = adress;
		shift=null;
		penalty=0;
	}
	
	
	
	public long getPenalty() {
		return penalty;
	}
	public void setPenalty(long penalty) {
		this.penalty = penalty;
	}
	public ArrayList<Shift> getShiftsHistory() {
		return shiftsHistory;
	}
	public void setShiftsHistory(ArrayList<Shift> shiftsHistory) {
		this.shiftsHistory = shiftsHistory;
	}
	/**
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}
	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * @return the adress
	 */
	public String getAdress() {
		return adress;
	}
	/**
	 * @param adress the adress to set
	 */
	public void setAdress(String adress) {
		this.adress = adress;
	}
	@Override
	public String toString() {
		return "Nombre=" + name + ", Apellido=" + lastName ;
	}

	
	
	
	
	
	
	
	
	

}
