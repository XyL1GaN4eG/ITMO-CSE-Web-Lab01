package handlers.commands;

import data.ResponseData;
import exceptions.InvalidRequestException;

import java.util.List;

public interface HttpCommand {
    List<ResponseData> execute() throws InvalidRequestException;
}