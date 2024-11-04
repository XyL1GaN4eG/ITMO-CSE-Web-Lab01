package handlers;

import data.ResponseData;

public class AreaCheck {
    int x;
    double y;
    double r;

    public boolean validate(ResponseData data) {
        this.x = data.getX();
        this.y = data.getY();
        this.r = data.getR();
        if (validateVars()) {
            return validateCircle() ||
                    validateRectangle() ||
                    validateTriangle();
        }
        return false;
    }

    private boolean validateVars() {

        return isVarInRange(x, -4, 4) &&
                isVarInRange(y, -5, 3) &&
                isVarInRange(r, 1, 4);
    }

    private boolean isVarInRange(Number var, Number min, Number max) {
        if (var == null) {
            return false;
        }
        double varDouble = var.doubleValue();
        double minDouble = min.doubleValue();
        double maxDouble = max.doubleValue();

        return varDouble >= minDouble && varDouble <= maxDouble;
    }


    private boolean validateRectangle() {
        return isVarInRange(x, r, 0) &&
                isVarInRange(y, r / 2, 0);
    }

    private boolean validateTriangle() {
        // kx+b < y
        return isVarInRange(x, 0, r/2) &&
                isVarInRange(y, 0, r) &&
                x * 2 - r <= y;
    }

    private boolean validateCircle() {
        // x^2+y^2 < r - формула круга + ограничение чтобы сделать его четвертью
        return pow(x) + pow(y) > pow(r) && x > 0 && y > 0;

    }

    private int pow(int var) {return var * var;}
    private double pow(double var) {return var * var;}
}
