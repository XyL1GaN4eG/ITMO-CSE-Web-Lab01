package data;

public record RequestData(int[] x, double y, double r) {
    @Override
    public String toString() {
        var res = "x = ";
        if (x != null) {
            for (int element : x) {
                res += element + ", ";
            }
        }
        res += "y = " + y + ", r = " + r;
        return res;
    }
}
