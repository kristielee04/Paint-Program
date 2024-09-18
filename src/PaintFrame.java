import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PaintFrame extends JFrame implements ActionListener, ChangeListener {
    JButton colorPicker, clear, save;
    JToggleButton eraser;
    JRadioButton pen, rectangle, oval, line, selectFill;
    JSlider strokeSize;
    JLabel sizeLabel;
    Canvas canvas;
    ButtonGroup shapes = new ButtonGroup();
    PaintFrame(){
        this.setTitle("Paint");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        canvas = new Canvas(1000,800, Color.white);
        canvas.setBackground(Color.white);
        canvas.setOpaque(true);

        colorPicker = new JButton("Pick a color");
        colorPicker.addActionListener(this);
        colorPicker.setFocusable(false);

        eraser = new JToggleButton("Eraser");
        eraser.setFocusable(false);
        eraser.addActionListener(this);

        clear = new JButton("Clear");
        clear.setFocusable(false);
        clear.addActionListener(this);

        save = new JButton("Save Paint");
        save.setFocusable(false);
        save.addActionListener(this);

        pen = new JRadioButton("Pen");
        pen.setFocusable(false);
        pen.addActionListener(this);
        pen.setSelected(true);
        rectangle = new JRadioButton("Rectangle");
        rectangle.setFocusable(false);
        rectangle.addActionListener(this);
        oval = new JRadioButton("Oval");
        oval.setFocusable(false);
        oval.addActionListener(this);
        line = new JRadioButton("Line");
        line.setFocusable(false);
        line.addActionListener(this);
        selectFill = new JRadioButton("Select Fill");
        selectFill.setFocusable(false);
        selectFill.addActionListener(this);

        shapes.add(pen);
        shapes.add(rectangle);
        shapes.add(oval);
        shapes.add(line);
        shapes.add(selectFill);

        JPanel buttonHeader = new JPanel();
        buttonHeader.setBackground(Color.LIGHT_GRAY);
        buttonHeader.add(colorPicker);
        buttonHeader.add(eraser);
        buttonHeader.add(save);
        buttonHeader.add(clear);
        buttonHeader.add(pen);
        buttonHeader.add(rectangle);
        buttonHeader.add(oval);
        buttonHeader.add(line);
        buttonHeader.add(selectFill);

        strokeSize = new JSlider(1,100,5);
        strokeSize.addChangeListener(this);
        strokeSize.setPreferredSize(new Dimension(50,200));
        strokeSize.setOrientation(SwingConstants.VERTICAL);

        sizeLabel = new JLabel(String.valueOf(strokeSize.getValue()));
        sizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sizeLabel.setFont(new Font("Dialog", Font.BOLD, 20));

        JPanel sliderPanel = new JPanel();
        sliderPanel.setPreferredSize(new Dimension(50,200));
        sliderPanel.add(strokeSize);
        sliderPanel.add(sizeLabel, SwingConstants.CENTER);

        this.add(buttonHeader, BorderLayout.NORTH);
        this.add(sliderPanel, BorderLayout.WEST);
        this.add(canvas, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==colorPicker){
            Color color = JColorChooser.showDialog(colorPicker.getTopLevelAncestor(), "Pick a color", Color.BLACK);
            canvas.setStrokeColor(color);
        }
        if(e.getSource()==eraser){
            canvas.toggleEraser();
        }
        if(pen.isSelected()){
            canvas.shapesOff();
        }else if(e.getSource()==rectangle){
            canvas.toggleRectangle();
        }else if(e.getSource()==oval){
            canvas.toggleOval();
        }else if(e.getSource()==line){
            canvas.toggleLine();
        }else if(e.getSource()==selectFill){
            canvas.toggleSelectFill();
        }
        if(e.getSource()==clear){
            canvas.clear();
        }
        if(e.getSource()==save){
            JFileChooser fileChooser = new JFileChooser();

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    canvas.save(filePath);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        sizeLabel.setText(String.valueOf(strokeSize.getValue()));
        canvas.setStrokeSize(strokeSize.getValue());
    }
}
