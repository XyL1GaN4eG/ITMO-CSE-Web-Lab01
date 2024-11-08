package handlers.commands;

import com.fastcgi.FCGIRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.InvalidRequestException;
import handlers.ResponseSender;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class RequestHandler {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ResponseSender responseSender = new ResponseSender();

    public void handleRequest(FCGIRequest request) throws IOException {
        try {
            // used command pattern for processing requests
            var command = chooseHTTPMethod(request);
            var result = command.execute();
            var json = gson.toJson(result);
            responseSender.sendResponse(request.outStream, json);
        } catch (InvalidRequestException e) {
            responseSender.sendError(request.outStream, e.getMessage());
        }
    }

    private HttpCommand chooseHTTPMethod(FCGIRequest request) throws InvalidRequestException {
        return switch (request.params.getProperty("REQUEST_METHOD").toUpperCase()) {
            case "POST" -> new Post(request);
            case "GET" -> new Get();
            case "DELETE" -> new Delete();
            default -> throw new InvalidRequestException("Invalid HTTP method");
        };
    }
}
