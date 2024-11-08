package handlers.commands;

import data.ResponseData;

import java.util.List;

public class Get implements HttpCommand {
    private final History history = History.getInstance();

    @Override
    public List<ResponseData> execute() {
        return history.get();
    }
}
