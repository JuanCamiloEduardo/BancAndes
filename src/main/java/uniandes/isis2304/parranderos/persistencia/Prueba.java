package uniandes.isis2304.parranderos.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

class Prueba {
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
	
	public Prueba (PersistenciaParranderos pp)
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
	public long adicionarUsuario (PersistenceManager pm, long idTipoBebida, String nombre) 
	{
		System. out. println("6");
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaUsuario  () + "(id, nombre) values (?, ?)");
        System. out. println("7");
        q.setParameters(idTipoBebida, nombre);
        System. out. println("8");
        return (long) q.executeUnique();            
	}
	public void funciona() {
		System. out. println("Funciona");
	}
}
