package uniandes.isis2304.parranderos.negocio;

public class PuntoDeAtencion implements VOPuntoDeAtencion{
	private long id;
	private String tipo;
	private String localizacion;
	private String oficina;
	
	public PuntoDeAtencion() {
		super();
		this.id = 0;
		this.tipo = "";
		this.localizacion = "";
		this.oficina = "";
	}
	
	public PuntoDeAtencion(long id, String tipo, String localizacion, String oficina) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.localizacion = localizacion;
		this.oficina = oficina;
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

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	@Override
	public String toString() {
		return "PuntoDeAtencion [id=" + id + ", tipo=" + tipo + ", localizacion=" + localizacion + ", oficina="
				+ oficina + "]";
	}
	
}
