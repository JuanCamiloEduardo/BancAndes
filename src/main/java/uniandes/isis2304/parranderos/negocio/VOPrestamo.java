package uniandes.isis2304.parranderos.negocio;

public interface VOPrestamo {
	public long getId();
	public String getTipo();
	public String getNombre();
	public String getEstado();
	public long getMonto();
	public long getInteres();
	public long getNumeroCuotas();
	public long getValorCuota();
	public String getDiaPaga();
	@Override
	public String toString();
	
}
