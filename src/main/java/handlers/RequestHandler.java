package handlers;

import com.fastcgi.FCGIRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.RequestData;
import data.ResponseData;
import exceptions.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static consts.Consts.*;

@Slf4j
public class RequestHandler {
    private final AreaCheck areaCheck = new AreaCheck();
    private final DateTimeFormatter yyyymmddhhmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private FCGIRequest request;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RequestData.class, new RequestDataAdapter()) //используем кастомный
            .setPrettyPrinting()
            .create();


    public void handleRequest(FCGIRequest request) {
        var params = request.params;
        var requestMethod = params.getProperty("REQUEST_METHOD");
        log.info("Получен реквест со следующим методом: {}", requestMethod);
        this.request = request;
        try {
            chooseHTTPMethod(requestMethod);
        } catch (InvalidRequestException e) {
            sendError(e.getMessage());
        }
    }

    //todo: добавить поддержку истории
    private void chooseHTTPMethod(String requestMethod) throws InvalidRequestException {
        switch (requestMethod.toUpperCase()) {
            case "POST" -> handlePOST();
            case "GET" -> handleGET();
            case "DELETE" -> handleDELETE();
            default -> throw new InvalidRequestException("Invalid HTTP method");
        }
    }

    //todo: дописать метод
    private void handleDELETE() {
        log.info("Начинаем обрабатывать DELETE запрос");
    }

    //todo: дописать метод
    private void handleGET() {
        log.info("Начинаем обрабатывать GET запрос");
    }

    private void handlePOST() throws InvalidRequestException {
        log.info("Начинаем обрабатывать POST запрос");
        RequestData requestsArray = getRequestData();
        log.info("Распарсили реквест: {}", requestsArray);

        var responseArr = getResponseData(requestsArray);
        var response = responseArrayToJSON(responseArr);

        log.info("Готовый для отправки ответ: \n{}", response);

        sendResponse(response);
    }

    private String responseArrayToJSON(List<ResponseData> responseArr) {
        var json = gson.toJson(responseArr);
        return String.format(
                HTTP_RESPONSE,
                json.getBytes(StandardCharsets.UTF_8).length, json
        );
    }

    private List<ResponseData> getResponseData(RequestData requestsArray) {
        var response = new ResponseData();
        var responseArr = new ArrayList<ResponseData>();
        for (int x : requestsArray.x()) {
            response = createElementOfResponse(requestsArray, x);
            responseArr.add(response);
        }
        return responseArr;
    }

    private ResponseData createElementOfResponse(RequestData requestsArray, int x) {
        ResponseData response;
        long startTime = System.nanoTime();
        response = new ResponseData(x,
                requestsArray.y(),
                requestsArray.r());
        response.setIn(areaCheck.validate(response));
        response.setServerTime(LocalDateTime.now().format(yyyymmddhhmmss));
        response.setExecutionTime(System.nanoTime() - startTime);
        return response;
    }


    private RequestData getRequestData() throws InvalidRequestException {
        try {
            String requestString;
            requestString = readJSON();
            log.info("Получили следующий JSON при попытке чтения из буфера: {}", requestString);
            RequestData data = gson.fromJson(requestString, RequestData.class);
            log.info("Строка успешно привелась к объекту: {}", data.toString());
            return data;
        } catch (NullPointerException | IOException e) {
            throw new InvalidRequestException("Invalid JSON");
        }

    }

    private String readJSON() throws IOException {
        request.inStream.fill();
        var contentLength = request.inStream.available();
        var buffer = ByteBuffer.allocate(contentLength);
        var readBytes = request.inStream.read(
                buffer.array(),
                0,
                contentLength);
        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();
        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }

    private void sendError(String msg) {
        String json = String.format(ERROR_JSON, msg).trim();
        String response = String.format(HTTP_ERROR, json.getBytes(StandardCharsets.UTF_8).length, json);
        sendResponse(response);
    }

    private void sendResponse(String response) {
        try {
            log.info("Попытка записать полученные данные в request.outStream");
            request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
            log.info("Попытка смыть данные: request.outStream.flush()");
            request.outStream.flush();
            log.info("Данные отправлены без ошибок!");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправлении данных на клиент: ", e);
        }
    }
}
