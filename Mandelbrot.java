package com.company;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator{
    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range){

        double newWidth = 3;
        double newHeight = 3;
        range.x = -2;
        range.y = -1.5;
        range.width = newWidth;
        range.height = newHeight;
    }

    @Override
    public int numIterations(double x, double y) {
        double xn = x;
        double yn = y;
        for (int i = 0; i < MAX_ITERATIONS; i++){
            double nextX = xn*xn - yn*yn + x;
            double nextY = 2 * xn * yn + y;
            xn = nextX;
            yn = nextY;
            if ((xn * xn + yn * yn) > 4)
                return i;
        }
        return -1;
    }
    public String toString(){
        return "Mandelbrot";
    }
}
