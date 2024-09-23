package Elements;

import java.awt.*;

public class Wire extends Element {

    public Wire(Node start, Node end) {
        super(start, end, 0, ElementType.WIRE);
    }

    @Override
    public void drawElement(Graphics g) {
        super.drawElement(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setStroke(new BasicStroke(3));
        g.setColor(new Color(0x8A8A8A));
        g.drawLine(getStart().getPoint().x + 5, getStart().getPoint().y + 5, getEnd().getPoint().x + 5, getEnd().getPoint().y + 5);
    }
}
