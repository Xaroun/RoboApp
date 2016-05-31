package roboniania.com.roboniania_android.api.network;

public enum HttpMethod {
    GET("GET"), POST("POST"), PUT("PUT");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}

