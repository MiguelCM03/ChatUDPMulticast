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
            int puerto = 33333;
            InetAddress grupo= InetAddress.getByName("225.0.0.1");
            MulticastSocket socketServidor = new MulticastSocket(puerto);
            byte[] bufferEntrada = new byte[1024];
            DatagramPacket entrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
            DatagramPacket pqtNombre = new DatagramPacket(bufferEntrada, bufferEntrada.length);
            Map<String, Integer> mapa = new HashMap<>();

            socketServidor.joinGroup(grupo);

            InetAddress direccionCliente = InetAddress.getByName("localhost");
            byte[] bufferSalida;
            DatagramPacket salida;


            while(true){
                socketServidor.receive(pqtNombre);
                String nombreRecibido = new String(entrada.getData()).trim();
                Integer puertoCliente = pqtNombre.getPort();
                if(!mapa.containsKey(nombreRecibido)){
                    mapa.put(nombreRecibido, puertoCliente);
                }


                System.out.println("Dirección: " + pqtNombre.getAddress());
                System.out.println("Puerto: " + pqtNombre.getPort());


                socketServidor.receive(entrada);
                String recibido = new String(entrada.getData()).trim();
                System.out.println(recibido);

                for (Map.Entry<String, Integer> entry : mapa.entrySet()){
                    String respuesta = entry.getKey() + ": " + recibido;
                    bufferSalida = respuesta.getBytes();
                    salida = new DatagramPacket(bufferSalida, bufferSalida.length, direccionCliente, entry.getValue());
                    socketServidor.send(salida);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
