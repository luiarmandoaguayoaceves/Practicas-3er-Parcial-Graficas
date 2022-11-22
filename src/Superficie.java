import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.event.*;

public class Superficie extends JFrame implements KeyListener
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

    private int ancho, alto;

    private int[][][] points;

    public Superficie(){
        ancho = 800;
        alto = 600;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Surface");
        setSize( ancho, alto );
        setResizable( true );
        setLocationRelativeTo( null );
        setVisible( true );

        this.addKeyListener(this);

        x=50;
        y=50;
        z=-450;

        pointAccount = 20;

        points = new int[10][pointAccount][3];

        traslationY = 0;

        System.out.println("Shift button to change between camera and effect");

        System.out.print("(LEFT = x--, RIGHT = x++ ) X:");
        System.out.println(x);

        System.out.print("(DOWN = y--, UP = y++ ) Y:");
        System.out.println(y);

        System.out.print("(Z = z--, X = z++ ) Z:");
        System.out.println(z);

        DefinePoints(35,25);

    }

    public static void main(String[] args){
        Superficie model = new Superficie();

        while(true){
            try{
                Thread.sleep(50);
                model.repaint();
            }
            catch(Exception ex){}
        }
    }

    public void DefinePoints(int size, int space ){
        int posX = -300;
        for(int i = 0; i < pointAccount; i++){
            points[0][i][0] = posX;
            points[0][i][1] = (int)(Math.cos(Math.toRadians(posX)) * size);
            points[0][i][2] = -40;

            points[1][i][0] = posX;
            points[1][i][1] = points[0][i][1];
            points[1][i][2] = -10;

            points[2][i][0] = posX;
            points[2][i][1] = points[0][i][1];
            points[2][i][2] = 20;

            points[3][i][0] = posX;
            points[3][i][1] = points[0][i][1];
            points[3][i][2] = 50;

            posX += space;
        }
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
                y--;
                this.repaint();
                break;
            case KeyEvent.VK_DOWN:
                y++;
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

        int initialPosition[]={350,300,-120};
        int[][][] pointsDraw = new int[4][pointAccount][3];

        if(rotateY){
            traslationY += 1;
            cosY = Math.cos(traslationY * (0.0174532));
            sinY = Math.sin(traslationY * (0.0174532));
            pointsDraw = Rotate();
        }
        for(int j=0; j<4;j++ )
            for (int i=0; i<pointAccount;i++ )
            {
                pointsDraw[j][i][0]=pointsDraw[j][i][0] + initialPosition[0];
                pointsDraw[j][i][1]=pointsDraw[j][i][1] + initialPosition[1];
                pointsDraw[j][i][2]=pointsDraw[j][i][2] + initialPosition[2];
            }

        for(int j=0; j<4;j++ ){
            if(j<3){
                drawLine(pointsDraw[j][0], pointsDraw[j+1][0],Color.black);
            }

            for (int i = 1; i < pointAccount; i++) {
                drawLine(pointsDraw[j][i-1], pointsDraw[j][i],Color.black);
                if(j<3){
                    drawLine(pointsDraw[j][i],pointsDraw[j+1][i],Color.black);
                    drawLine(pointsDraw[j][i],pointsDraw[j+1][i-1],Color.black);
                }
            }
        }
        g.drawImage(buffer, x, y, this);
    }

    public int[][][] Rotate(){
        int[] auxPos = new int[3];
        int[][][] returnPoints = new int[4][pointAccount][3];

        for(int j=0; j<4;j++ )
            for (int i=0; i<pointAccount;i++)
            {
                returnPoints[j][i][0] = (int)( (cosY * points[j][i][0]) - (sinY * points[j][i][2]) );
                returnPoints[j][i][1] = points[0][i][1];
                returnPoints[j][i][2] = (int)( (sinY * points[j][i][0]) + (cosY * points[j][i][2]) );
            }
        return returnPoints;
    }

    public void drawLine(int[] p1, int[] p2, Color color){
        int u = (z != 0)? (p1[2])/z : 0;

        int[] p1_2D = new int[] {p1[0]-(x*u), p1[1]-(y*u)};

        u = (z != 0)? (p2[2])/z : 0;

        int[] p2_2D = new int[] {p2[0]-(x*u), p2[1]-(y*u)};

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