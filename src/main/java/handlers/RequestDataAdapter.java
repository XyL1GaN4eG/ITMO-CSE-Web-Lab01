package handlers;

import com.google.gson.*;
import data.RequestData;
import lombok.Getter;

import java.lang.reflect.Type;

public class RequestDataAdapter implements JsonDeserializer<RequestData> {
    @Override
    public RequestData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Парсим массив x_array из строк в int[]
        JsonArray xArray = jsonObject.getAsJsonArray("x_array");
        int[] x = new int[xArray.size()];
        for (int i = 0; i < xArray.size(); i++) {
            x[i] = Integer.parseInt(xArray.get(i).getAsString());
        }

        // Парсим y и r как double
        double y = jsonObject.get("y").getAsDouble();
        double r = jsonObject.get("r").getAsDouble();

        return new RequestData(x, y, r);
    }
}
