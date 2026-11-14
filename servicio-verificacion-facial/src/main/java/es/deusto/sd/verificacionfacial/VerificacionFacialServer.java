package es.deusto.sd.verificacionfacial;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VerificacionFacialServer {

	private static final int PUERTO = 9090;

	public static void main(String[] args) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
			System.out.println("Servicio de verificacion facial escuchando en el puerto " + PUERTO);
			while (true) {
				Socket socket = serverSocket.accept();
				new Thread(() -> atender(socket)).start();
			}
		}
	}

	private static void atender(Socket socket) {
		// De momento solo se acepta y se cierra la conexion (protocolo por definir).
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Error al cerrar la conexion: " + e.getMessage());
		}
	}
}
