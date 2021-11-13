package uniandes.isis2304.parranderos.negocio;

public class Consigna implements VOConsigna{
	
	private String jefe;
	private long idJefe;
	private String empleado;
	private long idEmpleado;
	private long monto;
	private String fecha;
	private String frecuencia;
	
	public Consigna() {
		super();
		this.jefe = "";
		this.idJefe = 0;
		this.empleado = "";
		this.idEmpleado = 0;
		this.monto = 0;
		this.fecha = "";
		this.frecuencia = "";
	}

	public Consigna(String jefe, long idJefe, String empleado, long idEmpleado, long monto, String fecha,
			String frecuencia) {
		super();
		this.jefe = jefe;
		this.idJefe = idJefe;
		this.empleado = empleado;
		this.idEmpleado = idEmpleado;
		this.monto = monto;
		this.fecha = fecha;
		this.frecuencia = frecuencia;
	}

	public long getIdJefe() {
		return idJefe;
	}

	public void setIdJefe(long idJefe) {
		this.idJefe = idJefe;
	}

	public long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	public String getJefe() {
		return jefe;
	}

	public void setJefe(String jefe) {
		this.jefe = jefe;
	}

	public String getEmpleado() {
		return empleado;
	}

	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}

	public long getMonto() {
		return monto;
	}

	public void setMonto(long monto) {
		this.monto = monto;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "Consigna [jefe=" + jefe + ", empleado=" + empleado + ", monto=" + monto + ", fecha=" + fecha + "]";
	}
	
}
