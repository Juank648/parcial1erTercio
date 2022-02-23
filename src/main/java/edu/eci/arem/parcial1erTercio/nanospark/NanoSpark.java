package edu.eci.arem.parcial1erTercio.nanospark;



import edu.eci.arem.parcial1erTercio.nanospark.components.Request;
import edu.eci.arem.parcial1erTercio.nanospark.components.Response;

import java.io.OutputStream;
import java.util.function.BiFunction;


public interface NanoSpark {

    void execute(String path, BiFunction<Request, Response, String> function);

    void get(String endpoint, BiFunction<Request, Response, String> function);

    void setOut(OutputStream out);

    OutputStream getOut();

    void check(String path);
}
