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


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import uniandes.isis2304.parranderos.negocio.Bar;
import uniandes.isis2304.parranderos.negocio.Bebedor;
import uniandes.isis2304.parranderos.negocio.Bebida;
import uniandes.isis2304.parranderos.negocio.Cliente;
import uniandes.isis2304.parranderos.negocio.Consigna;
import uniandes.isis2304.parranderos.negocio.Cuenta;
import uniandes.isis2304.parranderos.negocio.GerenteOficina;
import uniandes.isis2304.parranderos.negocio.Gustan;
import uniandes.isis2304.parranderos.negocio.Prestamo;
import uniandes.isis2304.parranderos.negocio.Sirven;
import uniandes.isis2304.parranderos.negocio.TipoBebida;
import uniandes.isis2304.parranderos.negocio.Usuario;
import uniandes.isis2304.parranderos.negocio.Oficina;
import uniandes.isis2304.parranderos.negocio.Operaciones;
import uniandes.isis2304.parranderos.negocio.PuntoDeAtencion;
import uniandes.isis2304.parranderos.negocio.Visitan;

/**
 * Clase para el manejador de persistencia del proyecto Parranderos
 * Traduce la información entre objetos Java y tuplas de la base de datos, en ambos sentidos
 * Sigue un patrón SINGLETON (Sólo puede haber UN objeto de esta clase) para comunicarse de manera correcta
 * con la base de datos
 * Se apoya en las clases SQLBar, SQLBebedor, SQLBebida, SQLGustan, SQLSirven, SQLTipoBebida y SQLVisitan, que son 
 * las que realizan el acceso a la base de datos
 * 
 * @author Germán Bravo
 */
