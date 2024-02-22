package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServidorMulticast {
    public static void main(String[] args) {
        try {
            String conversacion = "";
            int puerto = 33333;
            InetAddress grupo= InetAddress.getByName("225.0.0.1");
            MulticastSocket socketServidor = new MulticastSocket(puerto);
            byte[] bufferEntrada = new byte[1024];
            DatagramPacket pqtEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
            Map<String, Integer> mapa = new HashMap<>();

            socketServidor.joinGroup(grupo);

            InetAddress direccionCliente = InetAddress.getByName("localhost");
            byte[] bufferSalida;
            DatagramPacket salida;


            while(true){
                socketServidor.receive(pqtEntrada);
                String recibido = new String(pqtEntrada.getData(),0,pqtEntrada.getLength()).trim();
                if(recibido.startsWith("nombre")){
                    Integer puertoCliente = pqtEntrada.getPort();
                    String nombreRecibido = "";
                    for(int i = 6; i < recibido.length(); i++){
                        nombreRecibido += recibido.charAt(i);
                    }
                    if(!mapa.containsKey(nombreRecibido)){
                        mapa.put(nombreRecibido, puertoCliente);
                    }
                    bufferSalida = conversacion.getBytes();
                    int puertoUsuarioActual = pqtEntrada.getPort();
                    salida = new DatagramPacket(bufferSalida, bufferSalida.length, direccionCliente, puertoUsuarioActual);
                    socketServidor.send(salida);


                    System.out.println("Dirección: " + pqtEntrada.getAddress());
                    System.out.println("Puerto: " + pqtEntrada.getPort());
                }else if(recibido.startsWith("mensaje")){
                    String mensajeRecibido = "";
                    for(int i = 7; i < recibido.length(); i++){
                        mensajeRecibido += recibido.charAt(i);
                    }
                    conversacion += "\n" + mensajeRecibido;
                    for (Map.Entry<String, Integer> entry : mapa.entrySet()){
                        String respuesta = entry.getKey() + ": " + mensajeRecibido;
                        bufferSalida = respuesta.getBytes();
                        salida = new DatagramPacket(bufferSalida, bufferSalida.length, direccionCliente, entry.getValue());
                        socketServidor.send(salida);
                    }
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
