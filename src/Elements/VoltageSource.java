package Elements;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class VoltageSource extends Element {

    public VoltageSource(Node start, Node end, double value) {
        super(start, end, value, ElementType.VOLTAGE_SOURCE);
    }

    @Override
    public void drawElement(Graphics g) {
        super.drawElement(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(0xC91C34));

        double angle = Math.atan2(getEnd().getPoint().y - getStart().getPoint().y, getEnd().getPoint().x - getStart().getPoint().x);
        int centerX = (getStart().getPoint().x + getEnd().getPoint().x)/2 + 5;
        int centerY = (getStart().getPoint().y + getEnd().getPoint().y)/2 + 5;

        int circleDiameter = 25;
        int circleRadius = circleDiameter / 2;

        Ellipse2D circle = new Ellipse2D.Double(-circleRadius, -circleRadius, circleDiameter, circleDiameter);

        AffineTransform transform = new AffineTransform();
        transform.translate(centerX, centerY);
        transform.rotate(angle);

        Shape rotatedCircle = transform.createTransformedShape(circle);
        g2d.fill(rotatedCircle);


        g2d.drawLine(getStart().getPoint().x + 5, getStart().getPoint().y + 5, getEnd().getPoint().x + 5, getEnd().getPoint().y + 5);

        g2d.setColor(Color.BLACK);

        Font font = new Font("Arial", Font.BOLD, 16);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);
        int letterX = centerX - metrics.stringWidth("V") / 2;
        int letterY = centerY - metrics.getHeight() / 2 + metrics.getAscent();

        g2d.drawString("V", letterX, letterY);
    }
}
