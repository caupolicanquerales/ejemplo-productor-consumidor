package Paquete_productor_consumidor;

/*
 * Clase creada para analizar los datos suministrados y capturados en la clase
 * DataCaptura. 
NOTA:
    esta clase es encargada de recibir BYTE a BYTE la informacion suministrada
    por la clase PRODUCTOR. Cada "byte" recibido por la clase, 
    se inicializa un TIMER y se desactivara cada vez que entre un nuevo valor
    en un intervalo comprendido por 200 miliseconds o el tiempo que se tenga
    estipulado.
    Una vez recibido el ultimo valor, el TIMER sera activado y como el tiempo
    estimado activara la ejecucion del TIMER, este cerrara su ejecucion e indicara
    que la linea de codigo finalizo su recepcion.

 * ++++++++++++++ CLASE CONSUMIDORA DE DATOS +++++++++++++++++++++++++++
 */

import Paquete_almacena_informacion.Clase_para_guardar_ArrayList;
import Paquete_coordinar_acciones.Clase_para_coordinar_acciones;
import Paquete_de_comunicacion_serial.Clase_para_coordinar_configuracion_puerto_serial;
import Paquete_para_procesar_ArrayList.Clase_para_procesar_arrayList;
import java.io.*;
import java.util.Timer;
import javax.comm.SerialPort;
import Paquete_para_TIMER.Clase_para_TIMER;
import java.util.ArrayList;

public class AnalisisDatos_puerto implements   Runnable
{
    private OutputStream os2;//stream para la escritura de byte.
    public int[] datos2; //arreglo para los datos enviados por la clase DataCaptura.
    public int totalPuntos;// cantidad de bytes en la linea del mensaje.
    public SerialPort sPort2; //para inicializar en vacio.
    public Byte[] codigo;
    public String[] cadena;
    public byte[] valor;
    public ArrayList<Integer> lineaEnteros=new ArrayList<Integer>();
    
    public int paso; 
    private int contador=0; // variable encargada de contar numeros de datos enviados por el "Pipe"
  
    private Timer tiempo;
    
    private final Buffer_puerto datosCompartidos; // objeto de la clase buffer creada para compartir valores.
    Clase_para_coordinar_configuracion_puerto_serial configuracion_puerto;
    Clase_para_procesar_arrayList procesar_arrayList;
    Clase_para_TIMER disparadorTIMER;
    
    Clase_para_guardar_ArrayList guardar_ArrayList;
    
    Clase_para_coordinar_acciones coordinar_acciones;
////////////////////////////////////////////////////////////////////////////////
 /* Con la tecnica BlockingQueue el metodo llamadoDataCaptura no seria funcional*/
    
    public AnalisisDatos_puerto(Buffer_puerto shared,
            Clase_para_coordinar_configuracion_puerto_serial configuracion_puerto,
            Clase_para_guardar_ArrayList guardar_ArrayList,
            Clase_para_coordinar_acciones coordinar_acciones)
    {
        datosCompartidos= shared; //variable para cargar los datos.
        this.configuracion_puerto= configuracion_puerto;
        this.guardar_ArrayList=guardar_ArrayList;
        this.coordinar_acciones=coordinar_acciones;
        
        procesar_arrayList=new Clase_para_procesar_arrayList();
        
        os2= configuracion_puerto.metodo_para_retornar_os();
    }
    
 //////////////////////////////////////////////////////////////////////////////
    public void run()
    {
        try{
            while(!Thread.interrupted())
            {
                try
                {
                   
                    datos2= datosCompartidos.get(); //forma de cargar los datos en el arreglo datos2.
                    
                    if(contador==0)
                    {
                        procesar_arrayList.metodo_para_adicionar_ultimo_elemento_INTEGER(lineaEnteros,datos2[0]);
                        lineaEnteros= procesar_arrayList.metodo_para_regresar_arrayList_Integer();
                        
                        guardar_ArrayList.metodo_para_pasar_ArrayList_INTEGER(lineaEnteros);
                        
                        tiempo=new Timer();
                        disparadorTIMER=new Clase_para_TIMER(tiempo,lineaEnteros,guardar_ArrayList,
                                                            coordinar_acciones);
                        tiempo.schedule(disparadorTIMER, 0, 200);
                        contador++;
                    }else {
                        tiempo.cancel();
                        tiempo.purge();
                        
                        lineaEnteros= guardar_ArrayList.metodo_para_regresar_ArrayList_INTEGER();
                        
                        procesar_arrayList.metodo_para_adicionar_ultimo_elemento_INTEGER(lineaEnteros,datos2[0]);
                        lineaEnteros= procesar_arrayList.metodo_para_regresar_arrayList_Integer();
                        
                        tiempo=new Timer();
                        disparadorTIMER=new Clase_para_TIMER(tiempo,lineaEnteros,guardar_ArrayList,
                                                            coordinar_acciones);
                        tiempo.schedule(disparadorTIMER, 0, 200);
                    }
                    
                }catch(Exception e)
                {
                }
            }
        
        }finally{
            Thread.currentThread().interrupt();
        }
        
    }


}//fin de la clase AnalisisDatos.
