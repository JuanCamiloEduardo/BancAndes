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

package uniandes.isis2304.parranderos.interfazApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.parranderos.negocio.Cuenta;
import uniandes.isis2304.parranderos.negocio.Operaciones;
import uniandes.isis2304.parranderos.negocio.Parranderos;
import uniandes.isis2304.parranderos.negocio.Prestamo;
import uniandes.isis2304.parranderos.negocio.PuntoDeAtencion;
import uniandes.isis2304.parranderos.negocio.VOCliente;
import uniandes.isis2304.parranderos.negocio.VOConsigna;
import uniandes.isis2304.parranderos.negocio.VOPrestamo;

import uniandes.isis2304.parranderos.negocio.VOCuenta;
import uniandes.isis2304.parranderos.negocio.VOGerenteOficina;
import uniandes.isis2304.parranderos.negocio.VOTipoBebida;
import uniandes.isis2304.parranderos.negocio.VOUsuario;
import uniandes.isis2304.parranderos.negocio.VOOficina;
import uniandes.isis2304.parranderos.negocio.VOOperaciones;
import uniandes.isis2304.parranderos.negocio.VOPuntoDeAtencion;


/**
 * Clase principal de la interfaz
 * @author Germán Bravo
 */
@SuppressWarnings("serial")


