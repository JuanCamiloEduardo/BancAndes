package uniandes.isis2304.parranderos.negocio;

public class Consigna implements VOConsigna{
	
	private String jefe;
	private String empleado;
	private long monto;
	private String fecha;
	
	public Consigna() {
		super();
		this.jefe = "";
		this.empleado = "";
		this.monto = 0;
		this.fecha = "";
	}
	
	public Consigna(String jefe, String empleado, long monto, String fecha) {
		super();
		this.jefe = jefe;
		this.empleado = empleado;
		this.monto = monto;
		this.fecha = fecha;
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
