package util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class ClienteMulticast {
    public static void main(String[] args) {
        try {
            InetAddress direccionServer = InetAddress.getByName("225.0.0.1");
            int puertoServer = 33333;
            DatagramSocket socketCliente = new DatagramSocket();
            byte[] buffer;
            byte[] bufferNombre;
            DatagramPacket pqtSalida;
            DatagramPacket pqtNombre;

            Scanner tcd = new Scanner(System.in);
            System.out.println("Dime tu nombre: ");
            String nombre = "nombre" + tcd.nextLine();
            bufferNombre = nombre.getBytes();
            pqtNombre = new DatagramPacket(bufferNombre, bufferNombre.length, direccionServer, puertoServer);
            socketCliente.send(pqtNombre);
            System.out.println("-INICIO DEL CHAT-");
            System.out.println("-----------------");

            boolean fin = false;
            while(!fin){
                String mensaje = "mensaje" + tcd.nextLine();
                if(mensaje.equalsIgnoreCase("mensajefin")){
                    fin = true;
                }

                buffer = mensaje.getBytes();
                pqtSalida = new DatagramPacket(buffer, buffer.length, direccionServer, puertoServer);
                socketCliente.send(pqtSalida);

                byte[] bufferEntrada = new byte[1024];
                DatagramPacket pqtEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
                socketCliente.receive(pqtEntrada);
                String respuesta = new String(pqtEntrada.getData()).trim();
                System.out.println(respuesta);

            }
            System.exit(1);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
