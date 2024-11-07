package consts;

public class Consts {
    public static final String HTTP_RESPONSE = """
            HTTP/1.1 200 OK
            Content-Type: application/json
            Content-Length: %d

            %s
            """;
    public static final String HTTP_ERROR = """
            HTTP/1.1 400 Bad Request
            Content-Type: application/json
            Content-Length: %d
                        
            %s
            """;
    public static final String ERROR_JSON = """
            {
                "reason": "%s"
            }
            """;
}
