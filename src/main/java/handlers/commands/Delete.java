package handlers.commands;

import data.ResponseData;

import java.util.List;

public class Delete extends HttpCommand {

    @Override
    public List<ResponseData> execute() {
        history.clear();
        return null;
    }
}
