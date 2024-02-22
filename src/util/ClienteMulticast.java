package util;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClienteMulticast {
    public static void main(String[] args) {
        try {
            InetAddress direccionServer = InetAddress.getByName("225.0.0.1");
            int puertoServer = 33333;
            DatagramSocket socketCliente = new DatagramSocket();
            byte[] buffer;
            DatagramPacket pqt;
            Scanner tcd = new Scanner(System.in);
            System.out.println("Dime tu nombre: ");
            String nombre = tcd.nextLine();
            buffer = nombre.getBytes();
            pqt = new DatagramPacket(buffer, buffer.length, direccionServer, puertoServer);
            socketCliente.send(pqt);
            while(true){
                String mensaje = tcd.nextLine();
                buffer = mensaje.getBytes();
                pqt  = new DatagramPacket(buffer, buffer.length, direccionServer, puertoServer);
                socketCliente.send(pqt);

                byte[] bufferEntrada = new byte[1024];
                DatagramPacket entrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
                socketCliente.receive(entrada);
                String respuesta = new String(entrada.getData()).trim();
                System.out.println(respuesta);

            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
