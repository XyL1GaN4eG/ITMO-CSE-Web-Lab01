package handlers;

import com.fastcgi.FCGIRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.RequestData;
import data.ResponseData;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static consts.Consts.*;

@Slf4j
public class RequestHandler {
    private final AreaCheck areaCheck = new AreaCheck();
    private final DateTimeFormatter yyyymmddhhmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private FCGIRequest request;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(RequestData.class, new RequestDataAdapter()) //используем кастомный
            .setPrettyPrinting()
            .create();


    public void handleRequest(FCGIRequest request) {
        log.info("получен следующий реквест: {}", request);
        var params = request.params;
        //todo: нормальная ошибка

        var requestMethod = params.getProperty("REQUEST_METHOD");
        log.info("Получен реквест со следующим методом: {}", requestMethod);
        this.request = request;
        switch (requestMethod) {
            case "POST" -> handlePOST();
            case "GET" -> handleGET();
            default -> sendError("Пришел неизвестный метод");
        }
    }

    //todo: дописать метод
    private void handleGET() {
        log.info("Начинаем обрабатывать get запрос");
    }

    private void handlePOST() {
        log.info("Начинаем обрбатывать post запрос");
        log.info("Попытка getRequestData()");
        var requestsArray = getRequestData();
        try {
            log.info("Распарсили реквест: {}", requestsArray.toString());
        } catch (NullPointerException e) {
            log.error("Ошибка при парсинге requestsArray: ", e);
        }

        var responseArr = getResponseData(requestsArray);
        var response = responseArrayToJSON(responseArr);

        log.info("Провалидировали данные: \n{}", response);

        try {
            log.info("Попытка записать полученные данные в request.outStream");
            request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
            log.info("Попытка смыть данные: request.outStream.flush()");
            request.outStream.flush();

        } catch (IOException e) {
            log.error("Произошла ошибка при записи/смыве данных: {}", e.getMessage());
        }
    }

    private String responseArrayToJSON(ArrayList<ResponseData> responseArr) {
        var json = gson.toJson(responseArr);
        return String.format(
                HTTP_RESPONSE,
                json.getBytes(StandardCharsets.UTF_8).length, json
        );
    }

    private ArrayList<ResponseData> getResponseData(RequestData requestsArray) {
        var response = new ResponseData();
        var responseArr = new ArrayList<ResponseData>();
        for (int x : requestsArray.x()) {
            long startTime = System.nanoTime();
            response = new ResponseData(x,
                    requestsArray.y(),
                    requestsArray.r());
            response.setIn(areaCheck.validate(response));
            response.setServerTime(LocalDateTime.now().format(yyyymmddhhmmss));
            response.setExecutionTime(System.nanoTime() - startTime);
            responseArr.add(response);
        }
        return responseArr;
    }

    private void sendError(String msg) {
        try {
            //todo: нормальная ошибка пожалуйста
            String json = String.format(ERROR_JSON, "пук пук ниче не выполнилось т.к. " + msg).trim();
            String response = String.format(HTTP_ERROR, json.getBytes(StandardCharsets.UTF_8).length, json);
            System.out.println(response);
            request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
            request.outStream.flush();
        } catch (IOException ex) {
            log.error("Ошибка при отправке исключения {} на клиент: ", msg, ex);
        }
    }

    private RequestData getRequestData() {
        try {
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
            String requestString = new String(requestBodyRaw, StandardCharsets.UTF_8);
            log.info("Получили следующую строку при попытке парсинга: {}", requestString);
            RequestData data = gson.fromJson(requestString, RequestData.class);
            return data;
        } catch (IOException e) {
            log.error("Ошибка при парсинге RequestData: ", e);

            //создаем реквестдату которая точно не попадет в промежуток в случае ошибки парсинга
            return new RequestData(
                    new int[]{-20},
                    -20,
                    -20);
        }
    }
}
