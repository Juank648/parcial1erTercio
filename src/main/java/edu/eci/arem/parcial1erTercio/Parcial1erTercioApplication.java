package edu.eci.arem.parcial1erTercio;


import edu.eci.arem.parcial1erTercio.httpserver.HttpServer;
import edu.eci.arem.parcial1erTercio.httpserver.HttpServerImpl;

import java.io.IOException;

public class Parcial1erTercioApplication {

	public static void main(String[] args) {
		HttpServer httpServer = new HttpServerImpl();
		try {
			httpServer.startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
