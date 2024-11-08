package handlers.commands;

import com.fastcgi.FCGIRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.RequestData;
import data.ResponseData;
import exceptions.InvalidRequestException;
import handlers.AreaCheck;
import handlers.RequestDataAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Post implements HttpCommand {
    private final FCGIRequest request;
    private final AreaCheck areaCheck = new AreaCheck();
    private final DateTimeFormatter yyyymmddhhmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final History history = History.getInstance();

    //for json to correct array
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(RequestData.class, new RequestDataAdapter()) //используем кастомный
            .setPrettyPrinting()
            .create();

    public Post(FCGIRequest request) {
        this.request = request;
    }

    @Override
    public List<ResponseData> execute() throws InvalidRequestException {
        RequestData requestsArray = getRequestData();
        log.info("Строка успешно привелась к объекту: {}", requestsArray.toString());
        return getResponseData(requestsArray);
    }

    private List<ResponseData> getResponseData(RequestData requestsArray) {
        var elementOfResponse = new ResponseData();
        var responseArr = new ArrayList<ResponseData>();
        for (int x : requestsArray.x()) {
            elementOfResponse = createElementOfResponse(requestsArray, x);
            history.add(elementOfResponse); // сохраняем в историю каждый обработанный элемент
            responseArr.add(elementOfResponse);
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
            return gson.fromJson(requestString, RequestData.class);
        } catch (NullPointerException | IOException e) {
            throw new InvalidRequestException("Invalid JSON");
        } catch (NumberFormatException ex) {
            throw new InvalidRequestException("Invalid input format: Floating-point precision too high.");
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
}
