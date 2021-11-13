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
import java.time.LocalDate;
import java.util.ArrayList;
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

import uniandes.isis2304.parranderos.negocio.Parranderos;
import uniandes.isis2304.parranderos.negocio.VOCliente;
import uniandes.isis2304.parranderos.negocio.VOConsigna;
import uniandes.isis2304.parranderos.negocio.VOPrestamo;

import uniandes.isis2304.parranderos.negocio.VOCuenta;
import uniandes.isis2304.parranderos.negocio.VOGerenteOficina;
import uniandes.isis2304.parranderos.negocio.VOTipoBebida;
import uniandes.isis2304.parranderos.negocio.VOUsuario;
import uniandes.isis2304.parranderos.negocio.VOOficina;
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
    		String nombreTipo = JOptionPane.showInputDialog (this, "Nombre del tipo de usuario?", "Adicionar usuario", JOptionPane.QUESTION_MESSAGE);
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
    		if (nombreTipo != null & login!= null & clave!= null  & tipoDocumento!= null & nacionalidad!= null & direccionFisica!= null & direccionElectronica!= null & ciudad!= null & departamento!= null  )
    		{
    			System.out.println("0");
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
    		if (nombreTipo != null & estado!= null & tipo!= null & diaPaga!=null )
    		{
    			System.out.println("0");
        		VOPrestamo tb = parranderos.adicionarPrestamo (tipo,estado,nombreTipo,monto,interes,numeroCuotas,diaPaga,valorCuota);
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
    		if (nombreTb != null )
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
    		
    		if (nombre != null & direccion!= null & gerenteUsuario!= null)
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
    		String oficina = JOptionPane.showInputDialog (this, "Oficina en caso de tener?", "Adicionar oficina", JOptionPane.QUESTION_MESSAGE);
    		
    		if (tipo != null & localizacion!= null)
    		{
        		VOPuntoDeAtencion tb = parranderos.adicionarPuntoDeAtencion(tipo,localizacion,oficina);
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
    
    public void adicionarCuenta( )
    {
    	try 
    	{
    		if (gerenteOficina) {
    		String tipo = JOptionPane.showInputDialog (this, "Tipo de la cuenta?", "Adicionar tipo", JOptionPane.QUESTION_MESSAGE);
    		long saldo =Integer.parseInt( JOptionPane.showInputDialog (this, "Saldo inicial de la cuenta?", "Adicionar saldo", JOptionPane.QUESTION_MESSAGE));
    		String cliente = JOptionPane.showInputDialog (this, "Cliente de la cuenta?", "Adicionar cliente", JOptionPane.QUESTION_MESSAGE);
    		String gerente = JOptionPane.showInputDialog (this, "Gerente de la cuenta?", "Adicionar gerente", JOptionPane.QUESTION_MESSAGE);
    		
    		if (tipo != null & cliente!= null & gerente!=null)
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
<<<<<<< Updated upstream
<<<<<<< Updated upstream
    		verificarPagosAutomaticos(LocalDate.now().toString());
=======
    		verificarPagosAutomaticos(LocalDate.now());
>>>>>>> Stashed changes
=======
    		verificarPagosAutomaticos(LocalDate.now());
>>>>>>> Stashed changes
    		if (loginTb != null & claveTb!= null)
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
            String fecha;
            if (frecuencia.equals("M"))
            {
<<<<<<< Updated upstream
<<<<<<< Updated upstream
            fecha=LocalDate.now().plusDays(30).toString();
=======
            	
            fecha=LocalDate.now().plusDays(30).toString();
            
>>>>>>> Stashed changes
=======
            	
            fecha=LocalDate.now().plusDays(30).toString();
            
>>>>>>> Stashed changes
            System.out.print(fecha);
            }
            else
            {
            fecha=LocalDate.now().plusDays(15).toString();
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
    
<<<<<<< Updated upstream
<<<<<<< Updated upstream
    public void verificarPagosAutomaticos(String fecha) {
    	
    	
=======
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    	
    }
    
    public void operacionCuentaV2() {
    	
    	try 
    	{
    		if(cajero || cliente)
    		{
    		String nombreConsignador = JOptionPane.showInputDialog (this, "Nombre de la cuenta de origen de consignacion", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE);
    		String nombreTb = JOptionPane.showInputDialog (this, "Nombre de la cuenta a consignar", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE);
    		long saldo = Integer.parseInt(JOptionPane.showInputDialog (this, "Cantidad de dinero a transferir", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE));
    		long idOrigen = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero ID de la cuenta que consigna?", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE));
    		long idDestino = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero ID de la cuenta a consignar?", "Operacion Cuenta", JOptionPane.QUESTION_MESSAGE));	
    		if (nombreTb != null & nombreConsignador!=null & nombre.equals(nombreConsignador))
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
    private String listarTiposBebida(List<VOTipoBebida> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOTipoBebida tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}

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
