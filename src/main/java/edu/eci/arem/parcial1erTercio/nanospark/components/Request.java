package edu.eci.arem.parcial1erTercio.nanospark.components;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private final HashMap<String, String> valuesHashMap;

    public Request(String path) {
        valuesHashMap = new HashMap<>();
        int indexOfValue = path.indexOf("?");
        if (indexOfValue >= 0) {
            String values = path.substring(indexOfValue + 1);
            setValues(values);
        }
    }

    private void setValues(String values) {
        String[] valuesList = values.split("%26");
        for (String value : valuesList) {
            String[] valuePair = value.split("=");
            valuesHashMap.put(valuePair[0], valuePair[1]);
        }
    }

    public Map<String, String> queryParams() {
        return valuesHashMap;
    }

    public String queryParams(String key) throws NanoSparkException {
        if (valuesHashMap.isEmpty()) {
            throw new NanoSparkException("No hay valores asignados");
        }
        if (!valuesHashMap.containsKey(key)) {
            throw new NanoSparkException("No existe valor con el nombre " + key);
        }
        return valuesHashMap.get(key);
    }
}
