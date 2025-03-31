package models;

import java.sql.Date;
import java.sql.Timestamp;

public class Session {
	
	public int id;
	public int idGromista;
	public int idMascota;
	public Date fecha;
	public String sesionStatus;
	public int idCliente;
	public int diference;
	public Timestamp horaInicio;
	public Timestamp horaFinal;
	public Timestamp horaCambioStatus;
	public int minutosRecepcion;
	
	public Session(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdGromista() {
		return idGromista;
	}

	public void setIdGromista(int idGromista) {
		this.idGromista = idGromista;
	}

	public int getIdMascota() {
		return idMascota;
	}

	public void setIdMascota(int idMascota) {
		this.idMascota = idMascota;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getSesionStatus() {
		return sesionStatus;
	}

	public void setSesionStatus(String sesionStatus) {
		this.sesionStatus = sesionStatus;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getDiference() {
		return diference;
	}

	public void setDiference(int diference) {
		this.diference = diference;
	}

	public Timestamp getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Timestamp horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Timestamp getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Timestamp horaFinal) {
		this.horaFinal = horaFinal;
	}

	public Timestamp getHoraCambioStatus() {
		return horaCambioStatus;
	}

	public void setHoraCambioStatus(Timestamp horaCambioStatus) {
		this.horaCambioStatus = horaCambioStatus;
	}

	public int getMinutosRecepcion() {
		return minutosRecepcion;
	}

	public void setMinutosRecepcion(int minutosRecepcion) {
		this.minutosRecepcion = minutosRecepcion;
	}
	
	
	

}
