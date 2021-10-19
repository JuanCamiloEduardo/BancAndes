package uniandes.isis2304.parranderos.negocio;

public class Cliente implements VOCliente{

	private long id;
	
	private String nombre;
	private String login;
	private String clave;
	private long numeroDocumento;
	private String tipoDocumento;
	private String nacionalidad;
	private String direccionFisica;
	private String direccionElectronica;
	private long telefono;
	private String ciudad;
	private String departamento;
	private long codigoPostal;
	
	public Cliente() {
		this.id = 0;
		this.nombre = "";
		this.clave="";
		this.numeroDocumento=0;
		this.tipoDocumento="";
		this.nacionalidad="";
		this.direccionFisica="";
		this.direccionElectronica="";
		this.telefono=0;
		this.ciudad="";
		this.departamento="";
		this.codigoPostal=0;
	}
	
	public Cliente(long id, String nombre, String login, String clave, long numeroDocumento, String tipoDocumento,
			String nacionalidad, String direccionFisica, String direccionElectronica, long telefono, String ciudad,
			String departamento, long codigoPostal) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.login = login;
		this.clave = clave;
		this.numeroDocumento = numeroDocumento;
		this.tipoDocumento = tipoDocumento;
		this.nacionalidad = nacionalidad;
		this.direccionFisica = direccionFisica;
		this.direccionElectronica = direccionElectronica;
		this.telefono = telefono;
		this.ciudad = ciudad;
		this.departamento = departamento;
		this.codigoPostal = codigoPostal;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public long getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(long numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getDireccionFisica() {
		return direccionFisica;
	}

	public void setDireccionFisica(String direccionFisica) {
		this.direccionFisica = direccionFisica;
	}

	public String getDireccionElectronica() {
		return direccionElectronica;
	}

	public void setDireccionElectronica(String direccionElectronica) {
		this.direccionElectronica = direccionElectronica;
	}

	public long getTelefono() {
		return telefono;
	}

	public void setTelefono(long telefono) {
		this.telefono = telefono;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public long getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(long codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", login=" + login + ", clave=" + clave
				+ ", numeroDocumento=" + numeroDocumento + ", tipoDocumento=" + tipoDocumento + ", nacionalidad="
				+ nacionalidad + ", direccionFisica=" + direccionFisica + ", direccionElectronica="
				+ direccionElectronica + ", telefono=" + telefono + ", ciudad=" + ciudad + ", departamento="
				+ departamento + ", codigoPostal=" + codigoPostal + "]";
	}
	
}
