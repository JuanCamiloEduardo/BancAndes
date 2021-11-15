package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.Prestamo;



class SQLPrestamo {
	private SQLUtil sqlUtil;
	
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
	public List<Prestamo> buscarPrestamo (PersistenceManager pm) 
    {
        Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPrestamo () );
        q.setResultClass(Prestamo.class);
        q.setParameters();
        return (List<Prestamo>) q.executeList();
    }
	
	public long adicionarPrestamo (PersistenceManager pm, long id, String tipo,String estado,String nombre,long monto,long interes,long numeroCuotas,String diaPaga,long valorCuota,String gerente) 
	{

        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPrestamo () + "(id,tipo,estado,nombre,monto,interes,numeroCuotas,diaPaga,valorCuota,gerente) values (?, ?, ?,?,?,?,?,?,?,?)");

        q.setParameters(id, tipo, estado,nombre,monto,interes,numeroCuotas,diaPaga,valorCuota,gerente);

        return (long)q.executeUnique();            
	}
	
	public long cambioPrestamo (PersistenceManager pm, String nombre,long id,long monto) 
	{
	
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaPrestamo() + " SET monto=monto-? WHERE nombre = ? AND id=? AND ?>valorcuota");
		q.setParameters(monto,nombre,id,monto);
	
        return (long) q.executeUnique();
	}
	
	public long cerrarPrestamo (PersistenceManager pm, String nombre,long id) 
	{
	
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaPrestamo() + " SET estado='cerrado'  WHERE nombre = ? AND id=? AND monto=0");
		q.setParameters(nombre,id);
	
        return (long) q.executeUnique();
	}
	
	public List<Prestamo> darPrestamo (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPrestamo ());
		q.setResultClass(Prestamo.class);
		return (List<Prestamo>) q.execute();
	}
	
	public void adicionarOperacion(PersistenceManager pm, long id, String tipo, String consignador, long idconsignador, String destinatario, long iddestinatario, long monto, String fecha) {
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaOperaciones () + "(id,tipo,consignador,idconsignador,destinatario,iddestinatario,monto,fecha) values (?,?,?,?,?,?,?,?)");
		q.setParameters(id,tipo,consignador,idconsignador,destinatario,iddestinatario,monto,fecha);
        q.executeUnique();
	}
	
}
