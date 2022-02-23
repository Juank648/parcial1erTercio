package edu.eci.arem.parcial1erTercio.httpserver;

import edu.eci.arem.parcial1erTercio.nanospark.NanoSpark;
import edu.eci.arem.parcial1erTercio.nanospark.NanoSparkImpl;
import edu.eci.arem.parcial1erTercio.nanospark.components.NanoSparkException;
import edu.eci.arem.parcial1erTercio.nanospark.components.Request;
import edu.eci.arem.parcial1erTercio.nanospark.components.Response;
import edu.eci.arem.parcial1erTercio.weather.WeatherImpl;
import edu.eci.arem.parcial1erTercio.weather.WeatherService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServerImpl implements HttpServer{
    private boolean running;
    private static final String ROUTE_TO_STATIC_FILES = "/src/main/resources/static";
    private ServerSocket serverSocket;
    private OutputStream out;
    private BufferedReader in;
    private WeatherService weatherService;


    public HttpServerImpl() {
        super();
        weatherService = new WeatherImpl();
    }

    @Override
    public void startServer() throws IOException {
        NanoSpark nanoSpark = null;
        int port = getPort();
        try {
            serverSocket = new ServerSocket(port);
            nanoSpark = new NanoSparkImpl(this);
            nanoSpark.get("/consulta?lugar=", this::getWeatherByCity);
            nanoSpark.get("/clima", this::redirectToIndexPage);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
            System.exit(1);
        }
        startAcceptingRequests(nanoSpark);
    }

    @Override
    public void getStaticFiles(String endpoint) {
        String fullPath = ROUTE_TO_STATIC_FILES + endpoint;
        if (endpoint.contains("html") || endpoint.contains("js") || endpoint.contains("jsx")) {
            getResource(fullPath);
        }
    }

    @Override
    public void getResource(String fullPath) {
        String type = fullPath.split("\\.")[1];
        try {
            in = new BufferedReader(new FileReader(System.getProperty("user.dir") + fullPath));
            String outLine = "";
            String line;
            while ((line = in.readLine()) != null) {
                outLine += line;
            }
            out.write(("HTTP/1.1 201 OK\r\n"
                    + "Content-Type: text/" + type + ";"
                    + "charset=\"UTF-8\" \r\n"
                    + "\r\n"
                    + outLine).getBytes());
        } catch (IOException e) {
            int statusCode = 404;
            printErrorMessage(statusCode,
                    "<!DOCTYPE html>\n"
                            + "<html>\n"
                            + "<head>\n"
                            + "<meta charset=\"UTF-8\">\n"
                            + "<title>" + statusCode + " Error</title>\n"
                            + "</head>\n"
                            + "<body>\n"
                            + "<h1>404 File Not Found</h1>\n"
                            + "</body>\n"
                            + "</html>\n", "Not Found");
        }
    }


    @Override
    public void printErrorMessage(int statusCode, String message, String statusName) {
        try {
            out.write(("HTTP/1.1 " + statusCode + " " + statusName + "\r\n"
                    + "\r\n"
                    + message).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public OutputStream getOut() {
        return out;
    }

    @Override
    public void setOut(OutputStream out) {
        this.out = out;
    }

    private void startAcceptingRequests(NanoSpark nanoSpark) throws IOException {
        running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            receiveRequest(clientSocket, nanoSpark);
        }
        serverSocket.close();
    }

    private void receiveRequest(Socket clientSocket, NanoSpark nanoSpark) throws IOException {
        out = clientSocket.getOutputStream();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        nanoSpark.setOut(out);
        String inputLine;
        //System.out.println(in.readLine());
        String endpoint;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Entra while******");
            //System.out.println(inputLine);
            if (inputLine.contains("GET")) {
                System.out.println("Entra primer if");
                endpoint = inputLine.split(" ")[1];
                //System.out.println(endpoint);
                if (!isSparkEndpoint(endpoint, nanoSpark)) getStaticFiles(endpoint);
            }
            if (!in.ready()) break;
        }
        in.close();
        clientSocket.close();
        System.out.println("Socket cerrado");
    }

    private boolean isSparkEndpoint(String endpoint, NanoSpark nanoSpark) {
        boolean isSparkEndpoint = false;
        if (endpoint.equals("/clima")) {
            nanoSpark.check(endpoint);
            isSparkEndpoint = true;
        }
        return isSparkEndpoint;
    }

    private String getWeatherByCity(Request req, Response res) {
        String weather = null;

        try {

            weather = weatherService.getWeatherByName(req.queryParams("value"));
        } catch (NanoSparkException e) {
            res.setError("No existe valor para la ciudad ingresada");
        }
        return weather;
    }


    private String redirectToIndexPage(Request req, Response res) {
        res.redirect("/index.html");
        return null;
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4444; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
