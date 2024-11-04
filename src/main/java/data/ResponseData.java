package data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {
    private int x;
    private double y;
    private double r;
    private boolean isIn;
    private long executionTime;
    private String serverTime;

    public ResponseData (int x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isIn = false;
    }
}
