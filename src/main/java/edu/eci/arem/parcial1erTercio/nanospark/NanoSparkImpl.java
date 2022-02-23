package edu.eci.arem.parcial1erTercio.nanospark;



import edu.eci.arem.parcial1erTercio.httpserver.HttpServer;
import edu.eci.arem.parcial1erTercio.nanospark.components.Request;
import edu.eci.arem.parcial1erTercio.nanospark.components.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.function.BiFunction;

/**
 * Class That Implements The Minimum Spark Methods For The App.
 */
public class NanoSparkImpl implements NanoSpark {

    private final HttpServer httpServer;
    private OutputStream out;
    private final HashMap<String, BiFunction<Request, Response, String>> endpoints;

    public NanoSparkImpl(HttpServer httpServer) {
        this.httpServer = httpServer;
        this.out = httpServer.getOut();
        this.endpoints = new HashMap<>();
    }

    @Override
    public void execute(String path, BiFunction<Request, Response, String> function) {
        Request request = new Request(path);
        Response response = new Response(httpServer, path);
        try {
            String result = function.apply(request, response);
            if (result != null) {
                out.write(("HTTP/1.1 200 OK\r\n"
                        + "\r\n"
                        + result).getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void get(String endpoint, BiFunction<Request, Response, String> function) {
        endpoints.put(endpoint, function);
    }

    @Override
    public void setOut(OutputStream out) {
        this.out = out;
    }

    @Override
    public OutputStream getOut() {
        return out;
    }

    @Override
    public void check(String path) {
        int indexOfValue = path.indexOf("?");
        endpoints.forEach((k, v) -> {
            if (indexOfValue < 0 && path.equals(k)) {
                execute(path, v);
            } else if (indexOfValue >= 0) {
                String pathWithOutValues = path.substring(0, indexOfValue);
                if (pathWithOutValues.equals(k)) {
                    execute(path, v);
                }
            }
        });
    }
}
