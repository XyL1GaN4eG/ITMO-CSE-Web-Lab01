package handlers.commands;

import data.ResponseData;
import exceptions.InvalidRequestException;

import java.util.List;

public abstract class HttpCommand {
    protected final History history = History.getInstance();
    abstract List<ResponseData> execute() throws InvalidRequestException;
}