package utn.frd.lsi.domain;

import org.json.JSONObject;


public class Numeracion {
	//{"SERVICIO":"PCS","OPERADOR":"AMX ARGENTINA SOCIEDAD ANONIMA","FECHA":"23-Apr-08","RESOLUCION":"SC 65/08",
	//"LOCALIDAD":"PILAR (PROV.  BUENOS AIRES)","MODALIDAD":"CPP","BLOQUE":"435","INDICATIVO":"230"}
	private String servicio;
	private String operador;
	private String fecha;
	private String resolucion;
	private String localidad;
	private String modalidad;
	private String bloque;
	private String indicativo;
	
	public Numeracion(){}
	
	public Numeracion(JSONObject json){
		if(json.has("SERVICIO"))
			this.servicio = json.getString("SERVICIO");
		if(json.has("OPERADOR"))
			this.operador = json.getString("OPERADOR");
		if(json.has("FECHA"))
			this.fecha = json.getString("FECHA");
		if(json.has("RESOLUCION"))
			this.resolucion = json.getString("RESOLUCION");
		if(json.has("LOCALIDAD"))
			this.localidad = json.getString("LOCALIDAD");
		if(json.has("MODALIDAD"))
			this.modalidad = json.getString("MODALIDAD");
		if(json.has("BLOQUE"))
			this.bloque = json.getString("BLOQUE");
		if(json.has("INDICATIVO"))
			this.indicativo = json.getString("INDICATIVO");
	}
	
	public Numeracion(String prefijo, String bloque) {
		this.indicativo = prefijo;
		this.bloque = bloque;
	}

	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public String getOperador() {
		return operador;
	}
	public void setOperador(String operador) {
		this.operador = operador;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getResolucion() {
		return resolucion;
	}
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getModalidad() {
		return modalidad;
	}
	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	public String getBloque() {
		return bloque;
	}
	public void setBloque(String bloque) {
		this.bloque = bloque;
	}
	public String getIndicativo() {
		return indicativo;
	}
	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}

	public String getPrefijo() {
		return indicativo;
	}
	
	
}
