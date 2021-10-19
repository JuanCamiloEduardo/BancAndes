package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Prestamo;



class SQLPrestamo {
	
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
	public SQLPrestamo (PersistenciaParranderos pp)
	{
		this.pp = pp;
	}

	
	public long adicionarPrestamo (PersistenceManager pm, long id, String tipo,String estado,String nombre,long monto,long interes,long numeroCuotas,String diaPaga,long valorCuota) 
	{
		System.out.println("5");
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPrestamo () + "(id,tipo,estado,nombre,monto,interes,numeroCuotas,diaPaga,valorCuota) values (?, ?, ?,?,?,?,?,?,?)");
        System.out.println("6");
        q.setParameters(id, tipo, estado,nombre,monto,interes,numeroCuotas,diaPaga,valorCuota);
        System.out.println("7");
        return (long)q.executeUnique();            
	}
	
	public long cambioPrestamo (PersistenceManager pm, String nombre,long id,long monto) 
	{
		System.out.println("8");
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaPrestamo() + " SET monto=monto-? WHERE nombre = ? AND id=? AND ?>valorcuota");
		q.setParameters(monto,nombre,id,monto);
		System.out.println("9");
        return (long) q.executeUnique();
	}
	
	public long cerrarPrestamo (PersistenceManager pm, String nombre,long id) 
	{
		System.out.println("8");
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaPrestamo() + " SET estado='cerrado'  WHERE nombre = ? AND id=? AND monto=0");
		q.setParameters(nombre,id);
		System.out.println("9");
        return (long) q.executeUnique();
	}
	
	public List<Prestamo> darPrestamo (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPrestamo ());
		q.setResultClass(Prestamo.class);
		return (List<Prestamo>) q.execute();
	}
}
