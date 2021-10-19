package uniandes.isis2304.parranderos.negocio;

public class Cuenta implements VOCuenta{
	
	private long id;
	private String tipo;
	private String cliente;
	private String gerente;
	
	public Cuenta() {
		super();
		this.id = 0;
		this.tipo = "";
		this.cliente = "";
		this.gerente = "";
	}
	
	public Cuenta(long id, String tipo, String cliente, String gerente) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.cliente = cliente;
		this.gerente = gerente;
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

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getGerente() {
		return gerente;
	}

	public void setGerente(String gerente) {
		this.gerente = gerente;
	}

	@Override
	public String toString() {
		return "Cuenta [id=" + id + ", tipo=" + tipo + ", cliente=" + cliente + ", gerente=" + gerente + "]";
	}
	
}
