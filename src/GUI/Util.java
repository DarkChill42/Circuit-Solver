package GUI;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

    public static double distanceFromLine(Point lineStart, Point lineEnd, Point mouseLocation) {
        double x0 = mouseLocation.x, y0 = mouseLocation.y;
        double x1 = lineStart.x, y1 = lineStart.y;
        double x2 = lineEnd.x, y2 = lineEnd.y;

        double numerator = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1);
        double denominator = Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));

        return numerator / denominator;
    }

    public static boolean isHoveringLine(Point lineStart, Point lineEnd, Point mouseLocation, int lineThickness) {
        return distanceFromLine(lineStart, lineEnd, mouseLocation) <= lineThickness &&
                isWithinLineSegment(mouseLocation, lineStart, lineEnd, lineThickness);
    }

    private static boolean isWithinLineSegment(Point p, Point lineStart, Point lineEnd, int thickness) {
        int x = p.x, y = p.y;
        int x1 = lineStart.x, y1 = lineStart.y;
        int x2 = lineEnd.x, y2 = lineEnd.y;

        int buffer = thickness / 2;

        return (x >= Math.min(x1, x2) - buffer && x <= Math.max(x1, x2) + buffer &&
                y >= Math.min(y1, y2) - buffer && y <= Math.max(y1, y2) + buffer);
    }

    public static String numberFormatter(double value, char unit) {
        StringBuilder stringBuilder = new StringBuilder();
        if(value == 0.0) return stringBuilder.append(value).append(unit).toString();

        if(value < 0.0) {
            value*=-1.0;
            stringBuilder.append('-');
        }

        if(value < 0.00001 && value >= 0.0000000001) {
            stringBuilder.append(round(Math.round(value*1000000.0 * 10.0) / 10.0, 2)).append('u').append(unit);
        } else if(value < 0.01 && value >= 0.00001) {
            stringBuilder.append(round(value*1000,2)).append('m').append(unit);
        } else if(value < 1000.0 && value >= 0.01) {
            stringBuilder.append(round(value,2)).append(unit);
        } else if(value >= 1000.0 && value < 1000000.0) {
            stringBuilder.append(round(value/1000.0,2)).append('k').append(unit);
        } else if(value >= 1000000.0) {
            stringBuilder.append(round(value/1000000.0,2)).append('M').append(unit);
        }
        return stringBuilder.toString();
    }

    public static double round(double value, int places) {
        return BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
}
