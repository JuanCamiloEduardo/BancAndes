package uniandes.isis2304.parranderos.negocio;

public interface VOPuntoDeAtencion {
	public long getId();
	public String getTipo();
	public String getLocalizacion();
	public String getOficina();
	@Override
	public String toString();
}
