package es.deusto.sd.verificacionfacial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

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
		try(
			socket;
			BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter w = new PrintWriter(socket.getOutputStream(), true);) {
			//Leer los dos vectores
			String r1 = bf.readLine();
			String r2 = bf.readLine();

			try{
				//Convertir lo leido en vectores
				double[] result1 = convertirVector(r1);
				double[] result2 = convertirVector(r2);

				//Llamar a VerificadorBiometrico - Verificar los 2 vectores
				VerificadorBiometrico vb = new VerificadorBiometrico();
				boolean resultado = vb.verificar(result1, result2);

				//Escribir el resultado
				w.println(resultado);
			} catch (IllegalArgumentException iae) {
				//Formato de vector incorrecto
				System.out.println(iae + " - Argumento ilegal o inapropiado o nulo o fuera del rango"); 
				//Escribir false
				w.println(false);
			}

			
		
			//Manejar en la operacion de input/output
		} catch (IOException io) {
			System.out.println(io + " - Error durante una operacion de entrada o salida");
		}
		
	}

	private static double[] convertirVector(String texto) {

		// Comprobar si el texto es nulo o esta vacio
		if (texto == null || texto.isBlank()) {
			throw new IllegalArgumentException("Formato inadecuado");
		} else {
			// Separar por coma
			String[] partes = texto.split(",", -1);
			double[] result = new double[partes.length];
			// Parsear a double
			for (int i = 0; i < partes.length; i++) {
				result[i] = Double.parseDouble(partes[i].trim());
			}
			return result;
		}

	}
}