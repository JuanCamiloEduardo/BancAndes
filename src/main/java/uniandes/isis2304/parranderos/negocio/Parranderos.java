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

package uniandes.isis2304.parranderos.negocio;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import com.google.gson.JsonObject;
import uniandes.isis2304.parranderos.persistencia.PersistenciaParranderos;

/**
 * Clase principal del negocio
 * Sarisface todos los requerimientos funcionales del negocio
 *
 * @author Germán Bravo
 */
public class Parranderos 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	
	
	
	private static Logger log = Logger.getLogger(Parranderos.class.getName());
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaParranderos pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public Parranderos ()
	{
		pp = PersistenciaParranderos.getInstance ();
	}
	
	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public Parranderos (JsonObject tableConfig)
	{
		pp = PersistenciaParranderos.getInstance (tableConfig);
	}
	
	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		pp.cerrarUnidadPersistencia ();
	}
	
	public Usuario adicionarUsuario(String nombre,String login,String clave,long numeroDocumento,String tipoDocumento,String nacionalidad,String direccionFisica,String direccionElectronica,long telefono,String ciudad,String departamento,long codigoPostal)
	{
		System.out.println("1");
        log.info ("Adicionando usuario: " + nombre);
        System.out.println("2");
        Usuario tipoBebida = pp.adicionarUsuario(nombre,login,clave,numeroDocumento,tipoDocumento,nacionalidad,direccionFisica,direccionElectronica,telefono,ciudad,departamento,codigoPostal);		
        log.info ("Adicionando usuario: " + tipoBebida);
        return tipoBebida;
	}
	

	public Prestamo adicionarPrestamo(String tipo,String estado,String nombre,long monto,long interes,long numeroCuotas,String diaPaga,long valorCuota)
	{
		System.out.println("1");
        log.info ("Adicionando prestamo: " + nombre);
        System.out.println("2");
        Prestamo tipoBebida = pp.adicionarPrestamo (tipo,estado,nombre,monto,interes,numeroCuotas,diaPaga,valorCuota);		
        log.info ("Adicionando prestamo: " + tipoBebida);
        return tipoBebida;
	}
	public long operacionPrestamo (String nombre,long id,long valorCuota)
	{	
		System.out.println("0");
		log.info ("Realizando un cambio en el prestamo: " + id );
		long tb = pp.cambioPrestamo (nombre,id,valorCuota);
		System.out.println("10");
		log.info ("Aumentando sedes de bares de una ciudad: " + tb + " tuplas actualizadas");
		return tb;
	}
	
	public long operacionCuenta (String nombre,long id,long saldo)
	{	
		System.out.println("0");
		log.info ("Realizando un cambio en el prestamo: " + id );
		long tb = pp.cambioCuenta (nombre,id,saldo);
		System.out.println("10");
		log.info ("Aumentando sedes de bares de una ciudad: " + tb + " tuplas actualizadas");
		return tb;
	}
	public long cerrarPrestamo (String nombre,long id)
	{	
		System.out.println("0");
		log.info ("Realizando un cambio en el prestamo: " + id );
		long tb = pp.cerrarPrestamo (nombre,id);
		System.out.println("10");
		log.info ("Aumentando sedes de bares de una ciudad: " + tb + " tuplas actualizadas");
		return tb;
	}
	
	public long cerrarCuenta (long id)
	{	
		System.out.println("0");
		log.info ("Realizando un cambio en la cuenta: " + id );
		long tb = pp.cerrarCuenta (id);
		return tb;
	}

	public Oficina adicionarOficina(String nombre,String direccion,String gerenteUsuario,long puntosDeAtencion)
	{
        log.info ("Adicionando oficina: " + nombre);
        Oficina oficina = pp.adicionarOficina(nombre,direccion,gerenteUsuario,puntosDeAtencion);		
        log.info ("Adicionando oficina: " + oficina);
        return oficina;
	}
	
	public PuntoDeAtencion adicionarPuntoDeAtencion(String tipo, String localizacion, String oficina)
	{
        log.info ("Adicionando punto de atencion");
        PuntoDeAtencion puntoDeAtencion = pp.adicionarPuntoDeAtencion(tipo,localizacion,oficina);		
        log.info ("Adicionando punto de atencion");
        return puntoDeAtencion;
	}
	
	public Cuenta adicionarCuenta(String tipo, long saldo, String cliente, String gerente)
	{
        log.info ("Adicionando cuenta");
        Cuenta cuenta = pp.adicionarCuenta(tipo,saldo,cliente,gerente);		
        log.info ("Adicionando cuenta");
        return cuenta;
	}
	

	public String darUsuario (String login,String clave)
	{
		log.info ("Buscando Tipo de bebida por nombre: " + login);
		List<Usuario> tb = new ArrayList<Usuario>();
		tb = pp.darUsuario ();
		int numero = 0;
		for(int i=0; i<(tb.size()); i++) {
			if (login.equals(tb.get(i).getLogin())){
				numero=i;
			}
		}
		String hola=tb.get(numero).getTipo();
		System.out.print(tb.get(numero));
		return hola;
	}
	
	public Consigna adicionarConsigna(String jefe, long idJefe, String empleado, long idEmpleado, long monto,String fecha, String frecuencia)
    {
        System.out.println("Aja1");
		log.info ("Adicionando Consigna: " + jefe);
        Consigna nuevaconsigna = pp.adicionarConsigna (jefe,idJefe,empleado,idEmpleado,monto,fecha,frecuencia);
        log.info ("Adicionando Consigna: " + nuevaconsigna);
        return nuevaconsigna;
    }

	public long operacionCuentaV2 (String nombreConsignador,long idConsignador,long saldo,String nombreDestino,long idDestino)
	{	
		System.out.println("0");
		log.info ("Realizando un cambio en las cuentas");
		long tb = pp.cambioCuentaV2 (nombreConsignador,idConsignador,saldo,nombreDestino,idDestino);
		System.out.println("10");
		return tb;
	}
	
	public void verificarPagosAutomaticos(LocalDate fecha) {
		
		pp.verificarPagosAutomaticos(fecha);
		
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los BEBEDORES
	 *****************************************************************/
	
	/* ****************************************************************
	 * 			Métodos para manejar la relación GUSTAN
	 *****************************************************************/

	/* ****************************************************************
	 * 			Métodos para manejar la relación SIRVEN
	 *****************************************************************/

	/* ****************************************************************
	 * 			Métodos para manejar la relación VISITAN
	 *****************************************************************/

	/* ****************************************************************
	 * 			Métodos para administración
	 *****************************************************************/
}
