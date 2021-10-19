package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Cliente;

class SQLCliente {
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
	public SQLCliente (PersistenciaParranderos pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un TIPOBEBIDA a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idTipoBebida - El identificador del tipo de bebida
	 * @param nombre - El nombre del tipo de bebida
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarCliente (PersistenceManager pm, long idTipoBebida, String nombre,String login,String clave,long numeroDocumento,String tipoDocumento,String nacionalidad,String direccionFisica,String direccionElectronica,long telefono,String ciudad,String departamento,long codigoPostal) 
	{
		System. out. println("6");
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCliente  () + "(id, nombre,login,clave,numeroDocumento,tipoDocumento,nacionalidad,direccionFisica,direccionElectronica,telefono,ciudad,departamento,codigoPostal) values (?, ?,?,?,?,?, ?,?,?,?,?,?,?)");
        System. out. println("7");
        q.setParameters(idTipoBebida, nombre,login,clave,numeroDocumento,tipoDocumento,nacionalidad,direccionFisica,direccionElectronica,telefono,ciudad,departamento,codigoPostal);
        System. out. println("8");
        return (long) q.executeUnique();            
	}

	public List<Cliente> darTipoCliente (PersistenceManager pm,String login, String clave)
	{	
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCliente  () + " WHERE login = ? AND clave = ?");
		q.setResultClass(Cliente.class);
		System.out.print(q.toString());
		q.setParameters(login,clave);
		return (List<Cliente>) q.executeList();
	}

}
