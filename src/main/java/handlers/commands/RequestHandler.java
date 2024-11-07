package handlers.commands;

import com.fastcgi.FCGIRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.ResponseData;
import exceptions.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static consts.Consts.*;

@Slf4j
public class RequestHandler {
    private FCGIRequest request;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void handleRequest(FCGIRequest request) {
        var params = request.params;
        var requestMethod = params.getProperty("REQUEST_METHOD");
        this.request = request;
        log.info("Получен реквест со следующим методом: {}", requestMethod);
        try {
            HttpCommand command = chooseHTTPMethod(requestMethod, request);
            var response = command.execute();
            if (response != null) {
//                String json = responseArrayToJSON(response);
                String json = stringToResponse();
                log.info("Получили непустой ответ: {}, начинаем отправлять", json);
                sendResponse(json);
            } else {
                log.info("Обработан запрос, не нуждающийся в ответе");
            }
        } catch (InvalidRequestException e) {
            sendError(e.getMessage());
        }
    }

    private HttpCommand chooseHTTPMethod(String requestMethod, FCGIRequest request) throws InvalidRequestException {
        return switch (requestMethod.toUpperCase()) {
            case "POST" -> new Post(request);
            case "GET" -> new Get();
            case "DELETE" -> new Delete();
            default -> throw new InvalidRequestException("Invalid HTTP method");
        };
    }

    private void sendError(String msg) {
        String json = String.format(ERROR_JSON, msg).trim();
        String response = String.format(HTTP_ERROR, json.getBytes(StandardCharsets.UTF_8).length, json);
        sendResponse(response);
    }

    private void sendResponse(String response) {
        System.out.println(response);
//        try {
//            log.info("Попытка записать полученные данные в request.outStream");
//            request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
//            log.info("Попытка смыть данные: request.outStream.flush()");
//            request.outStream.flush();
//            log.info("Данные отправлены без ошибок!");
//        } catch (IOException e) {
//            log.error("Произошла ошибка при отправлении данных на клиент: ", e);
//        }
    }

    private String responseArrayToJSON(List<ResponseData> responseArr) {
        var json = gson.toJson(responseArr);
        return String.format(
                HTTP_RESPONSE,
                json.getBytes(StandardCharsets.UTF_8).length, json
        );
    }

    private String stringToResponse() {
//        var json = gson.toJson();
        var json = "{\"x\": 1}";
        return String.format(
                HTTP_RESPONSE,
                json.getBytes(StandardCharsets.UTF_8).length, json
        );
    }
}
