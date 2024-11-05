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
    private boolean isIn;
    private final DateTimeFormatter yyyymmddhhmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private FCGIRequest request;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public void handleRequest(FCGIRequest request) {
        log.info("получен следующий реквест: {}", request);
//        try {
        sendError("cvb");
        System.out.println("asd");
//            var params = request.params;
//            //todo: нормальная ошибка
//            if (params == null) {
//                sendError("Ну я хз рял");
//                return;
//            }
//            this.request = request;
//
//            if (params.getProperty("REQUEST_METHOD").equals("POST")) {
//                handlePOST();
//            }
//
//            sendError("Метод не тот!!");
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

    }

    private void handlePOST() {

        var requestsArray = getRequestData();

        var responseArr = getResponseData(requestsArray);
        var response = responseArrayToJSON(responseArr);

        try {
            request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
            request.outStream.flush();
        } catch (IOException e) {
            sendError(e);
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

    //TODO: Убрать отправку джава ошибки на клиент после дебага
    private void sendError(Exception e) {
        try {
            String json = String.format(ERROR_JSON, e.getMessage()).trim();
            String response = String.format(HTTP_ERROR, json.getBytes(StandardCharsets.UTF_8).length, json);
            request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
            request.outStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
            ex.printStackTrace();
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

            RequestData data = gson.fromJson(requestString, RequestData.class);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
