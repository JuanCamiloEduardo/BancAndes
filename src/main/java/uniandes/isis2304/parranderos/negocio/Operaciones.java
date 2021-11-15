package uniandes.isis2304.parranderos.negocio;

public class Operaciones implements VOOperaciones{

	private long id;
	private String tipo;
	private String consignador;
	private long idConsignador;
	private String destinatario;
	private long idDestinatario;
	private long monto;
	private String fecha;
	
	public Operaciones() {
		super();
		this.id = 0;
		this.tipo = "";
		this.consignador = "";
		this.idConsignador = 0;
		this.destinatario = "";
		this.idDestinatario = 0;
		this.monto = 0;
		this.fecha = "";
	}
	
	public Operaciones(long id, String tipo, String consignador, long idConsignador, String destinatario,
			long idDestinatario, long monto, String fecha) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.consignador = consignador;
		this.idConsignador = idConsignador;
		this.destinatario = destinatario;
		this.idDestinatario = idDestinatario;
		this.monto = monto;
		this.fecha = fecha;
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

	public String getConsignador() {
		return consignador;
	}

	public void setConsignador(String consignador) {
		this.consignador = consignador;
	}

	public long getIdConsignador() {
		return idConsignador;
	}

	public void setIdConsignador(long idConsignador) {
		this.idConsignador = idConsignador;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public long getIdDestinatario() {
		return idDestinatario;
	}

	public void setIdDestinatario(long idDestinatario) {
		this.idDestinatario = idDestinatario;
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
		return "Operaciones [id=" + id + ", tipo=" + tipo + ", consignador=" + consignador + ", idConsignador="
				+ idConsignador + ", destinatario=" + destinatario + ", idDestinatario=" + idDestinatario + ", monto="
				+ monto + ", fecha=" + fecha + "]";
	}
	
}
