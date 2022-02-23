package edu.eci.arem.parcial1erTercio.nanospark.components;


import edu.eci.arem.parcial1erTercio.httpserver.HttpServer;

public class Response {
    private HttpServer httpServer;
    private String path;
    private int responseStatus;

    public Response(HttpServer httpServer, String path) {
        this.httpServer = httpServer;
        this.path = path;
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public void setHttpServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void redirect(String path) {
        httpServer.getStaticFiles(path);
    }

    public void setError(String message) {
        setResponseStatus(409);
        httpServer.printErrorMessage(409, message, "Conflict");
    }
}
