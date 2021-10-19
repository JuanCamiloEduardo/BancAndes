package uniandes.isis2304.parranderos.negocio;

public interface VOOficina {
	public long getId();
	public String getDireccion();
	public String getNombre();
	public String getGerenteUsuario();
	public long getNumeroPuntoAtencion();
	@Override
	public String toString();
}
