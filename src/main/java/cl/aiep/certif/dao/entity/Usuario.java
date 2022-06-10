package cl.aiep.certif.dao.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String rut;
	@Column (nullable = false)
	private String nombre;
	@Column (nullable = false)
	private String email;
	@Column (nullable = false)
	private String password;
	@Column (nullable = false)
	private Integer enabled;
	
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	public Usuario(String rut, String nombre, String email, String password, Integer enabled) {
		super();
		this.rut = rut;
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
	}
	public Usuario(String rut, String nombre, String email) {
		super();
		this.rut = rut;
		this.nombre = nombre;
		this.email = email;
	}
	
	public Usuario() {
		
	}


	
}
