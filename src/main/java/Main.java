import com.fastcgi.FCGIInterface;
import handlers.RequestHandler;

public class Main {
    public static void main(String args[]) {
        var fcgiInterface = new FCGIInterface();
        var requestHandler = new RequestHandler();
        while (fcgiInterface.FCGIaccept() >= 0) {
            requestHandler.handleRequest(FCGIInterface.request);
        }
    }
}