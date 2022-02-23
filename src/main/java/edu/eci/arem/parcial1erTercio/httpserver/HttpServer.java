package edu.eci.arem.parcial1erTercio.httpserver;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpServer {
    void startServer() throws IOException;

    void getStaticFiles(String endpoint);

    void getResource(String fullPath);


    void printErrorMessage(int statusCode, String message, String statusName);

    OutputStream getOut();

    void setOut(OutputStream out);
}
