package uniandes.isis2304.parranderos.persistencia;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Cliente;
import uniandes.isis2304.parranderos.negocio.Consigna;
import uniandes.isis2304.parranderos.negocio.Cuenta;
import uniandes.isis2304.parranderos.negocio.GerenteOficina;
import uniandes.isis2304.parranderos.negocio.Operaciones;
import uniandes.isis2304.parranderos.negocio.Prestamo;
import uniandes.isis2304.parranderos.negocio.TipoBebida;
import uniandes.isis2304.parranderos.negocio.Usuario;


class SQLOperaciones
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
	public SQLOperaciones (PersistenciaParranderos pp)
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
	
	public List<Operaciones> darOperaciones (PersistenceManager pm) 
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOperaciones ());
        q.setResultClass(Operaciones.class);
        return (List<Operaciones>) q.executeList();
    }
	 
	public List<Operaciones> buscarOperacionv2 (PersistenceManager pm,Timestamp Inicial,Timestamp Final) 
		 {
				System.out.println(Inicial);
		        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOperaciones ()+ " WHERE fecha BETWEEN ? AND ?");
		        q.setParameters(Inicial,Final);
		        q.setResultClass(Operaciones.class);
		        return (List<Operaciones>) q.executeList();
		 }
		
	

}