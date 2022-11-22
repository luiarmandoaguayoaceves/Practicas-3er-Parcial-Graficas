import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.event.*;

public class Cilindro extends JFrame implements KeyListener
{

    private Graphics graPixel;
    private BufferedImage buffer;

    private Image auxImage;

    private int x,y,z;

    private boolean rotateY = true;

    private int traslationY;

    private double cosY;
    private double sinY;

    private int pointAccount;
    private int lineAccount;

    private int ancho, alto;

    private int[][][] points;

    public Cilindro(){
        ancho = 800;
        alto = 600;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Cilindro");
        setSize( ancho, alto );
        setResizable( true );
        setLocationRelativeTo( null );
        setVisible( true );

        this.addKeyListener(this);

        x=0;
        y=0;
        z=-1000;

        pointAccount = 38;
        lineAccount = 20;
        points = new int[lineAccount][pointAccount][3];

        traslationY = 0;

        System.out.println("Shift button to change between camera and effect");

        System.out.print("(LEFT = x--, RIGHT = x++ ) X:");
        System.out.println(x);

        System.out.print("(DOWN = y--, UP = y++ ) Y:");
        System.out.println(y);

        System.out.print("(Z = z--, X = z++ ) Z:");
        System.out.println(z);

        DefinePoints(35,10);

    }

    public static void main(String[] args){
        Cilindro model = new Cilindro();

        while(true){
            try{
                // Thread.sleep(50);
                // model.repaint();
            }
            catch(Exception ex){}
        }
    }

    public void DefinePoints(int size, int space ){
        int posY = -100;
        int newX, newZ;
        int angle = 0;

        for(int i = 0; i < pointAccount; i++){
            cosY = Math.cos(Math.toRadians(angle));
            sinY = Math.sin(Math.toRadians(angle));

            int r = (int)(size *  Math.cos(Math.toRadians(angle)));



            // newX = (int)( (sinY * size) );
            // newZ = (int)( (cosY * size) );
            newX = (int)( (sinY * r) );
            newZ = (int)( (cosY * r) );
            System.out.println("I: "+i+" X: "+newX+" Z: "+newZ + " R:"+r);
            for(int j=0;j<lineAccount;j++){
                points[j][i][0] = newX;
                points[j][i][1] = posY + (10*j);
                points[j][i][2] = newZ;
            }

            angle += space;
        }
        System.out.println("Check!");
    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_Y:
                // rotateY = !rotateY;
                this.repaint();
                break;
            case KeyEvent.VK_LEFT:
                x--;
                this.repaint();
                break;
            case KeyEvent.VK_RIGHT:
                x++;
                this.repaint();
                break;
            case KeyEvent.VK_UP:
                y++;
                this.repaint();
                break;
            case KeyEvent.VK_DOWN:
                y--;
                this.repaint();
                break;
            case KeyEvent.VK_Z:
                z--;
                this.repaint();
                break;
            case KeyEvent.VK_X:
                z++;
                this.repaint();
                break;
        }
    }

    private void putPixel( int x, int y, Color color){
        buffer.setRGB(x,y, color.getRGB());
    }
    public void paint(Graphics g){
        super.paint(g);

        auxImage = createImage(ancho, alto);
        graPixel = auxImage.getGraphics();
        update(graPixel);

        g.drawImage(auxImage,0,0,this);
    }

    public void update(Graphics g)
    {
        buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        int initialPosition[]={350,300,0};
        int[][][] pointsDraw = new int[lineAccount][pointAccount][3];
        if(rotateY){
            traslationY += 1;
            cosY = Math.cos(Math.toRadians( traslationY ));
            sinY = Math.sin(Math.toRadians( traslationY ));
            pointsDraw = Rotate();
        }



        for(int j=0; j<lineAccount;j++ )
            for (int i=0; i<pointAccount;i++ )
            {
                pointsDraw[j][i][0]=pointsDraw[j][i][0] + initialPosition[0];
                pointsDraw[j][i][1]=pointsDraw[j][i][1] + initialPosition[1];
                pointsDraw[j][i][2]=pointsDraw[j][i][2] + initialPosition[2];
            }

        // System.out.println("Check!2");

        for(int j=0; j<lineAccount;j++ ){
            for (int i = 1; i < pointAccount; i++) {

                try{
                    drawLine(pointsDraw[j][i-1], pointsDraw[j][i],Color.black);
                }
                catch(Exception ex){
                    System.out.println("Check!3 I:" + i + " J:"+j);
                }

                if(j<lineAccount-1){
                    drawLine(pointsDraw[j][i],pointsDraw[j+1][i],Color.black);
                    drawLine(pointsDraw[j][i],pointsDraw[j+1][i-1],Color.black);
                }
                // System.out.println("Check!3" + i);
            }
        }

        // System.out.println("Check!3");


        g.drawString( "Camera",10,500);
        g.drawString( "X: " + Integer.toString(x),10,525);
        g.drawString( "Y: " + Integer.toString(y),10,550);
        g.drawString( "Z: " + Integer.toString(z),10,575);

        g.drawImage(buffer, x, y, this);
    }

    public int[][][] Rotate(){
        int[] auxPos = new int[3];
        int[][][] returnPoints = new int[lineAccount][pointAccount][3];

        for(int j=0; j<lineAccount;j++ )
            for (int i=0; i<pointAccount;i++)
            {
                returnPoints[j][i][0] = (int)( (cosY * points[j][i][0]) - (sinY * points[j][i][2]) );
                returnPoints[j][i][1] = points[j][i][1];
                returnPoints[j][i][2] = (int)( (sinY * points[j][i][0]) + (cosY * points[j][i][2]) );
            }
        return returnPoints;
    }

    public void drawLine(int[] p1, int[] p2, Color color){

        boolean doLine = (z != 0);
        int u = (z != 0)? (p1[2])/z : 0;
        int[] p1_2D = new int[] {p1[0]-(x*u), p1[1]-(y*u)};
        u = (z != 0)? (p2[2])/z : 0;
        int[] p2_2D = new int[] {p2[0]-(x*u), p2[1]-(y*u)};

        // int[] p1_2D = new int[]{0,0};
        // if((p1[2]-z) != 0)
        // {
        //   p1_2D[0] = x-(z*(p1[0]-x))/(p1[2]-z);
        //   p1_2D[1] = y-(z*(p1[1]-y))/(p1[2]-z);
        // }
        // else{
        //   System.out.println("Check! /0");
        //   doLine = false;
        // }

        // int[] p2_2D = new int[] {0,0};
        // if((p2[2]-z) != 0){
        //   p2_2D[0] = x-(z*(p2[0]-x))/(p2[2]-z);
        //   p2_2D[0] = y-(z*(p2[1]-y))/(p2[2]-z);
        // }
        // else{
        //   System.out.println("Check! /0");
        //   doLine = false;
        // }

        if(doLine){
            float dx= p2_2D[0] - p1_2D[0];
            float dy= p2_2D[1] - p1_2D[1];
            float steps=Math.max(Math.abs(dx),Math.abs(dy));
            float xinc=dx/steps;
            float yinc=dy/steps;
            float ix=p1_2D[0];
            float iy=p1_2D[1];
            putPixel(Math.round(ix), Math.round(iy),color);
            for (int i=1;i<=steps ;i++ )
            {
                ix=ix+xinc;
                iy=iy+yinc;
                putPixel(Math.round(ix), Math.round(iy),color);
            }
        }
    }
}