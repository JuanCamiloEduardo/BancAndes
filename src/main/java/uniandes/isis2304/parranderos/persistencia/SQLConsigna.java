/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.parranderos.persistencia;

import java.time.LocalDate;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Cliente;
import uniandes.isis2304.parranderos.negocio.Consigna;
import uniandes.isis2304.parranderos.negocio.Cuenta;
import uniandes.isis2304.parranderos.negocio.GerenteOficina;
import uniandes.isis2304.parranderos.negocio.TipoBebida;
import uniandes.isis2304.parranderos.negocio.Usuario;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto SIRVEN de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLConsigna
{
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
	public SQLConsigna (PersistenciaParranderos pp)
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
	public long adicionarConsigna (PersistenceManager pm,String jefe, long idJefe, String empleado, long idEmpleado, long monto, String fecha,String frecuencia) 
    {
		System.out.println("Aja3");
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaConsigna  () + "(jefe,idJefe,empleado,idEmpleado,monto,fecha,frecuencia) values (?,?,?,?,?,?,?)");
        q.setParameters(jefe,idJefe,empleado,idEmpleado,monto,fecha,frecuencia);
        System.out.println("Aja4");
        return (long) q.executeUnique();
    }
	
	public void consignar15Dias(PersistenceManager pm, LocalDate fecha) {
		
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaConsigna() + " SET fecha=?  WHERE fecha = ? AND frecuencia='Q'");
		q.setParameters(fecha.plusDays(15).toString(),fecha.toString());
        q.executeUnique();
		
	}

	public void consignar30Dias(PersistenceManager pm, LocalDate fecha) {
		
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaConsigna() + " SET fecha=?  WHERE fecha = ? AND frecuencia='M'");
		q.setParameters(fecha.plusDays(30).toString(),fecha.toString());
        q.executeUnique();
		
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN SIRVEN de la base de datos de Parranderos, por sus identificador
	 * @param pm - El manejador de persistencia
	 * @param idBar - El identificador del bar
	 * @param idBebida - El identificador de la bebida
	 * @return EL número de tuplas eliminadas
	 */
 
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar el identificador y el número de bebidas que sirven los bares de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de parejas de objetos, el primer elemento de cada pareja representa el identificador de un bar,
	 * 	el segundo elemento representa el número de bebidas que sirve (Una bebida que se sirve en dos horarios cuenta dos veces)
	 */
	
	public List<Consigna> darConsignas (PersistenceManager pm, LocalDate fecha) 
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaConsigna ()+" WHERE FECHA = ?");
        q.setResultClass(Consigna.class);
        q.setParameters(fecha.toString());
        return (List<Consigna>) q.executeList();
    }

}