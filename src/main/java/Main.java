import com.fastcgi.FCGIInterface;
import handlers.RequestHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("Приложение запущено");
        var fcgiInterface = new FCGIInterface();
        log.info("Создан fcgiInterface");
        var requestHandler = new RequestHandler();
        log.info("Создан requestHandler");
        while (fcgiInterface.FCGIaccept() >= 0) {
            log.info("Входим в цикл");
            requestHandler.handleRequest(FCGIInterface.request);
        }
    }
}