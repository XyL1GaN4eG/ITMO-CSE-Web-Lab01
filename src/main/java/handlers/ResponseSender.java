package handlers;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@NoArgsConstructor
@Slf4j
public class ResponseSender {
    public void sendResponse(OutputStream outStream, String jsonBody) {
        try {
            var headers = new Properties();
            headers.put("Content-Type", "application/json");
            headers.put("Content-Length", String.valueOf(jsonBody.getBytes(StandardCharsets.UTF_8).length));
            sendBuiltResponse(outStream, headers, jsonBody);
        } catch (IOException e) {
            log.error("Ошибка при отправке ответа: ", e);
        }
    }

    public void sendError(OutputStream outStream, String message) throws IOException {
        Properties headers = new Properties();
        headers.put("Content-Type", "text/html");
        headers.put("Content-Length", String.valueOf(message.getBytes(StandardCharsets.UTF_8).length));

        sendBuiltResponse(outStream, headers, message);
    }

    private void sendBuiltResponse(OutputStream outStream, Properties headers, String message) throws IOException {
        StringBuilder headerBuilder = new StringBuilder();
        for (String key : headers.stringPropertyNames()) {
            headerBuilder.append(key).append(": ").append(headers.getProperty(key)).append("\r\n");
        }
        headerBuilder.append("\r\n");
        outStream.write(headerBuilder.toString().getBytes());
        outStream.write(message.getBytes(StandardCharsets.UTF_8));
        outStream.flush();
    }
}
