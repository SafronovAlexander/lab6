package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent {
    BufferedImage Image;

    JImageDisplay(int width, int height){

        Image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        setPreferredSize(new Dimension(width, height));

    }
    public void paintComponent(Graphics g) {
        g.drawImage (Image, 0, 0, Image.getWidth(), Image.getHeight(), null);
    }

    void clearImage(){
        for (int x = 0; x < Image.getWidth(); x++){
            for (int y = 0; y < Image.getHeight(); y++){
                Image.setRGB(0,0, 0);
            }
        }
    }

    public void drawImage(int x, int y, int rgbColor){
        Image.setRGB(x, y,  rgbColor);
    }

    //void drawPixel(int x, int y, int rgbColor){
    //Image.setRGB(x, y, rgbColor);
    //}

    public BufferedImage getBufferedImage() {
        return Image;
    }
}
