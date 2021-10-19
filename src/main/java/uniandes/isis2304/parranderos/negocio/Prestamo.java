package uniandes.isis2304.parranderos.negocio;

public class Prestamo implements VOPrestamo {
 private long id;
 private String tipo;
 private String estado;
 private String nombre;
 private long monto;
 private long interes;
 private long numeroCuotas;
 private String diaPaga;
 private long valorCuota;
 
public Prestamo()
{
	this.id=0;
	this.tipo="";
	this.estado="";
	this.nombre="";
	this.monto=0;
	this.interes=0;
	this.numeroCuotas=0;
	this.diaPaga="";
	this.valorCuota=0;
	
}





public Prestamo(long id, String tipo, String estado, String nombre, long monto, long interes, long numeroCuotas,
		String diaPaga, long valorCuota) {
	super();
	this.id = id;
	this.tipo = tipo;
	this.estado = estado;
	this.nombre = nombre;
	this.monto = monto;
	this.interes = interes;
	this.numeroCuotas = numeroCuotas;
	this.diaPaga = diaPaga;
	this.valorCuota = valorCuota;
}





public long getMonto() {
	return monto;
}

public void setMonto(long monto) {
	this.monto = monto;
}

public long getInteres() {
	return interes;
}

public void setInteres(long interes) {
	this.interes = interes;
}

public long getNumeroCuotas() {
	return numeroCuotas;
}

public void setNumeroCuotas(long numeroCuotas) {
	this.numeroCuotas = numeroCuotas;
}

public long getValorCuota() {
	return valorCuota;
}

public void setValorCuota(long valorCuota) {
	this.valorCuota = valorCuota;
}


public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getTipo() {
	return tipo;
}
public void setTipo(String tipo) {
	this.tipo = tipo;
}
public String getEstado() {
	return estado;
}
public void setEstado(String estado) {
	this.estado = estado;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}


@Override
public String toString() {
	return "Prestamo [id=" + id + ", tipo=" + tipo + ", estado=" + estado + ", nombre=" + nombre + ", monto=" + monto
			+ ", interes=" + interes + ", numeroCuotas=" + numeroCuotas + ", diaPaga=" + diaPaga + ", valorCuota="
			+ valorCuota + "]";
}


public String getDiaPaga() {
	return diaPaga;
}


public void setDiaPaga(String diaPaga) {
	this.diaPaga = diaPaga;
}



}
