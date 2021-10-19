package uniandes.isis2304.parranderos.negocio;

public class Cuenta implements VOCuenta{
	
	private long id;
	private String tipo;
	private long saldo;
	private String cliente;
	private String gerente;
	
	public Cuenta() {
		super();
		this.id = 0;
		this.tipo = "";
		this.saldo = 0;
		this.cliente = "";
		this.gerente = "";
	}
	
	public Cuenta(long id, String tipo, long saldo, String cliente, String gerente) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.saldo = saldo;
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

	public long getSaldo() {
		return saldo;
	}

	public void setSaldo(long saldo) {
		this.saldo = saldo;
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
		return "Cuenta [id=" + id + ", tipo=" + tipo + ", saldo=" + saldo + ", cliente=" + cliente + ", gerente="
				+ gerente + "]";
	}
	
}
