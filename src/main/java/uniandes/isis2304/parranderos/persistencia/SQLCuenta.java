package uniandes.isis2304.parranderos.persistencia;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Cuenta;

public class SQLCuenta {
	private SQLUtil sqlUtil;
	
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
	public SQLCuenta (PersistenciaParranderos pp)
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
	public long adicionarCuenta (PersistenceManager pm, long idCuenta, String tipo,long saldo,String cliente,String gerente) 
	{
	
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCuenta  () + "(id, tipo,saldo,cliente,gerente) values (?,?,?,?,?)");
        q.setParameters(idCuenta, tipo,saldo,cliente,gerente);
        return (long) q.executeUnique();            
	}

	public long cambioCuenta (PersistenceManager pm, String cliente,long id,long saldo) 
	{

		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCuenta() + " SET saldo=saldo+?  WHERE cliente = ? AND id=? AND tipo != 'cerrado'");
		q.setParameters(saldo,cliente,id);
		
        return (long) q.executeUnique();
	}
	public List<Cuenta> verificar (PersistenceManager pm, long id) 
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCuenta ()+" WHERE  id= ?");
        q.setResultClass(Cuenta.class);
        q.setParameters(id);
        
        return (List<Cuenta>) q.executeList();
    }
	
	public long cerrarCuenta (PersistenceManager pm,long id) 
	{
	
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCuenta() + " SET tipo='cerrada' WHERE id=? AND saldo=0");
		q.setParameters(id);
	
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
	public List<Cuenta> darCuenta (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCuenta ());
		q.setResultClass(Cuenta.class);
		return (List<Cuenta>) q.execute();
	}
 
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar el identificador y el número de bebidas que sirven los bares de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de parejas de objetos, el primer elemento de cada pareja representa el identificador de un bar,
	 * 	el segundo elemento representa el número de bebidas que sirve (Una bebida que se sirve en dos horarios cuenta dos veces)
	 */

	public void adicionarOperacion(PersistenceManager pm, long id, String tipo, String consignador, long idconsignador, String destinatario, long iddestinatario, long monto, String fecha) {
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaOperaciones () + "(id,tipo,consignador,idconsignador,destinatario,iddestinatario,monto,fecha) values (?,?,?,?,?,?,?,?)");
        q.setParameters(id,tipo,consignador,idconsignador,destinatario,iddestinatario,monto,new Timestamp (System.currentTimeMillis()));
        q.executeUnique();
	}

}
