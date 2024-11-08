import com.fastcgi.FCGIInterface;
import handlers.commands.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        log.info("Приложение запущено");
        var fcgiInterface = new FCGIInterface();
        var requestHandler = new RequestHandler();
        while (fcgiInterface.FCGIaccept() >= 0) {
            log.info("Входим в цикл");
            requestHandler.handleRequest(FCGIInterface.request);
        }
    }
}