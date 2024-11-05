package handlers.commands;

import com.google.gson.Gson;
import data.ResponseData;
import exceptions.InvalidRequestException;
import handlers.RequestDataAdapter;

import java.util.List;

public class Get implements HttpCommand {
    private final History history = History.getInstance();
    @Override
    public List<ResponseData> execute() {
        return history.get();
    }
}
