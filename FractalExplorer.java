package com.company;
import java.awt.*;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import java.awt.event.*;
import  javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FractalExplorer extends JFrame{


    private int size;
    private JButton Reset;
    private JFrame frame;
    private JImageDisplay image;
    private JComboBox<Object> box;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double rectangle;
    private JButton Save;
    private int RemRows;

    FractalExplorer (int size){
        this.size = size;
        this.fractalGenerator = new Mandelbrot();
        this.rectangle = new Rectangle2D.Double();
        fractalGenerator.getInitialRange(this.rectangle);
        createAndShowGUI();
        drawFractal();
    }

    /*private void createAndShowGUI(){
        JFrame frame = new JFrame("Fractals");
        image = new JImageDisplay(size, size);
        Reset = new JButton("Reset");
        box = new JComboBox<>();
        box.addItem(new Mandelbrot());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Fractals: ");

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(box);

        JPanel panel2 = new JPanel();
        panel2.add(Reset);

        frame.add(image, BorderLayout.CENTER);
        frame.add(panel2, BorderLayout.SOUTH);
        Reset.addActionListener(new ButtonEventListener());
        image.addMouseListener(new MouseHandler());

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }*/

    public void createAndShowGUI() {
        frame = new JFrame("Fractals");
        image = new JImageDisplay(size, size);
        Reset = new JButton("Reset");
        Save = new JButton("Save image");
        JLabel label = new JLabel("Fractal: ");
        box = new JComboBox<>();
        box.addItem(new Mandelbrot());
        box.addItem(new Tricorn());
        box.addItem(new BurningShip());

        JPanel panelBox = new JPanel();
        panelBox.add(label);
        panelBox.add(box);

        JPanel panelForImage = new JPanel();
        panelForImage.add(image);

        JPanel panelBtn = new JPanel();
        panelBtn.add(Reset);
        panelBtn.add(Save);


        frame.add(panelBox, BorderLayout.AFTER_LINE_ENDS);
        frame.add(panelForImage, BorderLayout.CENTER);
        frame.add(panelBtn, BorderLayout.WEST);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ButtonEventListener ButtonEventListeneR = new ButtonEventListener();
        Reset.addActionListener(ButtonEventListeneR);
        Reset.addActionListener(ButtonEventListeneR);
        Save.addActionListener(ButtonEventListeneR);
        box.addActionListener(ButtonEventListeneR);
        image.addMouseListener(new MouseHandler());

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
        drawFractal();
    }

    public void enableUI(boolean val){
        if (val == true)
            frame.setEnabled(true);
        if (val == false)
            frame.setEnabled(false);
    }

    private void drawFractal(){
        /*for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                double xCoord = FractalGenerator.getCoord(rectangle.x, rectangle.x + rectangle.width, size, x);
                double yCoord = FractalGenerator.getCoord(rectangle.y, rectangle.y + rectangle.width, size, y);
                double numIters = fractalGenerator.numIterations(xCoord, yCoord);
                if (numIters == -1){
                    image.drawImage(x, y, 0);
                } else {
                    float hue = 0.7f + (float) numIters / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    image.drawImage(x, y, rgbColor);
                }
            }
        }
        image.repaint();*/
        enableUI(false);
        RemRows = size;
        for (int y = 0; y < size; y++){
            new FractalWorker(y).execute();
        }

    }

    //реализация кнопки Reset and Save
    public class ButtonEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == Reset){
                fractalGenerator.getInitialRange(rectangle);
                drawFractal();
            } else if (e.getSource() == box){
                fractalGenerator = (FractalGenerator) box.getSelectedItem();
                fractalGenerator.getInitialRange(rectangle);
                drawFractal();
            } else if(e.getSource() == Save){
                JFileChooser chooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "PNG");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
                    try{
                        ImageIO.write(image.getBufferedImage(), "png", new File(chooser.getSelectedFile() + "PNG"));
                    } catch(IOException ex){
                        System.out.println("Failed to save file");
                    }
                } else{
                    JOptionPane pane = new JOptionPane();
                    JOptionPane.showMessageDialog(pane, "ERROR", "Window of error",JOptionPane.ERROR_MESSAGE);
                    System.out.println("File was not chosen");
                }
            }
        }
    }

    public class MouseHandler implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            double mouseX = FractalGenerator.getCoord(rectangle.x, rectangle.x + rectangle.width, size, e.getX());
            double mouseY= FractalGenerator.getCoord(rectangle.x, rectangle.x + rectangle.width, size, e.getY());
            System.out.println(mouseX + " " + mouseY);
            if (e.getButton() == MouseEvent.BUTTON1){
                fractalGenerator.recenterAndZoomRange(rectangle, mouseX, mouseY, 0.5);
                drawFractal();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
    private class FractalWorker extends SwingWorker<Object, Object> {
        int y;
        int[] RGB;

        FractalWorker(int y) {
            this.y = y;
        }

        @Override
        protected Object doInBackground() throws Exception {
            RGB = new int[size];
            for (int x = 0; x < size; x++) {
                    double xCoord = FractalGenerator.getCoord(rectangle.x, rectangle.x + rectangle.width, size, x);
                    double yCoord = FractalGenerator.getCoord(rectangle.y, rectangle.y + rectangle.width, size, y);
                    double numIters = fractalGenerator.numIterations(xCoord, yCoord);
                    if (numIters == -1){
                        RGB[x] = 0;
                    } else{
                        float hue = 0.7f + (float) numIters / 200f;
                        int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                        RGB[x] = rgbColor;
                    }
            }
            return null;
        }

        protected void done() {
            for (int x = 0; x < size; x++){
                image.drawImage(x, y, RGB[x]);
           }
            image.repaint(0, 0, y, size, 1);
            RemRows -= 1;
            if (RemRows == 0)
                enableUI (true);
            super.done();
        }
    }


    public static void main(String[] args) {
        new FractalExplorer(600);
    }
}