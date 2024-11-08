package handlers.commands;

import data.ResponseData;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class History {
    @Getter
    private static final History instance = new History();
    private final List<ResponseData> history = Collections.synchronizedList(new ArrayList<>());

    public void add(ResponseData response) {
        history.add(response);
        log.info("Добавлен новый респонс в историю: {}", response);
    }

    public List<ResponseData> get() {
        return history;
    }

    public void clear() {
        history.clear();
    }
}
