package handlers.commands;

import data.ResponseData;

import java.util.List;

public class Delete implements HttpCommand {
    private final History history = History.getInstance();

    @Override
    public List<ResponseData> execute() {
        history.clear();
        return null;
    }
}