public class InterfazParranderosApp extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazParranderosApp.class.getName());
	
	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
    /**
     * Asociación a la clase principal del negocio.
     */
    private Parranderos parranderos;
    
	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
    /**
     * Objeto JSON con la configuración de interfaz de la app.
     */
    private JsonObject guiConfig;
    
    /**
     * Panel de despliegue de interacción para los requerimientos
     */
    private PanelDatos panelDatos;
    
    /**
     * Menú de la aplicación
     */
    private JMenuBar menuBar;
    private boolean adminBanc=false;
    private boolean gerenteOficina=false;
    private boolean gerenteGeneral=false;
    private boolean cajero=false;
    private boolean cliente=false;
    private String nombre = "";
    

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
    /**
     * Construye la ventana principal de la aplicación. <br>
     * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
     */
    public InterfazParranderosApp( )
    {
        // Carga la configuración de la interfaz desde un archivo JSON
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        // Configura la apariencia del frame que contiene la interfaz gráfica
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        parranderos = new Parranderos (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicació, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    /**
     * Método para configurar el frame principal de la aplicación
     */
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }

    /**
     * Método para crear el menú de la aplicación con base em el objeto JSON leído
     * Genera una barra de menú y los menús con sus respectivas opciones
     * @param jsonMenu - Arreglo Json con los menùs deseados
     */
    private void crearMenu(  JsonArray jsonMenu )
    {    	
    	// Creación de la barra de menús
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	// Creación de cada uno de los menús
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		// Creación de cada una de las opciones del menú
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
	public void adicionarUsuario( )
    {
   
    	try 
    	{	
    		if ( adminBanc || gerenteOficina)
        	{
    		String nombreTipo = JOptionPane.showInputDialog (this, "Nombre del usuario?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE);
    		String login = JOptionPane.showInputDialog (this, "Login?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE);
    		String clave = JOptionPane.showInputDialog (this, "Clave?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE);
    		long numeroDocumento =Integer.parseInt( JOptionPane.showInputDialog (this, "numeroDocumento?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE));
    		String tipoDocumento=JOptionPane.showInputDialog (this, "tipoDocumento?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE);
    		String nacionalidad=JOptionPane.showInputDialog (this, "nacionalidad?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE);
    		String direccionFisica=JOptionPane.showInputDialog (this, "direccionFisica?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE);
    		String direccionElectronica=JOptionPane.showInputDialog (this, "direccionElectronica?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE);
    		long telefono=Integer.parseInt(JOptionPane.showInputDialog (this, "telefono?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE));
    		String ciudad=JOptionPane.showInputDialog (this, "ciudad?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE);
    		String departamento=JOptionPane.showInputDialog (this, "Tipo Usuario?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE);
    		long codigoPostal=Integer.parseInt(JOptionPane.showInputDialog (this, "Codigopostal?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE));
    		if (nombreTipo != null && login!= null && clave!= null  && tipoDocumento!= null && nacionalidad!= null && direccionFisica!= null && direccionElectronica!= null && ciudad!= null && departamento!= null  )
    		{
    			
        		VOUsuario tb = parranderos.adicionarUsuario(nombreTipo,login,clave,numeroDocumento,tipoDocumento,nacionalidad,direccionFisica,direccionElectronica,telefono,ciudad,departamento,codigoPostal);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un usuario con nombre: " + nombreTipo);
        		}
        		String resultado = "En adicionarUsuario\n\n";
        		resultado += "Usuario adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("No es un administrado banca andes ni un gerente oficina esta accion no se puede realizar");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    public void adicionarPrestamo( )
    {
    	try 
    	{
    		if (gerenteOficina)
    		{
    		String estado=JOptionPane.showInputDialog (this, "Estado?", "Adicionar Prestamo", JOptionPane.QUESTION_MESSAGE);
    		String tipo =JOptionPane.showInputDialog (this, "Tipo de prestamo?", "Adicionar Prestamo", JOptionPane.QUESTION_MESSAGE);
    		String nombreTipo = JOptionPane.showInputDialog (this, "Nombre del cliente?", "Adicionar Prestamo", JOptionPane.QUESTION_MESSAGE);
    		long monto=Integer.parseInt(JOptionPane.showInputDialog (this, "Monto?", "Adicionar Prestamo", JOptionPane.QUESTION_MESSAGE));
    		long interes=Integer.parseInt(JOptionPane.showInputDialog (this, "Interes?", "Adicionar Prestamo", JOptionPane.QUESTION_MESSAGE));
    		long numeroCuotas=Integer.parseInt(JOptionPane.showInputDialog (this, "NumeroCuotas?", "Adicionar Prestamo", JOptionPane.QUESTION_MESSAGE));
    		String diaPaga=JOptionPane.showInputDialog (this, "Dia Pago Cuota?", "Adicionar Prestamo", JOptionPane.QUESTION_MESSAGE);
    		long valorCuota=Integer.parseInt(JOptionPane.showInputDialog (this, "Valor Cuota Minima?", "Adicionar Prestamo", JOptionPane.QUESTION_MESSAGE));
    		
    		if (nombreTipo != null && estado!= null && tipo!= null && diaPaga!=null )
    		{
    			
        		VOPrestamo tb = parranderos.adicionarPrestamo (tipo,estado,nombreTipo,monto,interes,numeroCuotas,diaPaga,valorCuota, nombre);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un tipo de bebida con nombre: " + nombreTipo);
        		}
        		String resultado = "En adicionarTipoBebida\n\n";
        		resultado += "Tipo de bebida adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    		}
    		else {
    			panelDatos.actualizarInterfaz("No es un gerente oficina");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    public void OperacionPrestamo( )
    {
    	try 
    	{
    		if(cajero)
    		{
    		String nombreTb = JOptionPane.showInputDialog (this, "Nombre del tipo de cliente?", "Operacion Prestamo", JOptionPane.QUESTION_MESSAGE);
    		long valorCuota = Integer.parseInt(JOptionPane.showInputDialog (this, "Cuota?", "Operacion Prestamo", JOptionPane.QUESTION_MESSAGE));
    		long id = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero ID del prestamo?", "Operacion Prestamo", JOptionPane.QUESTION_MESSAGE));
    		if (nombreTb != null )
    		{
    			parranderos.operacionPrestamo(nombreTb,id,valorCuota);
    			
    			
    			String resultado = "En buscar Tipo Bebida por nombre\n\n";
    			/*
    			if (tipoBebida != null)
    			{
        			resultado += "El tipo de bebida es: " + tipoBebida;
    			}
    			else
    			{
        			resultado += "Un tipo de bebida con nombre: " + nombreTb + " NO EXISTE\n";    				
    			}*/
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("No es un cajero");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void OperacionCuenta( )
    {
    	try 
    	{
    		if(cajero || cliente)
    		{
    		String nombreTb = JOptionPane.showInputDialog (this, "Nombre del tipo de cliente?", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE);
    		long saldo = Integer.parseInt(JOptionPane.showInputDialog (this, "Dinero(para sacar - y meter +) ?", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE));
    		long id = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero ID de la cuenta?", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE));
    		if (nombreTb != null && (nombreTb.equals(nombre) || cajero))
    		{
    			parranderos.operacionCuenta(nombreTb,id,saldo);
    			
    
    			String resultado = "En operacion cuenta\n\n";
    			/*
    			if (tipoBebida != null)
    			{
        			resultado += "El tipo de bebida es: " + tipoBebida;
    			}
    			else
    			{
        			resultado += "Un tipo de bebida con nombre: " + nombreTb + " NO EXISTE\n";    				
    			}*/
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    		}
    		else{
    			panelDatos.actualizarInterfaz("No es un cliente o cajero");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void cerrarPrestamo( )
    {
    	try 
    	{
    		if (gerenteOficina)
    		{
    		String nombreTb = JOptionPane.showInputDialog (this, "Nombre del cliente?", "Operacion Prestamo", JOptionPane.QUESTION_MESSAGE);
    		long id = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero ID del prestamo?", "Operacion Prestamo", JOptionPane.QUESTION_MESSAGE));
    		if (nombreTb != null )
    		{
    			parranderos.cerrarPrestamo(nombreTb,id);
    			
    			
    			String resultado = "En buscar Tipo Bebida por nombre\n\n";
    			/*
    			if (tipoBebida != null)
    			{
        			resultado += "El tipo de bebida es: " + tipoBebida;
    			}
    			else
    			{
        			resultado += "Un tipo de bebida con nombre: " + nombreTb + " NO EXISTE\n";    				
    			}*/
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else {
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("No es un gerente oficina");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void cerrarCuenta( )
    {
    	
    	try 
    	{
    		if (gerenteOficina)
        	{
    		long id = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero ID de la cuenta?", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE));
    		
    		parranderos.cerrarCuenta(id);
    			
    		
    		String resultado = "En cerrar cuenta por nombre\n\n";
    			/*
    			if (tipoBebida != null)
    			{
        			resultado += "El tipo de bebida es: " + tipoBebida;
    			}
    			else
    			{
        			resultado += "Un tipo de bebida con nombre: " + nombreTb + " NO EXISTE\n";    				
    			}*/
    		resultado += "\n Operación terminada";
    		panelDatos.actualizarInterfaz(resultado);
    		
    		
		}
    		else {
        		panelDatos.actualizarInterfaz("No es un gerente oficina");
        	}
    	}
    
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    public void adicionarOficina( )
    {
    	try 
    	{
    		if(adminBanc)
    		{
    		String nombre = JOptionPane.showInputDialog (this, "Nombre de la oficina?", "Adicionar oficina", JOptionPane.QUESTION_MESSAGE);
    		String direccion = JOptionPane.showInputDialog (this, "Direccion?", "Adicionar direccion", JOptionPane.QUESTION_MESSAGE);
    		String gerenteUsuario = JOptionPane.showInputDialog (this, "Usuario del gerente?", "Adicionar gerente", JOptionPane.QUESTION_MESSAGE);
    		long puntosDeAtencion =Integer.parseInt( JOptionPane.showInputDialog (this, "numero de puntos de atencion?", "Adicionar puntos de atencion", JOptionPane.QUESTION_MESSAGE));
    		
    		if (nombre != null && direccion!= null && gerenteUsuario!= null)
    		{
        		VOOficina tb = parranderos.adicionarOficina(nombre,direccion,gerenteUsuario,puntosDeAtencion);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear una oficina con nombre: " + nombre);
        		}
        		String resultado = "En adicionarOficina\n\n";
        		resultado += "Oficina adicionada exitosamente: " + tb;

    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    		}
   
    		else
    		{
    			panelDatos.actualizarInterfaz("No es un administrado banca andes");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}

    	
    }
    
    
    
    
    public void adicionarPuntoDeAtencion( )
    {
    	try 
    	{
    		if(adminBanc)
    		{
    			
    		
    		String tipo = JOptionPane.showInputDialog (this, "Tipo del punto de atencion?", "Adicionar tipo", JOptionPane.QUESTION_MESSAGE);
    		String localizacion = JOptionPane.showInputDialog (this, "Localizacion?", "Adicionar locaclizacion", JOptionPane.QUESTION_MESSAGE);
    		String oficina = JOptionPane.showInputDialog (this, "Id de la oficina en caso de tener?", "Adicionar oficina", JOptionPane.QUESTION_MESSAGE);
    		String cajero = JOptionPane.showInputDialog (this, "Login del cajero en caso de tener?", "Adicionar oficina", JOptionPane.QUESTION_MESSAGE);
    		
    		if (tipo != null && localizacion!= null)
    		{
        		VOPuntoDeAtencion tb = parranderos.adicionarPuntoDeAtencion(tipo,localizacion,oficina,cajero);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear el punto de atencion");
        		}
        		String resultado = "En adicionarPuntoDeAtencion\n\n";
        		resultado += "Punto de Atencion adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("No es un admin");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void adicionarCuenta( )
    {
    	try 
    	{
    		if (gerenteOficina) {
    		String tipo = JOptionPane.showInputDialog (this, "Tipo de la cuenta?", "Adicionar tipo", JOptionPane.QUESTION_MESSAGE);
    		long saldo =Integer.parseInt( JOptionPane.showInputDialog (this, "Saldo inicial de la cuenta?", "Adicionar saldo", JOptionPane.QUESTION_MESSAGE));
    		String cliente = JOptionPane.showInputDialog (this, "Cliente de la cuenta?", "Adicionar cliente", JOptionPane.QUESTION_MESSAGE);
    		String gerente = JOptionPane.showInputDialog (this, "Gerente de la cuenta?", "Adicionar gerente", JOptionPane.QUESTION_MESSAGE);
    		
    		if (tipo != null && cliente!= null && gerente!=null)
    		{
        		VOCuenta tb = parranderos.adicionarCuenta(tipo,saldo,cliente,gerente);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear la cuenta");
        		}
        		String resultado = "En adicionarCuenta\n\n";
        		resultado += "Cuenta adicionada exitosamente: " + tb;

    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    		}
    		else {
    			panelDatos.actualizarInterfaz("No es un gerente Oficina");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void OperacionPrestamov2( )
    {
    	try 
    	{
    		if(cajero || cliente)
    		{
    		String nombreTb = JOptionPane.showInputDialog (this, "Nombre del tipo de cliente?", "Operacion Prestamo", JOptionPane.QUESTION_MESSAGE);
    		long valorCuota = Integer.parseInt(JOptionPane.showInputDialog (this, "Cuota?", "Operacion Prestamo", JOptionPane.QUESTION_MESSAGE));
    		long idcuenta = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero ID de la cuenta?", "Operacion Prestamo", JOptionPane.QUESTION_MESSAGE));
    		long idprestamo = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero ID del prestamo?", "Operacion Prestamo", JOptionPane.QUESTION_MESSAGE));
    		if (nombreTb != null && nombreTb.equals(nombre))
    		{
    			long Quitar=-valorCuota;
    			//Operacion en la cuenta 
    			parranderos.operacionPrestamoV2(nombreTb,idcuenta,Quitar,idprestamo);
    			String resultado = "Realizando su transaccion ";
    			//Operacion en el prestamo
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("No es un cajero");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void buscarTipoUsuario( )
    {
    	try 
    	{	
    	    adminBanc=false;
    	    gerenteOficina=false;
    	    cajero=false;
    	    cliente=false;
    		String loginTb = JOptionPane.showInputDialog (this, "Login", "Iniciar sesion", JOptionPane.QUESTION_MESSAGE);
    		String claveTb = JOptionPane.showInputDialog (this, "Clave", "Iniciar sesion", JOptionPane.QUESTION_MESSAGE);
    		verificarPagosAutomaticos(LocalDate.now());
    		if (loginTb != null && claveTb!= null)
    		{	
    			nombre=loginTb;
    			String tipo = parranderos.darUsuario(loginTb,claveTb);
    			if (tipo.toLowerCase().equals("cliente") )
    			{
    				cliente=true;
    			}
    			else if (tipo.toLowerCase().equals("cajero"))
    			{
    				cajero=true;
    			}
    			else if (tipo.toLowerCase().equals("adminbanc"))
    			{	
    				adminBanc=true;
    			}
    			else if (tipo.toLowerCase().equals("gerenteoficina"))
    			{
    				gerenteOficina=true;
    			}
    			else if (tipo.toLowerCase().equals("gerentegeneral"))
    			{
    				gerenteGeneral=true;
    			}

    			
    			String resultado = "En buscar Tipo Bebida por nombre\n\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    public void adicionarConsigna( )
    
    {
        try 
        {
        	if(cliente)
    		{
            String jefe = JOptionPane.showInputDialog (this, "Nombre del jefe?", "Adicionar consigna", JOptionPane.QUESTION_MESSAGE);
            long idJefe = Integer.parseInt(JOptionPane.showInputDialog (this, "Id de la cuenta del jefe?", "Adicionar consigna", JOptionPane.QUESTION_MESSAGE));
            String empleado = JOptionPane.showInputDialog (this, "Nombre del empleado?", "Adicionar consigna", JOptionPane.QUESTION_MESSAGE);
            long idEmpleado = Integer.parseInt(JOptionPane.showInputDialog (this, "Id de la cuenta del empleado?", "Adicionar consigna", JOptionPane.QUESTION_MESSAGE));
            long saldo =Integer.parseInt( JOptionPane.showInputDialog (this, "Monto?", "Adicionar consigna", JOptionPane.QUESTION_MESSAGE));
            String frecuencia=JOptionPane.showInputDialog (this, "Mensual(M) o Quincenal(Q)?", "Adicionar consigna", JOptionPane.QUESTION_MESSAGE);
            int dia=Integer.parseInt(JOptionPane.showInputDialog (this, "Que dia del mes desea que se haga la consignacion?", JOptionPane.QUESTION_MESSAGE));
            
            LocalDate hoy = LocalDate.now();
            int mes = hoy.getMonthValue();
            int anio = hoy.getYear();
            LocalDate fechalocal = LocalDate.of(anio, mes, dia);
            String fecha = fechalocal.toString();	
            
            if (fechalocal.isBefore(hoy)) {
            	
            	if (frecuencia.equals("M"))
                {
                
                fecha=fechalocal.plusDays(30).toString();
                

                }
                
                else
                {
                fecha=fechalocal.plusDays(15).toString();
                }
            	
            }
            
            if (fechalocal.isBefore(hoy)) {
            	
            	if (frecuencia.equals("M"))
                {
                
                fecha=fechalocal.plusDays(30).toString();

                }
                
                else
                {
                fecha=fechalocal.plusDays(15).toString();
                }
            	
            }
            
            if (jefe != null && empleado != null && frecuencia != null )
            {
            	if (nombre.equals(jefe)){
	                VOConsigna tb = parranderos.adicionarConsigna (jefe,idJefe,empleado,idEmpleado,saldo,fecha,frecuencia);
	
	                if (tb == null)
	                {
	                    throw new Exception ("No se pudo");
	                }
	                String resultado = "En adicionarTipoBebida\n\n";
	                resultado += "Tipo de bebida adicionado exitosamente: " + tb;
	                resultado += "\n Operación terminada";
	                panelDatos.actualizarInterfaz(resultado);
            	}
            }
            else
            {
                panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
            }
        } 
        }
        catch (Exception e) 
        {
//            e.printStackTrace();
            String resultado = generarMensajeError(e);
            panelDatos.actualizarInterfaz(resultado);
        }
    }
public void eliminarCrearConsigna( )
    
    {
        try 
        {
        	if(cliente)
    		{
            String jefe = JOptionPane.showInputDialog (this, "Nombre del jefe?", "Adicionar consigna", JOptionPane.QUESTION_MESSAGE);
            long idJefe = Integer.parseInt(JOptionPane.showInputDialog (this, "Id de la cuenta del jefe?", "Adicionar consigna", JOptionPane.QUESTION_MESSAGE));
            long nuevaId = Integer.parseInt(JOptionPane.showInputDialog (this, "Id de la nueva cuenta del jefe?", "Adicionar consigna", JOptionPane.QUESTION_MESSAGE));
            
            if (jefe != null )
            {
            	if (nombre.equals(jefe))
            	{
	                parranderos.eliminarCrearConsigna (jefe,nuevaId,idJefe);
	
            	}
            	}
            }
        }
        catch (Exception e) 
        {
//            e.printStackTrace();
            String resultado = generarMensajeError(e);
            panelDatos.actualizarInterfaz(resultado);
        }
    }


public void buscarPrestamo( )
{
	
    try 
    {

        if (cliente || gerenteOficina || gerenteGeneral)
        {
        	
        	JOptionPane.showMessageDialog(this,"Para el perfecto funcionamiento de este requerimiento el usuario llenara el recuadro en caso de utilizar el filtro o lo dejara vacio de lo contrario");
        	JOptionPane.showMessageDialog(this,"Los siguientes filtros se llenan de la siguiente forma Mayor,Menor,Igual =[14,23,56] Como se buscan los numeros mayores a 14  que a su vez sean menores a 23 y tambien aquellos que sean iguales a 56 ");
        	String ID = JOptionPane.showInputDialog (this, "Filtro ID", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
        	String Monto = JOptionPane.showInputDialog (this, "Filtro Monto?", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
        	String Interes = JOptionPane.showInputDialog (this, "Filtro Interes?", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
        	String NumeroCuotas = JOptionPane.showInputDialog (this, "Filtro Numero Cuotas?", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
        	String ValorCuota = JOptionPane.showInputDialog (this, "Filtro Valor Cuota?", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
        	JOptionPane.showMessageDialog(this,"Los siguientes filtros se llenan de la siguiente forma Igual,Esta =[Abierto,E] Como se puede ver se estan buscando por aquellas que sean iguales a abierto o que tengan E");
        	String Tipo = JOptionPane.showInputDialog (this, "Filtro Tipo", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
        	String Estado = JOptionPane.showInputDialog (this, "Filtro Estado", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
        	String Nombre = JOptionPane.showInputDialog (this, "Filtro Nombre", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
        	
        	List<String> LTipo = new ArrayList<String>(Arrays.asList(Tipo.split(",")));
        	List<String> LEstado = new ArrayList<String>(Arrays.asList(Estado.split(",")));
        	List<String> LNombre = new ArrayList<String>(Arrays.asList(Nombre.split(",")));
        	List<String> LID = new ArrayList<String>(Arrays.asList(ID.split(",")));
        	List<String> LMonto = new ArrayList<String>(Arrays.asList(Monto.split(",")));
        	List<String> LInteres = new ArrayList<String>(Arrays.asList(Interes.split(",")));
        	List<String> LNumero = new ArrayList<String>(Arrays.asList(NumeroCuotas.split(",")));
        	List<String> LValor = new ArrayList<String>(Arrays.asList(ValorCuota.split(",")));
        	
        	List <VOPrestamo> listaPrestamos = parranderos.darVOPrestamo(LTipo, LEstado, LNombre, LID, LMonto, LInteres, LNumero, LValor,nombre,cliente,gerenteOficina);
        	

        	
        
			String resultado = "En listarTipoBebida";
			resultado +=  "\n" + listarP (listaPrestamos);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n OperaciÃ³n terminada";
            }

    }
    catch (Exception e) 
    {
//        e.printStackTrace();
        String resultado = generarMensajeError(e);
        panelDatos.actualizarInterfaz(resultado);
    }
}




    
    public void verificarPagosAutomaticos(LocalDate fecha) {
    	
    	try {
    		
    		parranderos.verificarPagosAutomaticos(LocalDate.now());
    		
    	}
    	
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    public void operacionCuentaV2() {
    	
    	try 
    	{
    		if(cajero || cliente)
    		{
    		String nombreConsignador = JOptionPane.showInputDialog (this, "Nombre de la cuenta de origen de consignacion", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE);
    		long idOrigen = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero ID de la cuenta que consigna?", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE));
    		long saldo = Integer.parseInt(JOptionPane.showInputDialog (this, "Cantidad de dinero a transferir", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE));
    		String nombreTb = JOptionPane.showInputDialog (this, "Nombre de la cuenta destino", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE);
    		long idDestino = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero ID de la cuenta destino?", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE));	
    		if (nombreTb != null && nombreConsignador!=null && nombre.equals(nombreConsignador))
    		{
    			
    			
	    			parranderos.operacionCuentaV2(nombreConsignador,idOrigen,saldo,nombreTb,idDestino);
    			
    			
    			String resultado = "En operacion cuenta\n\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
    		}
    		else{
    			panelDatos.actualizarInterfaz("No es un cliente o cajero");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}	
    	
    }
    
    public void consultarOperaciones() {
    	
    	try 
    	{
    		if(cliente || gerenteOficina || gerenteGeneral)
    		{
    		long id = Integer.parseInt(JOptionPane.showInputDialog (this, "Indicar por cual id especifico buscar o escribir 0", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE));
    		String tipo = JOptionPane.showInputDialog (this, "Indicar el tipo de operacion o dejar vacio", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE);
    		String consignador = JOptionPane.showInputDialog (this, "Indicar el consignador o dejar vacio", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE);
    		long idconsignador = Integer.parseInt(JOptionPane.showInputDialog (this, "Indicar el id de la cuenta consignador o escribir 0", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE));
    		String destinatario = JOptionPane.showInputDialog (this, "Indicar el destinatario dejar vacio", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE);
    		long iddestinatario = Integer.parseInt(JOptionPane.showInputDialog (this, "Indicar el id de la cuenta destinatario o escribir 0", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE));
    		long montosuperior = Integer.parseInt(JOptionPane.showInputDialog (this, "Indicar el monto superior o escribir 0", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE));
    		long montoinferior = Integer.parseInt(JOptionPane.showInputDialog (this, "Indicar el monto inferior o escribir 0", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE));
    		String fecha = JOptionPane.showInputDialog (this, "Indicar por cual fecha buscar (AAAA-DD-MM) o dejar vacio", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE);
    		
    		boolean condicion0 = (id==0);
    		boolean condicion1 = (tipo.equals(""));
    		boolean condicion2 = (consignador.equals(""));
    		boolean condicion3 = (idconsignador==0);
    		boolean condicion4 = (destinatario.equals(""));
    		boolean condicion5 = (iddestinatario==0);
    		boolean condicion6 = (montosuperior==0);
    		boolean condicion7 = (montoinferior==0);
    		boolean condicion8 = (fecha.equals(""));
    		boolean permiso = false;
    		
    		List<Operaciones> operaciones = parranderos.darOperaciones();
    		System.out.print(operaciones);
    		String resultado="";
    		
    		for (int i=0; i<operaciones.size(); i++) {

    			permiso=false;
    			if (gerenteGeneral) {
    				permiso = true;
    			}
    			else if (gerenteOficina) {
    				
    				if (operaciones.get(i).getIdConsignador()==0) {
    					permiso=verificarGerenteOficina(nombre, operaciones.get(i).getIdDestinatario());
    				}
    				else if (operaciones.get(i).getIdDestinatario()==0) {
    					permiso=verificarGerenteOficina(nombre, operaciones.get(i).getIdConsignador());
    				}
    				else {
    					if ((verificarGerenteOficina(nombre, operaciones.get(i).getIdDestinatario())) || (verificarGerenteOficina(nombre, operaciones.get(i).getIdConsignador()))) {
    						permiso = true;
    					}
    				}
    				
    			}
    			else if (cliente) {
    				if (nombre.equals(operaciones.get(i).getConsignador()) || nombre.equals(operaciones.get(i).getDestinatario())) {
    					permiso = true;
    				}
    			}
    			if (permiso) {
	    			if((condicion0 == false) || (condicion1 == false) || (condicion2 == false) || (condicion3 == false) || (condicion4 == false) || (condicion5 == false) || (condicion6 == false) || (condicion7 == false) || (condicion8 == false)) {
	    				
	    				if (condicion0 == false) {
	    					if((operaciones.get(i).getId())==id) {
	    					}
	    					else {
	    						continue;
	    					}
	    				}
	    				
	    				if (condicion1 == false) {
	    					if(operaciones.get(i).getTipo().equals(tipo)) {
	    					}
	    					else {
	    						continue;
	    					}
	    				}
	    				
	    				if (condicion2 == false) {
	    					if(operaciones.get(i).getConsignador().equals(consignador)) {
	    					}
	    					else {
	    						continue;
	    					}
	    				}
	    				
	    				if (condicion3 == false) {
	    					if(operaciones.get(i).getIdConsignador()==idconsignador) {
	    					}
	    					else {
	    						continue;
	    					}
	    				}
	
	    				if (condicion4 == false) {
	    					if(operaciones.get(i).getDestinatario().equals(destinatario)) {
	    					}
	    					else {
	    						continue;
	    					}
	    				}
	    				
	    				if (condicion5 == false) {
	    					if(operaciones.get(i).getIdDestinatario()==iddestinatario) {
	    					}
	    					else {
	    						continue;
	    					}
	    				}
	    				
	    				if (condicion6 == false) {
	    					if((operaciones.get(i).getMonto())<=montosuperior) {
	    					}
	    					else {
	    						continue;
	    					}
	    				}
	
	    				if (condicion7 == false) {
	    					if((operaciones.get(i).getMonto())>=montoinferior) {
	    					}
	    					else {
	    						continue;
	    					}
	    				}
	    				
	    				if (condicion8 == false) {
	    					if(operaciones.get(i).getFecha().equals(fecha)) {
	    					}
	    					else {
	    						continue;
	    					}
	    				}
	    				
	    				resultado+=operaciones.get(i).getId()+", "+operaciones.get(i).getTipo()+", "+operaciones.get(i).getConsignador()+", "+operaciones.get(i).getIdConsignador()+", "+operaciones.get(i).getDestinatario()+", "+operaciones.get(i).getIdDestinatario()+", "+operaciones.get(i).getMonto()+", "+operaciones.get(i).getFecha();
	    				resultado+="\n";
	    			}
	    			
	    			else {
	    				resultado+=operaciones.get(i).getId()+", "+operaciones.get(i).getTipo()+", "+operaciones.get(i).getConsignador()+", "+operaciones.get(i).getIdConsignador()+", "+operaciones.get(i).getDestinatario()+", "+operaciones.get(i).getIdDestinatario()+", "+operaciones.get(i).getMonto()+", "+operaciones.get(i).getFecha();
	    				resultado+="\n";
	    			}
    			}
    		}
    		
    		panelDatos.actualizarInterfaz(resultado);
    		
    		}
    		else{
    			panelDatos.actualizarInterfaz("No es un cliente o gerente");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    public void consultarOperacionesv2( )
    {
    	
        try 
        {

            if (true)
            {
            	
            	JOptionPane.showMessageDialog(this,"Para el perfecto funcionamiento de este requerimiento el usuario llenara el recuadro en caso de utilizar el filtro o lo dejara vacio de lo contrario");
            	
            	String Inicial= JOptionPane.showInputDialog (this, "Fecha Inicial con formato yyyy-mm-dd hh24:mi:ss", "Buscar Operacion", JOptionPane.QUESTION_MESSAGE);
            	String Final = JOptionPane.showInputDialog (this, "Fecha Final con formato yyyy-mm-dd hh24:mi:ss", "Buscar Operacion", JOptionPane.QUESTION_MESSAGE);
            	JOptionPane.showMessageDialog(this,"Los siguientes filtros se llenan de la siguiente forma Mayor,Menor,Igual =[14,23,56] Como se buscan los numeros mayores a 14  que a su vez sean menores a 23 y tambien aquellos que sean iguales a 56 ");
            	String ID = JOptionPane.showInputDialog (this, "Filtro ID", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
            	String Monto = JOptionPane.showInputDialog (this, "Filtro Monto?", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
            	String Interes = JOptionPane.showInputDialog (this, "Filtro Id Consignador?", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
            	String NumeroCuotas = JOptionPane.showInputDialog (this, "Filtro Id Destinatario?", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
            	JOptionPane.showMessageDialog(this,"Los siguientes filtros se llenan de la siguiente forma Igual,Esta =[Abierto,E] Como se puede ver se estan buscando por aquellas que sean iguales a abierto o que tengan E");
            	String Tipo = JOptionPane.showInputDialog (this, "Filtro Tipo Operacion", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
            	String Estado = JOptionPane.showInputDialog (this, "Filtro Nombre Consignador", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
            	String Nombre = JOptionPane.showInputDialog (this, "Filtro Nombre Destinatario", "Buscar Prestamo", JOptionPane.QUESTION_MESSAGE);
            	
            	
            	List<String> LEstado = new ArrayList<String>(Arrays.asList(Estado.split(",")));
            	List<String> LNombre = new ArrayList<String>(Arrays.asList(Nombre.split(",")));
            	List<String> LID = new ArrayList<String>(Arrays.asList(ID.split(",")));
            	List<String> LMonto = new ArrayList<String>(Arrays.asList(Monto.split(",")));
            	List<String> LInteres = new ArrayList<String>(Arrays.asList(Interes.split(",")));
            	List<String> LNumero = new ArrayList<String>(Arrays.asList(NumeroCuotas.split(",")));

            	Timestamp FechaI=Timestamp.valueOf(Inicial);
            	Timestamp FechaF=Timestamp.valueOf(Final);
            	List <VOOperaciones> listaPrestamos = parranderos.darVOOperacion(FechaI,FechaF,Tipo, LEstado, LNombre, LID, LMonto, LInteres, LNumero);
            	

            	
            
    			String resultado = "En listarTipoBebida";
    			resultado +=  "\n" + listarO (listaPrestamos);
    			panelDatos.actualizarInterfaz(resultado);
    			resultado += "\n OperaciÃ³n terminada";
                }

        }
        catch (Exception e) 
        {
//            e.printStackTrace();
            String resultado = generarMensajeError(e);
            panelDatos.actualizarInterfaz(resultado);
        }
    }
    
    public void consultarConsignas() {
    	
    	try 
    	{
    		if(gerenteGeneral)
    		{
    		long montoinferior = Integer.parseInt(JOptionPane.showInputDialog (this, "Indicar el monto menor de consignas a consultar", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE));
    		String tipo = JOptionPane.showInputDialog (this, "Indicar el tipo de operacion", "Consultar Operaciones", JOptionPane.QUESTION_MESSAGE);
    		
    		boolean condicion0 = (montoinferior==0);
    		boolean condicion1 = (tipo.equals(""));
    		
    		
    		List<Operaciones> operaciones = parranderos.darOperaciones();
    		String resultado="";
    		
    		if((!condicion0) || (!condicion1)) {
    		
    			for (int i=0; i<operaciones.size(); i++) {
	    				
	    			if (condicion0 == false) {
    					if((operaciones.get(i).getMonto())>=montoinferior) {
    					}
    					else {
    						continue;
    					}
    				}
	    				
	    			if (condicion1 == false) {
	    				if(operaciones.get(i).getTipo().equals(tipo)) {
	    				}
	    				else {
	    					continue;
	    				}
	    			}
	    				
	    			resultado+=operaciones.get(i).getId()+", "+operaciones.get(i).getTipo()+", "+operaciones.get(i).getConsignador()+", "+operaciones.get(i).getIdConsignador()+", "+operaciones.get(i).getDestinatario()+", "+operaciones.get(i).getIdDestinatario()+", "+operaciones.get(i).getMonto()+", "+operaciones.get(i).getFecha();
	    			resultado+="\n";
	    			}
    			
    			panelDatos.actualizarInterfaz(resultado);
    			
    		}
    		
    		else {
    			panelDatos.actualizarInterfaz("No selecciono un filtro de busqueda");
    		}
    		
    		}
    		else{
    			panelDatos.actualizarInterfaz("No es un cliente o gerente");
    		}
		}
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    public void consultarPuntosDeAtencion() {
    	
    	try {
    		
    		if(gerenteGeneral) {
    			
    			
    			
    		}
    		
    	}
    	
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
	public List obtenerPuntoDeAtencion() {
    	
    	List <PuntoDeAtencion> puntosdeatencion = parranderos.darPuntosDeAtencion();
    	
    	List retorno = new ArrayList();
    	
    	for (int i=0; i<puntosdeatencion.size(); i++) {
    		
    		if (puntosdeatencion.get(i).getCajero().equals(nombre)) {
    			
    			retorno.add(puntosdeatencion.get(i).getId());
    			
    		}
    		
    	}
    	
    	return retorno;
    	
    }
    
    
    public boolean verificarGerenteOficina(String gerente, long id) {
    	
    	List<Cuenta> cuentas = parranderos.darCuentas();
    	
    	for (int i=0; i<cuentas.size(); i++) {
    		
    		if (cuentas.get(i).getId() == id ) {
    			
    			if (cuentas.get(i).getGerente().equals(gerente)) {
    				return true;
    			}
    			else {
    				return false;
    			}
    			
    		}
    		
    	}
    	
    	return false;
    	
    }
    
	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Parranderos
	 */
	public void mostrarLogParranderos ()
	{
		mostrarArchivo ("parranderos.log");
	}
	
	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
	
	/**
	 * Limpia el contenido del log de parranderos
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogParranderos ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("parranderos.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}
	
	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}
	
	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}
	
	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	
	/**
     * Muestra la información acerca del desarrollo de esta apicación
     */
    public void acercaDe ()
    {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germán Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jiménez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
    }
    
    

	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una línea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una líea para cada tipo de bebida recibido
     */


    /**
     * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
     * @param e - La excepción recibida
     * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
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

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
    /**
     * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
     * Invoca al método correspondiente según el evento recibido
     * @param pEvento - El evento del usuario
     */
    @Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazParranderosApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
    
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
    /**
     * Este método ejecuta la aplicación, creando una nueva interfaz
     * @param args Arreglo de argumentos que se recibe por línea de comandos
     */
    private String listarP(List<VOPrestamo> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOPrestamo tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    private String listarO(List<VOOperaciones> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOOperaciones tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    public static void main( String[] args )
    {
        try
        {
            // Unifica la interfaz para Mac y para Windows.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazParranderosApp interfaz = new InterfazParranderosApp( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
