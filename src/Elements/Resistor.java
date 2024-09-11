package Elements;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Resistor extends Element{

    public Resistor(Node start, Node end, double value) {
        super(start, end, value, ElementType.RESISTOR);


    }

    @Override
    public void drawElement(Graphics g) {
        super.drawElement(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(0x8B4513));

        double angle = Math.atan2(getEnd().getPoint().y - getStart().getPoint().y, getEnd().getPoint().x - getStart().getPoint().x);
        int centerX = (getStart().getPoint().x + getEnd().getPoint().x)/2 + 5;
        int centerY = (getStart().getPoint().y + getEnd().getPoint().y)/2 + 5;


        Rectangle rectangle = new Rectangle(-15,-7,30,14);
        AffineTransform transform = new AffineTransform();
        transform.translate(centerX,centerY);
        transform.rotate(angle);

        Shape rotatedRect = transform.createTransformedShape(rectangle);
        g2d.fill(rotatedRect);
        g2d.draw(rotatedRect);

        g2d.drawLine(getStart().getPoint().x + 5, getStart().getPoint().y + 5, getEnd().getPoint().x + 5, getEnd().getPoint().y + 5);


    }
}
