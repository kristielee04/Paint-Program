import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener {
    private int strokeSize, shapeStartX, shapeStartY, shapeEndX, shapeEndY;
    private Color strokeColor;
    private final Color backgroundColor;
    private boolean eraser, oval, rectangle, line, selectFill = false;
    private final BufferedImage paintFile;
    Canvas(int x, int y, Color color){
        this.setPreferredSize(new Dimension(x,y));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.strokeSize = 5;
        this.strokeColor = Color.black;
        this.backgroundColor = color;
        this.setBackground(color);

        paintFile = new BufferedImage(x, y, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = paintFile.createGraphics();
        g2d.setPaint(color);
        g2d.fillRect(0,0,x,y);
        g2d.dispose();
    }

    public void setStrokeSize(int i) {
        this.strokeSize = i;
    }

    public void setStrokeColor (Color color){
        this.strokeColor = color;
    }

    public void toggleEraser(){
        eraser = !eraser;
    }

    public void toggleOval(){
        rectangle = false;
        line = false;
        selectFill = false;
        oval = !oval;
    }

    public void toggleRectangle(){
        oval = false;
        line = false;
        selectFill = false;
        rectangle = !rectangle;
    }

    public void toggleLine(){
        rectangle = false;
        oval = false;
        selectFill = false;
        line = !line;
    }

    public void toggleSelectFill(){
        rectangle = false;
        oval = false;
        line = false;
        selectFill = !selectFill;
    }

    public void shapesOff(){
        oval = false;
        rectangle = false;
        line = false;
        selectFill = false;
    }

    public void clear(){
        Graphics2D g2d = paintFile.createGraphics();
        g2d.setPaint(backgroundColor);
        g2d.fillRect(0,0,paintFile.getWidth(),paintFile.getHeight());
        g2d.dispose();
        this.repaint();
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(paintFile, 0, 0, null);
    }

    private void drawPoint(int x, int y){
        Graphics2D g2d = paintFile.createGraphics();
        g2d.setStroke(new BasicStroke(strokeSize));
        if (eraser) {
            g2d.setPaint(backgroundColor);
        } else {
            g2d.setPaint(strokeColor);
        }
        g2d.fillRect(x, y, strokeSize, strokeSize);
        g2d.dispose();
        this.repaint();
    }

    private void drawShape(){
        Graphics2D g2d = paintFile.createGraphics();
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setPaint(strokeColor);
        if (oval) {
            g2d.drawOval(shapeStartX, shapeStartY, Math.abs(shapeStartX-shapeEndX), Math.abs(shapeStartY-shapeEndY));
        } else if(rectangle){
            g2d.drawRect(shapeStartX, shapeStartY, Math.abs(shapeStartX-shapeEndX), Math.abs(shapeStartY-shapeEndY));
        } else if(line || selectFill){
            g2d.drawLine(shapeStartX, shapeStartY, shapeEndX, shapeEndY);
        }
        g2d.dispose();
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!oval && !rectangle) drawPoint(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        shapeStartX = e.getX();
        shapeStartY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!eraser){
            shapeEndX = e.getX();
            shapeEndY = e.getY();
            drawShape();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if ((!oval && !rectangle && !selectFill && !line) || eraser){
            drawPoint(e.getX(), e.getY());
        } else if(selectFill){
            shapeEndX = e.getX();
            shapeEndY = e.getY();
            drawShape();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
    public void save(String filename) throws IOException {
        ImageIO.write(paintFile, "PNG", new File(filename+".png"));
    }
}
