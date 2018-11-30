package Paquete_productor_consumidor;

/*
 * La clase comunicacion configura el puerto serial para la configuracion del
 * sensor. La clase DataCaptura se crea para mandar el codigo de inicio de adquisicion
 * de datos continuos en el sensor y para su proceso. En esta clase, vuelve a configurar
 * el puerto serial de la misma manera que en la clase comunicacion. Esto permite hacer 
 * la captura de Data independiente de la clase Comunicacion.
 * ++++++++++++ CLASE PRODUCTORA DE DATOS ++++++++++++++++++++++++++++++++

NOTA: En esta clase, la variable "PERMISO" permite capturar el puerto en el codigo
se escucha de datos para evitar que se puedan perder datos. Esta accion se activa
al usuario ordenar ejecutar el PRODUCTOR-CONSUMIDOR. 
La variable "ACTIVAR" permite salir del ciclo de captura de la linea de codigo,
de esta forma el proceso de cerrar el proceso de PRODUCTOR-CONSUMIDOR se hace
mas efectivo. 
El cuerpo del codigo de captura esta separado en cabezal y cuerpo del codigo,
se debe ordenar si la captura comienza por la busqueda del cabezal o si solo
se quiere capturar el cuerpo completo del codigo. De la ultima forma no se asegura
que la linea de codigo se capture de forma correcta.

    LA estructura de la secuencia del programa es la siguiente:
    - La variable "PERMITIR" cambia de valor cuando el boton de conexion es activado.
    - La estructura de captura de cabezal se activa una vez el puerto es motivado por
      un evento. El tipo de cabezal dependera de la conexion establecida.
    - La siguiente estructura es la captura del cuerpo del codigo enviado.
      este tiene la longitud especificada.
    - Una vez que el codigo es totalmente capturado,el ultimo cuerpo del programa
      se encarga de enviar al buffer el ultimo valor y de reiniciar las bandedaras
 */

import Paquete_almacena_informacion.Clase_para_guardar_datos_numericos;
import Paquete_de_comunicacion_serial.Clase_para_coordinar_configuracion_puerto_serial;
//import Paquete_informacion_para_ambientes.Clase_informacion_productor_consumidor;
import java.util.TooManyListenersException;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import java.io.*;
import javax.swing.JButton;
import java.awt.Color;

public class DataCaptura_puerto implements SerialPortEventListener, Runnable
{
    private OutputStream os2;//stream para la escritura de byte.
    private InputStream io2;//stream para la lectura de byte.
    public SerialPort sPort2 = null; //para inicializar en vacio.
    public int captura=0;//variable para numero entero en el paso Byte a int.
    public int contador3=0;
    public int cantidad;//variable para especificar la cantidad de datos, dependiendo de la resolucion.
    
    private volatile boolean paso_while = true;
    public int paso;
    public int permiso;//variable para detectar que el boton de conexion fue activado.
    public int activar=1;
    public int cabezal=0;//bandera que indica que existe cabezal en linea de codigo.
    public int longitud_codigo;//varible para establecer longitud del cuerpo de linea de codigo
    public int confirmacion=0;
    public int[] cabezal_principal;
    public int[] valores_guardados;
    private int[] linea_codigo;
    private int longitudInternoCodigo;
    
    private final Buffer_puerto datosCompartidos; // objeto de la clase buffer creada para compartir valores.
    Clase_para_coordinar_configuracion_puerto_serial configuracion_puerto;
    Clase_para_guardar_datos_numericos almacena_numeros;
    
    public DataCaptura_puerto(Clase_para_coordinar_configuracion_puerto_serial configuracion_puerto,
            Buffer_puerto shared,Clase_para_guardar_datos_numericos almacena_numeros,
            int longitudInternoCodigo)
    {
        this.configuracion_puerto= configuracion_puerto;
        datosCompartidos=shared;
        this.almacena_numeros= almacena_numeros;
        this.longitudInternoCodigo=longitudInternoCodigo;
///////////////////////////////////////////////////////////////////////////
/* No alterar el orden le los metodos colocados en el constructor.*/
        
        setupPuerto3(); //suministra la propiedad de actionLisneter del puerto.
  
    }
    
    public void run()
    {
        try{
            while(paso_while && !Thread.currentThread().isInterrupted())
            {
            
                try{
                    
                    
                }catch(Exception e)
                {
                }
            }
        
        }finally{
            Thread.currentThread().interrupt();
        }
   
    }
    

///////////////////////////////////////////////////////////////////////////////    
/* Este metodo permite activar la funcion de EventListener encargada de escuchar
 cuando los datos seriales estan disponibles, esta configuracion debe estar
 ligada al metodo SerialPortEvent (Importante) */ 
    public void setupPuerto3()
    {
        sPort2= configuracion_puerto.metodo_para_retornar_sPort();
        io2= configuracion_puerto.metodo_para_retornar_io();
 /////////////////////////////////////////////////////////////////////////////        
// esto crea un evento listener para cuando sPort tiene los datos disponibles.
        try{
            sPort2.addEventListener(this);
        }catch(TooManyListenersException e)
        {
            System.out.println("Error en Lectura serial: " +sPort2.getName());
        }
///////////////////////////////////////////////////////////////////////        
        sPort2.notifyOnDataAvailable(true); //esto abilita para cuando el dato esta disponible.    
    }

////////////////////////////////////////////////////////////////////////////
    public void impresionNumeros()
    {
        String hex = Integer.toHexString(captura); //paso de int a HEX.
        System.out.println("entro algo "+captura+ " esto es en hex" + hex+
                            " contador "+contador3);
    }

    
/////////////////////////////////////////////////////////////////////////////
  //************************************************************************  
  ///////////////////////////////////////////////////////////////////////  
    public void serialEvent(SerialPortEvent e)
    {
        switch (e.getEventType())
        {
            case SerialPortEvent.DATA_AVAILABLE:
                
                System.out.println("/////////////////////////////////");
                
                int posicion=8;//lugar en el arreglo para la bandera permiso
                valores_guardados=almacena_numeros.metodo_para_regresar_arreglo_numeros();//Comprueba se el boton de conectar fue activado
                permiso= valores_guardados[posicion];
                activar=1;
                confirmacion=0;
                
                while(permiso==1)//activa accion por orden del usuario, secuestra el puerto para no perder el dato
                {  
                    
                    if(activar!=0)// este metodo establece si se ejecuta la orden de captura de codigo.
                    {
                     
                        try{
                            captura= io2.read();
                            datosCompartidos.set(captura);
                        }catch (Exception ev){
                            System.err.println("Algo paso");
                        }
                
///////////////////////////////////////////////////////////////////////////////                 
 //la accion de detener el proceso productor consumidor que se ejecuto, al
 //presinar el boton de fin de recepcion(esta para ciclos de captura indefinido de datos .
                    valores_guardados=almacena_numeros.metodo_para_regresar_arreglo_numeros();//Comprueba se el boton de conectar fue activado
                    permiso= valores_guardados[posicion];
                    if(permiso==0)//para ejecutar la captura una vez
                    {
                        activar=0;
                    }
                }//fin del if activar
        }//fin del while de permiso
                
            sPort2.removeEventListener();
            break;
        }//fin del switch. 
    }//fin de serialEvent
}//fin de la clase
