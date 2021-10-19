package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.PuntoDeAtencion;

class SQLPuntoDeAtencion {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaParranderos.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaParranderos pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLPuntoDeAtencion (PersistenciaParranderos pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un SIRVEN a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idBar - El identificador del bar
	 * @param idBebida - El identificador de la bebida
	 * @param horario - El horario en que el bar sirve la bebida (DIURNO, NOCTURNO, TDOOS)
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarPuntoDeAtencion (PersistenceManager pm, long idPuntoDeAtencion, String tipo,String localizacion,String oficina) 
	{
		System. out. println("6");
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPuntoDeAtencion  () + "(id, tipo,localizacion,oficina) values (?,?,?,?)");
        q.setParameters(idPuntoDeAtencion, tipo,localizacion,oficina);
        return (long) q.executeUnique();            
	}


	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN SIRVEN de la base de datos de Parranderos, por sus identificador
	 * @param pm - El manejador de persistencia
	 * @param idBar - El identificador del bar
	 * @param idBebida - El identificador de la bebida
	 * @return EL número de tuplas eliminadas
	 */


	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los SIRVEN de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos SIRVEN
	 */
	public List<PuntoDeAtencion> darPuntoDeAtencion (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPuntoDeAtencion ());
		q.setResultClass(PuntoDeAtencion.class);
		return (List<PuntoDeAtencion>) q.execute();
	}
 
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar el identificador y el número de bebidas que sirven los bares de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de parejas de objetos, el primer elemento de cada pareja representa el identificador de un bar,
	 * 	el segundo elemento representa el número de bebidas que sirve (Una bebida que se sirve en dos horarios cuenta dos veces)
	 */


}
