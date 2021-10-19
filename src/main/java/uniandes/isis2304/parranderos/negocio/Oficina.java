package uniandes.isis2304.parranderos.negocio;

public class Oficina implements VOOficina{
	private long id;
	private String direccion;
	private String nombre;
	private String gerenteUsuario;
	private long numeroPuntoAtencion;
	
	public Oficina () {
		this.id = 0;
		this.direccion = "";
		this.nombre = "";
		this.gerenteUsuario = "";
		this.numeroPuntoAtencion = 0;
	}

	public Oficina(long id, String direccion, String nombre, String gerenteUsuario, long numeroPuntoAtencion) {
		super();
		this.id = id;
		this.direccion = direccion;
		this.nombre = nombre;
		this.gerenteUsuario = gerenteUsuario;
		this.numeroPuntoAtencion = numeroPuntoAtencion;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getGerenteUsuario() {
		return gerenteUsuario;
	}

	public void setGerente(String gerenteUsuario) {
		this.gerenteUsuario = gerenteUsuario;
	}

	public long getNumeroPuntoAtencion() {
		return numeroPuntoAtencion;
	}

	public void setNumeroPuntoAtencion(long numeroPuntoAtencion) {
		this.numeroPuntoAtencion = numeroPuntoAtencion;
	}

	@Override
	public String toString() {
		return "Oficina [id=" + id + ", direccion=" + direccion + ", nombre=" + nombre + ", numeroPuntoAtencion="
				+ numeroPuntoAtencion + "]";
	}
	
}
