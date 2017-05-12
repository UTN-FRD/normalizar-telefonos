package utn.frd.lsi.domain;

public class NumeroTelefonico {

	private Numeracion numeracion;
	private String numero;
	
	public NumeroTelefonico(){}

	public NumeroTelefonico(String prefijo, String bloque, String numero){
		this.numeracion = new Numeracion(prefijo, bloque);
		this.numero = numero;
	}
	
	public NumeroTelefonico(String numero) {
		this.numero = numero;
	}

	public NumeroTelefonico(Numeracion numeracion, String numero) {
		this.numeracion = numeracion;
		this.numero = numero;
	}

	public Numeracion getNumeracion() {
		return numeracion;
	}
	public void setNumeracion(Numeracion numeracion) {
		this.numeracion = numeracion;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Object getNumeroCompleto() {
		return this.numeracion.getPrefijo()+this.numeracion.getBloque()+this.numero;
	}

}
