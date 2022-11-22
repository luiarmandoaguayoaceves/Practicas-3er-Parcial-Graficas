import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.event.*;

public class CurvaExplicita extends JFrame implements KeyListener
{

    private Graphics graPixel;
    private BufferedImage buffer;

    private Image auxImage;

    private int x,y,z,c;

    private boolean rotateY = true;

    private boolean auxColor = true;

    private int traslationY;

    private double cosY;
    private double sinY;

    private int pointAccount;

    private int ancho, alto;

    private int[][] points;

    public CurvaExplicita()
    {
        ancho = 800;
        alto = 600;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Traslation");
        setSize( ancho, alto );
        setResizable( true );
        setLocationRelativeTo( null );
        setVisible( true );

        this.addKeyListener(this);

        //buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        x=50;
        y=50;
        z=-450;

        auxColor = false;
        c=254;

        pointAccount = 50;

        points = new int[pointAccount][3];

        traslationY = 0;

        System.out.println("Shift button to change between camera and effect");

        System.out.print("(LEFT = x--, RIGHT = x++ ) X:");
        System.out.println(x);

        System.out.print("(DOWN = y--, UP = y++ ) Y:");
        System.out.println(y);

        System.out.print("(Z = z--, X = z++ ) Z:");
        System.out.println(z);

        DefinePoints(80,25);
    }

    public static void main(String[] args)
    {
        CurvaExplicita model = new CurvaExplicita();

        while(true){
            try{
                Thread.sleep(100);
                model.repaint();
            }
            catch(Exception ex){}
        }
    }

    public void DefinePoints(int size, int space ){

        int posY = 200;

        int angle = 0;
        int newX, newZ;
        for(int i = 0; i < pointAccount; i++){
            cosY = Math.cos(angle * (0.0174532));
            sinY = Math.sin(angle * (0.0174532));

            newX = (int)( (sinY * size) );
            newZ = (int)( (cosY * size) );

            posY = (int)(posY * 0.96);

            points[i][0] = newX;
            points[i][1] = posY;
            points[i][2] = newZ;

            angle += space;
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
    public void paint(Graphics g)
    {
        super.paint(g);

        auxImage = createImage(ancho, alto);
        graPixel = auxImage.getGraphics();
        update(graPixel);

        g.drawImage(auxImage,0,0,this);
    }

    public void update(Graphics g)
    {
        buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        int initialPosition[]={400,300,-120};

        int[][] pointsDraw = new int[pointAccount][3];

        if(rotateY){
            traslationY += 1;
            cosY = Math.cos(traslationY * (0.0174532));
            sinY = Math.sin(traslationY * (0.0174532));
            pointsDraw = Rotate();
        }

        for (int i=0; i<pointAccount;i++ )
        {
            pointsDraw[i][0]=pointsDraw[i][0] + initialPosition[0];
            pointsDraw[i][1]=pointsDraw[i][1] + initialPosition[1];
            pointsDraw[i][2]=pointsDraw[i][2] + initialPosition[2];
        }

        if(c == 255 || c == 0) auxColor = !auxColor;
        c = auxColor? (c-1) : (c+1);

        for (int i = 1; i < pointAccount; i++) {
            //drawLine(pointsDraw[i-1], pointsDraw[i],new Color(c, c, c));
            drawLine(pointsDraw[i-1], pointsDraw[i],Color.black);
        }

        g.drawImage(buffer, x, y, this);
    }

    public int[][] Rotate(){
        int[] auxPos = new int[3];
        int[][] returnPoints = new int[pointAccount][3];

        for (int i=0; i<pointAccount;i++)
        {
            returnPoints[i][0] = (int)( (cosY * points[i][0]) - (sinY * points[i][2]) );
            returnPoints[i][1] = points[i][1];
            returnPoints[i][2] = (int)( (sinY * points[i][0]) + (cosY * points[i][2]) );
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