public class PersistenciaParranderos 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaParranderos.class.getName());
	
	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaParranderos instance;
	
	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;
	
	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, tipoBebida, bebida, bar, bebedor, gustan, sirven y visitan
	 */
	private List <String> tablas;
	
	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaParranderos
	 */
	private SQLUtil sqlUtil;
	
	private SQLUsuario sqlUsuario;

	private SQLPrestamo sqlPrestamo;

	private SQLOficina sqlOficina;
	private SQLPuntoDeAtencion sqlPuntoDeAtencion;
	private SQLCuenta sqlCuenta;
	private SQLConsigna sqlConsigna;
	private SQLOperaciones sqlOperaciones;


	/* ****************************************************************
	 * 			Métodos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaParranderos ()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("Parranderos");		
		crearClasesSQL ();
		
		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("Parranderos_sequence");
		tablas.add("USUARIO");
		tablas.add("Prestamo");
		tablas.add("OFICINA");
		tablas.add("PUNTODEATENCION");
		tablas.add("CUENTA");
		tablas.add("CLIENTE");
		tablas.add("GERENTEOFICINA");
		tablas.add("CONSIGNA");
		tablas.add("OPERACIONES");

}

	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto Json - Patrón SINGLETON
	 * @param tableConfig - Objeto Json que contiene los nombres de las tablas y de la unidad de persistencia a manejar
	 */
	private PersistenciaParranderos (JsonObject tableConfig)
	{
		crearClasesSQL ();
		tablas = leerNombresTablas (tableConfig);
		
		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
	}

	/**
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaParranderos getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaParranderos ();
		}
		return instance;
	}
	
	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto tableConfig
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaParranderos getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaParranderos (tableConfig);
		}
		return instance;
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}
	
	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}
		
		return resp;
	}
	
	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL ()
	{	
		sqlUsuario=new SQLUsuario(this);
		sqlOficina=new SQLOficina(this);
		sqlPuntoDeAtencion=new SQLPuntoDeAtencion(this);
		sqlCuenta=new SQLCuenta(this);
		sqlUtil = new SQLUtil(this);
		sqlPrestamo=new SQLPrestamo(this);
		sqlConsigna=new SQLConsigna(this);
		sqlOperaciones=new SQLOperaciones(this);
	}

	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de parranderos
	 */
	public String darSeqParranderos ()
	{
		return tablas.get (0);
	}

	/**
	 * Transacción para el generador de secuencia de Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return El siguiente número del secuenciador de Parranderos
	 */
	public String darTablaUsuario ()
	{
		return tablas.get (1);
	}
	
	public String darTablaOficina ()
	{
		return tablas.get (2);
	}
	
	public String darTablaPuntoDeAtencion ()
	{
		return tablas.get (3);
	}

	public String darTablaCuenta ()
	{
		return tablas.get (4);
	}
	
	public String darTablaPrestamo ()
	{
		return tablas.get (5);
	}
	
	public String darTablaConsigna ()
	{
		return tablas.get (6);
	}
	public String darTablaOperaciones ()
	{
		return tablas.get (7);
	}
	
	private long nextval ()
	{
        long resp = sqlUtil.nextval (pmf.getPersistenceManager());
        log.trace ("Generando secuencia: " + resp);
        return resp;
    }
	
	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la Exception e, que da el detalle específico del problema encontrado
	 * @param e - La excepción que ocurrio
	 * @return El mensaje de la excepción JDO
	 */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/* ****************************************************************
	 * 			Métodos para manejar los TIPOS DE BEBIDA
	 *****************************************************************/

	public Usuario adicionarUsuario(String nombre,String login,String clave,long numeroDocumento,String tipoDocumento,String nacionalidad,String direccionFisica,String direccionElectronica,long telefono,String ciudad,String departamento,long codigoPostal)
	{
		System. out. println("3");
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {    	
            tx.begin();
            long idUsuario = nextval ();
            long tuplasInsertadas = sqlUsuario.adicionarUsuario(pm, idUsuario, nombre,login,clave,numeroDocumento,tipoDocumento,nacionalidad,direccionFisica,direccionElectronica,telefono,ciudad,departamento,codigoPostal);
            tx.commit();
            
            log.trace ("Inserción de tipo de bebida: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Usuario (idUsuario,nombre,login,clave,numeroDocumento,tipoDocumento,nacionalidad,direccionFisica,direccionElectronica,telefono,ciudad,departamento,codigoPostal);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	
	
	public Oficina adicionarOficina(String nombre,String direccion,String gerenteUsuario,long puntosDeAtencion)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {   
            tx.begin();
            long idOficina = nextval ();
            long tuplasInsertadas = sqlOficina.adicionarOficina(pm, idOficina, nombre,direccion,gerenteUsuario,puntosDeAtencion);
            tx.commit();
            log.trace ("Inserción de oficina: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Oficina (idOficina,nombre,direccion,gerenteUsuario,puntosDeAtencion);

        }
        catch (Exception e)
        {
//       	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	

	public PuntoDeAtencion adicionarPuntoDeAtencion(String tipo,String localizacion,String oficina)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {   
            tx.begin();
            long idPuntoDeAtencion = nextval ();
            long tuplasInsertadas = sqlPuntoDeAtencion.adicionarPuntoDeAtencion(pm, idPuntoDeAtencion, tipo,localizacion,oficina);
            tx.commit();
            log.trace ("Inserción de punto de atencion: " + tuplasInsertadas + " tuplas insertadas");
            
            return new PuntoDeAtencion (idPuntoDeAtencion, tipo,localizacion,oficina);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public Prestamo adicionarPrestamo(String tipo,String estado,String nombre,long monto,long interes,long numeroCuotas,String diaPaga,long valorCuota,String gerente)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long idPrestamo = nextval ();
            
            long tuplasInsertadas = sqlPrestamo.adicionarPrestamo(pm, idPrestamo,tipo,estado,nombre,monto,interes,numeroCuotas,diaPaga,valorCuota,gerente);
            tx.commit();
            
            log.trace ("Inserción de tipo de bebida: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Prestamo (idPrestamo,tipo,estado, nombre,monto,interes,numeroCuotas,diaPaga,valorCuota,gerente);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	public long cambioPrestamo (String nombre,long id,long nuevaCuota)
	{
	
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlPrestamo.cambioPrestamo(pm, nombre,id,nuevaCuota);
          
          
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public long cambioCuenta (String nombre,long id,long saldo)
	{
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlCuenta.cambioCuenta(pm, nombre,id,saldo);
            
            long idOperacion = nextval ();
            if (saldo<0) {
            	sqlCuenta.adicionarOperacion(pm, idOperacion, "transferencia", nombre, Math.abs(id), "", 0, saldo, LocalDate.now().toString());
            }
            else {
            	sqlCuenta.adicionarOperacion(pm, idOperacion, "transferencia", "", 0, nombre, Math.abs(id), saldo, LocalDate.now().toString());
            }
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public long cambioPrestamov2 (String nombre,long id,long saldo,long idprestamo)
	{
	
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlCuenta.cambioCuenta(pm, nombre,id,(-1)*Math.abs(saldo));
            sqlPrestamo.cambioPrestamo(pm, nombre, idprestamo,Math.abs(saldo));
            long idOperacion = nextval ();
            sqlPrestamo.adicionarOperacion(pm, idOperacion, "prestamo", nombre, id, "", 0, Math.abs(saldo), LocalDate.now().toString());
          
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long cerrarPrestamo (String nombre,long id)
	{
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlPrestamo.cerrarPrestamo(pm, nombre,id);
           
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public long cerrarCuenta (long id)
	{
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlCuenta.cerrarCuenta(pm,id);
       
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public Cuenta adicionarCuenta(String tipo,long saldo,String cliente,String gerente)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {   
            tx.begin();
            long idCuenta = nextval ();
            long tuplasInsertadas = sqlCuenta.adicionarCuenta(pm, idCuenta, tipo,saldo,cliente,gerente);
            tx.commit();
            log.trace ("Inserción de cuenta: " + tuplasInsertadas + " tuplas insertadas");
            
            return new Cuenta (idCuenta, tipo,saldo,cliente,gerente);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public List<Prestamo> buscarPrestamo (List<String> LTipo,List<String> LEstado,List<String> LNombre,List<String> LID, List<String> LMonto,List<String> LInteres,List<String> LNumero,List<String> LValor,String nombre,boolean cliente,boolean gerente)
    {
		List<Prestamo> listaPrestamos=sqlPrestamo.buscarPrestamo (pmf.getPersistenceManager());
		List<Prestamo> list=new ArrayList<Prestamo>();
		list=Filtro(listaPrestamos,LTipo,LEstado,LNombre,LID,LMonto,LInteres,LNumero,LValor,nombre,cliente,gerente);

		
		
        return list;
    }
	public List<Prestamo> Filtro(List<Prestamo> listaPrestamos,List<String> LTipo,List<String> LEstado,List<String> LNombre,List<String> LID, List<String> LMonto,List<String> LInteres,List<String> LNumero,List<String> LValor,String nombre,boolean cliente,boolean gerente)
	{
	
    	long Mayor =Long.MAX_VALUE,MayorM=Long.MAX_VALUE,MayorI=Long.MAX_VALUE,MayorN=Long.MAX_VALUE,MayorV=Long.MAX_VALUE;
    	long Menor=-2,MenorM=-2,MenorI=-2,MenorN=-2,MenorV=-2;
    	long Igual=-2,IgualM=-2,IgualI=-2,IgualN=-2,IgualV=-2;
    	String igual="?||°°",igualE="?||°°",igualN="?||°°";
    	String esta="?||°°",estaE="?||°°",estaN="?||°°";
    	

    	if (LTipo.size()==2) {
   
    		igual=LTipo.get(0);
    		esta=LTipo.get(1);
    	}
    	
    
    	if (LEstado.size()==2) {
    		igualE=LEstado.get(0);
    		estaE=LEstado.get(1);
    	}
    	if (LNombre.size()==2) {
    		igualN=LNombre.get(0);
    		estaN=LNombre.get(1);
    	}

    	if (LID.size()==3) {
    		Mayor =Long.parseLong(LID.get(0));
    		Menor=Long.parseLong(LID.get(1));
    		Igual=Long.parseLong(LID.get(2));
    	}
    	if (LMonto.size()==3) {
    		MayorM =Long.parseLong(LMonto.get(0));
    		MenorM=Long.parseLong(LMonto.get(1));
    		IgualM=Long.parseLong(LMonto.get(2));
    	}
    	if (LInteres.size()==3) {
    		MayorI =Long.parseLong(LInteres.get(0));
    		MenorI=Long.parseLong(LInteres.get(1));
    		IgualI=Long.parseLong(LInteres.get(2));
    	}
    	if (LNumero.size()==3) {
    		MayorN =Long.parseLong(LNumero.get(0));
    		MenorN=Long.parseLong(LNumero.get(1));
    		IgualN=Long.parseLong(LNumero.get(2));
    	}
    	if (LValor.size()==3) {
    		MayorV =Long.parseLong(LValor.get(0));
    		MenorV=Long.parseLong(LValor.get(1));
    		IgualV=Long.parseLong(LValor.get(2));
    	}
    	
    	String oficina="895";
    	if (cliente)
    	{
    		igualN=nombre;
    		estaN="<<";
    	}
    	else if(gerente)
    	{
    		oficina=nombre;
    	}

        ArrayList<Prestamo> Cambios=new ArrayList<Prestamo>();
        for (int i = 0; i<listaPrestamos.size(); i++) {
        	Cambios.add(listaPrestamos.get(i));
        }
        ArrayList<Prestamo> PrimerFiltro=new ArrayList<Prestamo>();
    
        List<Prestamo> SegundoFiltro=new ArrayList<Prestamo>();
        PrimerFiltro=filtroString(esta,igual,Cambios,estaE,igualE,estaN,igualN,oficina);
   
        SegundoFiltro=filtrolong(Mayor,Menor,Igual,PrimerFiltro ,MayorM,MenorM,IgualM,MayorI,MenorI,IgualI,MayorN,MenorN,IgualN,MayorV,MenorV,IgualV);
       
    	
    	/*Pasar la de Arraylist a List"*/
        return SegundoFiltro;

//        	e.printStackTrace();

		
	}


	public List<Prestamo> filtrolong(long mayor,long menor,long igual,ArrayList<Prestamo> lista ,long mayorM,long menorM,long igualM,long mayorI,long menorI,long igualI,long mayorN,long menorN,long igualN,long mayorV,long menorV,long igualV)
	{
		List<Prestamo> list=new ArrayList<Prestamo>();
		ArrayList<Integer> Eliminar=new ArrayList<Integer>();
	
		
		for (int i = 0; i<lista.size(); i++)
		{	
			Prestamo index=lista.get(i);

			if(igual!=-2) {
				
			if( igual!=lista.get(i).getId() && !(lista.get(i).getId() > mayor && lista.get(i).getId()  < menor))
			{
				
				Eliminar.add(i);
			}
			}

			if(igualM!=-2) {
				
			if(igualM!=index.getMonto() && !(index.getMonto() > mayorM  &&  index.getMonto()  < menorM))
			{
				Eliminar.add(i);
			}
			}

			if(igualI!=-2) {
			if( igualI!=index.getInteres() && !(index.getInteres() > mayorI  &&  index.getInteres()  < menorI))
			{
				Eliminar.add(i);
			}
			}

			if(igualN!=-2) {
			if(igualN!=index.getNumeroCuotas() && !(index.getNumeroCuotas() > mayorN  &&  index.getNumeroCuotas()  < menorN))
			{
				Eliminar.add(i);
			}
			}

			if(igualV!=-2) {
			if(igualV!=index.getValorCuota() && !(index.getValorCuota() > mayorV  &&  index.getValorCuota()  < menorV))
			{
				Eliminar.add(i);
			}
	
			}
			
					
		}
		
		for (int j = 0; j<lista.size(); j++)
		{
			if(!(Eliminar.contains(j))) {
			list.add(lista.get(j));
			}
		}

		return list;


		
	}

	public ArrayList<Prestamo> filtroString(String esta,String igual,ArrayList<Prestamo> lista,String estaE,String igualE,String estaN,String igualN,String oficina )
	{
		

		ArrayList<Integer> Eliminar=new ArrayList<Integer>();
		ArrayList<Prestamo> Cambios=new ArrayList<Prestamo>();
		for (int i = 0; i<lista.size(); i++)
		{
			Prestamo index=lista.get(i);
			if(!esta.equals("?||°°")) 
			{
			if( !igual.equals(index.getTipo()) && !index.getTipo().contains(esta))
			{
				Eliminar.add(i);
			}	
			}
			
			
			


			if(!estaE.equals("?||°°")) 
			{
			if( !igualE.equals(index.getEstado()) && !index.getEstado().contains(estaE) )
			{ 
				Eliminar.add(i);
			}	
			}
	
			
			
			if(!estaN.equals("?||°°")) 
			{
				
				
			if( !igualN.equals(index.getNombre()) && !index.getNombre().contains(estaN)  )
			{
				
				Eliminar.add(i);
			}			
			}

			if (!oficina.equals("895") && !index.getGerente().equals(oficina)) 
			{
				Eliminar.add(i);
			}
		}
		
		for (int j = 0; j<lista.size(); j++)
		{
			if(!(Eliminar.contains(j))) {
			Cambios.add(lista.get(j));
			}
		}
		return Cambios;
		
	}

	
	
	
 
	
 
	/* ****************************************************************
	 * 			Métodos para manejar las BEBIDAS
	 *****************************************************************/
	
	

	/* ****************************************************************
	 * 			Métodos para manejar los BEBEDORES
	 *****************************************************************/
	
	/**
	 * Método privado para generar las información completa de las visitas realizadas por un bebedor: 
	 * La información básica del bar visitado, la fecha y el horario, en el formato esperado por los objetos BEBEDOR
	 * @param tuplas - Una lista de arreglos de 7 objetos, con la información del bar y de la visita realizada, en el siguiente orden:
	 *   bar.id, bar.nombre, bar.ciudad, bar.presupuesto, bar.cantsedes, vis.fechavisita, vis.horario
	 * @return Una lista de arreglos de 3 objetos. El primero es un objeto BAR, el segundo corresponde a la fecha de la visita y
	 * el tercero corresponde al horaario de la visita
	 */
	private List<Object []> armarVisitasBebedor (List<Object []> tuplas)
	{
		List<Object []> visitas = new LinkedList <Object []> ();
		for (Object [] tupla : tuplas)
		{
			long idBar = ((BigDecimal) tupla [0]).longValue ();
			String nombreBar = (String) tupla [1];
			String ciudadBar = (String) tupla [2];
			String presupuestoBar = (String) tupla [3];
			int sedesBar = ((BigDecimal) tupla [4]).intValue ();
			Timestamp fechaVisita = (Timestamp) tupla [5];
			String horarioVisita = (String) tupla [6];
			
			Object [] visita = new Object [3];
			visita [0] = new Bar (idBar, nombreBar, ciudadBar, presupuestoBar, sedesBar);
			visita [1] = fechaVisita;
			visita [2] = horarioVisita;

			visitas.add (visita);
		}
		return visitas;
	}
	
	/**
	 * Método privado para generar las información completa de las bebidas que le gustan a un bebedor: 
	 * La información básica de la bebida, especificando también el nombre de la bebida, en el formato esperado por los objetos BEBEDOR
	 * @param tuplas - Una lista de arreglos de 5 objetos, con la información de la bebida y del tipo de bebida, en el siguiente orden:
	 * 	 beb.id, beb.nombre, beb.idtipobebida, beb.gradoalcohol, tipobebida.nombre
	 * @return Una lista de arreglos de 2 objetos. El primero es un objeto BEBIDA, el segundo corresponde al nombre del tipo de bebida
	 */
	private List<Object []> armarGustanBebedor (List<Object []> tuplas)
	{
		List<Object []> gustan = new LinkedList <Object []> ();
		for (Object [] tupla : tuplas)
		{			
			long idBebida = ((BigDecimal) tupla [0]).longValue ();
			String nombreBebida = (String) tupla [1];
			long idTipoBebida = ((BigDecimal) tupla [2]).longValue ();
			int gradoAlcohol = ((BigDecimal) tupla [3]).intValue ();
			String nombreTipo = (String) tupla [4];

			Object [] gusta = new Object [2];
			gusta [0] = new Bebida (idBebida, nombreBebida, idTipoBebida, gradoAlcohol);
			gusta [1] = nombreTipo;	
			
			gustan.add(gusta);
		}
		return gustan;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los BARES
	 *****************************************************************/
	
	
	
	/* ****************************************************************
	 * 			Métodos para manejar la relación GUSTAN
	 *****************************************************************/
	
	public List<Usuario> darUsuario ()
	{
		return sqlUsuario.darUsuario (pmf.getPersistenceManager());
	}
	
	public Consigna adicionarConsigna(String jefe, long idJefe, String empleado, long idEmpleado, long monto,String fecha, String frecuencia)
    {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
  
            tx.begin();
            long tuplasInsertadas = sqlConsigna.adicionarConsigna(pm,jefe,idJefe,empleado,idEmpleado,monto,fecha,frecuencia);
      
            tx.commit();

            log.trace ("Inserción de consigna: " + jefe + ": " + tuplasInsertadas + " tuplas insertadas");

            return new Consigna (jefe,idJefe,empleado,idEmpleado,monto,fecha,frecuencia);
        }
        catch (Exception e)
        {
//            e.printStackTrace();
            log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
    }
	


	
	public void eliminaryCrearConsigna(long cuentaEliminar,long nuevaId,String jefe) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        
        try
        {
            tx.begin();
            List<Cuenta> verificar =sqlCuenta.verificar(pm, nuevaId);
            if(verificar.isEmpty()) 
            {
            	return;
            }
            List<Consigna> lista = sqlConsigna.darConsignasEliminar(pm, jefe,cuentaEliminar);
            sqlConsigna.eliminarConsigna(pm, jefe, cuentaEliminar);
            for (int i = 0; i<lista.size(); i++) {
            	sqlConsigna.adicionarConsigna(pm,lista.get(i).getJefe(),nuevaId,lista.get(i).getEmpleado(),lista.get(i).getIdEmpleado(),lista.get(i).getMonto(),lista.get(i).getFecha(),lista.get(i).getFrecuencia());
            	
            	
            }
            
            tx.commit();
           lista.clear();


            return;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
		
	}
	
	public long cambioCuentaV2 (String nombreConsignador,long idConsignador,long saldo,String nombreDestino,long idDestino)
	{
	
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlCuenta.cambioCuenta(pm, nombreConsignador,idConsignador,(Math.abs(saldo))*(-1));
            sqlCuenta.cambioCuenta(pm, nombreDestino,idDestino,Math.abs(saldo));
            long idOperacion = nextval ();
            sqlCuenta.adicionarOperacion(pm, idOperacion, "transferencia", nombreConsignador, idConsignador, nombreDestino, idDestino, Math.abs(saldo), LocalDate.now().toString());
            
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public void verificarPagosAutomaticos(LocalDate fecha) {
		
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            
            List<Consigna> lista = sqlConsigna.darConsignas(pm, fecha);
            
            for (int i = 0; i<lista.size(); i++) {
            	
            	cambioCuentaV2(lista.get(i).getJefe(),lista.get(i).getIdJefe(),lista.get(i).getMonto(),lista.get(i).getEmpleado(),lista.get(i).getIdEmpleado());
            	
            }
            
            
            sqlConsigna.consignar15Dias(pm, fecha);
            sqlConsigna.consignar30Dias(pm, fecha);
            
            tx.commit();

            return;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
		
	}
	
	public List<Operaciones> darOperaciones() {
		
		return sqlOperaciones.darOperaciones (pmf.getPersistenceManager());
		
	}
	
	public List<Cuenta> darCuentas() {
		
		return sqlCuenta.darCuenta (pmf.getPersistenceManager());
		
	}

 }
