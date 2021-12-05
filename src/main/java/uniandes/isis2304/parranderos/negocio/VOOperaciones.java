package uniandes.isis2304.parranderos.negocio;

import java.sql.Timestamp;

public interface VOOperaciones {

	public long getId();
	public String getTipo();
	public String getConsignador();
	public long getIdConsignador();
	public String getDestinatario();
	public long getIdDestinatario();
	public long getMonto();
	public Timestamp getFecha();
	
	
	
}
