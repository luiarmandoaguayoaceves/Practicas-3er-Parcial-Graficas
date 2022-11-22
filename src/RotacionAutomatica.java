import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.event.*;

public class RotacionAutomatica extends JFrame implements KeyListener
{
    private Graphics graPixel;
    private BufferedImage buffer;

    private int x,y,z;

    private boolean rotateX = true, rotateY = true, rotateZ = true;

    private float traslationX,traslationY,traslationZ;

    private double cosX;
    private double cosY;
    private double cosZ;

    private double sinX;
    private double sinY;
    private double sinZ;


    public RotacionAutomatica()
    {
        int ancho = 800, alto = 600;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Traslation");
        setSize( ancho, alto );
        setResizable( false );
        setLocationRelativeTo( null );
        setVisible( true );

        this.addKeyListener(this);

        //buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        graPixel = buffer.createGraphics();

        x=50;
        y=50;
        z=-250;

        traslationX = 0;
        traslationY = 0;
        traslationZ = 0;

        System.out.println("Shift button to change between camera and effect");

        System.out.print("(LEFT = x--, RIGHT = x++ ) X:");
        System.out.println(x);

        System.out.print("(DOWN = y--, UP = y++ ) Y:");
        System.out.println(y);

        System.out.print("(Z = z--, X = z++ ) Z:");
        System.out.println(z);

    }

    public static void main(String[] args)
    {
        RotacionAutomatica model = new RotacionAutomatica();


        while(true){
            try{
                Thread.sleep(50);
                model.repaint();
            }
            catch(Exception ex){}

        }


    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
    public void keyPressed(KeyEvent e){

        switch (e.getKeyCode())
        {
            case KeyEvent.VK_Z:
                rotateZ = !rotateZ;
                this.repaint();
                break;
            case KeyEvent.VK_X:
                rotateX = !rotateX;
                this.repaint();
                break;
            case KeyEvent.VK_Y:
                rotateY = !rotateY;
                this.repaint();
                break;
        }
    }

    private void putPixel( int x, int y, Color color){
        buffer.setRGB(0,0, color.getRGB());
        this.getGraphics().drawImage(buffer, x, y, this);
    }
    public void paint(Graphics g)
    {
        super.paint(g);
        update(g);
    }
    public void update(Graphics g)
    {


        int size=20;
        int cubePosition[]={400,300,-20};
        int aux[][]={ {0,0,0}, {0,0,1}, {0,1,0}, {0,1,1}, {1,0,0}, {1,0,1}, {1,1,0}, {1,1,1}};
        int[][] cube = new int[8][3];
        for (int i=0; i<8;i++ )
        {
            for (int j=0;j<3;j++ )
            {
                //cube[i][j]=(aux[i][j]==1)?cubePosition[j]+size:cubePosition[j]-size;
                cube[i][j]=(aux[i][j]==1)?size:-size;
            }
        }

        if(rotateX){
            traslationX += 1;
            cosX = Math.cos(traslationX * (0.0174532));
            sinX = Math.sin(traslationX * (0.0174532));
            cube = RotateCube(0,cube,size);
        }

        if(rotateY){
            traslationY += 1;
            cosY = Math.cos(traslationY * (0.0174532));
            sinY = Math.sin(traslationY * (0.0174532));
            cube = RotateCube(1,cube,size);
        }

        if(rotateZ){
            traslationZ += 1;
            cosZ = Math.cos(traslationZ * (0.0174532));
            sinZ = Math.sin(traslationZ * (0.0174532));
            cube = RotateCube(2,cube,size);
        }







        for (int i=0; i<8;i++ )
        {
            for (int j=0;j<3;j++ )
            {
                cube[i][j]+=cubePosition[j];
            }
        }

        drawLine(new int[] {cube[1][0],cube[1][1],cube[1][2]},new int[] {cube[3][0],cube[3][1],cube[3][2]},Color.blue);
        drawLine(new int[] {cube[1][0],cube[1][1],cube[1][2]},new int[] {cube[5][0],cube[5][1],cube[5][2]},Color.blue);
        drawLine(new int[] {cube[7][0],cube[7][1],cube[7][2]},new int[] {cube[3][0],cube[3][1],cube[3][2]},Color.blue);
        drawLine(new int[] {cube[7][0],cube[7][1],cube[7][2]},new int[] {cube[5][0],cube[5][1],cube[5][2]},Color.blue);

        drawLine(new int[] {cube[0][0],cube[0][1],cube[0][2]}, new int[] {cube[2][0],cube[2][1],cube[2][2]},Color.orange);
        drawLine(new int[] {cube[0][0],cube[0][1],cube[0][2]}, new int[] {cube[4][0],cube[4][1],cube[4][2]},Color.orange);
        drawLine(new int[] {cube[6][0],cube[6][1],cube[6][2]}, new int[] {cube[2][0],cube[2][1],cube[2][2]},Color.orange);
        drawLine(new int[] {cube[6][0],cube[6][1],cube[6][2]}, new int[] {cube[4][0],cube[4][1],cube[4][2]},Color.orange);

        drawLine(new int[] {cube[0][0],cube[0][1],cube[0][2]}, new int[] {cube[1][0],cube[1][1],cube[1][2]},Color.black);
        drawLine(new int[] {cube[4][0],cube[4][1],cube[4][2]}, new int[] {cube[5][0],cube[5][1],cube[5][2]},Color.black);
        drawLine(new int[] {cube[6][0],cube[6][1],cube[6][2]}, new int[] {cube[7][0],cube[7][1],cube[7][2]},Color.black);
        drawLine(new int[] {cube[2][0],cube[2][1],cube[2][2]}, new int[] {cube[3][0],cube[3][1],cube[3][2]},Color.black);

        Font boldFont = new Font("Arial", Font.BOLD, 80);
        this.getGraphics().setFont(boldFont);
        this.getGraphics().setColor(new Color(125,125,125));

        this.getGraphics().drawString( "Rotation",10,500);
        this.getGraphics().drawString( "X: " + Boolean.toString(rotateX),10,525);
        this.getGraphics().drawString( "Y: " + Boolean.toString(rotateY),10,550);
        this.getGraphics().drawString( "Z: " + Boolean.toString(rotateZ),10,575);
    }

    public int[][] RotateCube(int pos, int[][] cube, int size){
        int[] auxPos = new int[3];
        switch(pos){
            case 0:
                for (int i=0; i<8;i++)
                {
                    auxPos[1] = (int)( (cosX * cube[i][1]) - (sinX * cube[i][2]) );
                    auxPos[2] = (int)( (sinX * cube[i][1]) + (cosX * cube[i][2]) );

                    cube[i][1] = auxPos[1];
                    cube[i][2] = auxPos[2];
                }
                break;
            case 1:
                for (int i=0; i<8;i++)
                {
                    auxPos[0] = (int)( (cosY * cube[i][0]) - (sinY * cube[i][2]) );
                    auxPos[2] = (int)( (sinY * cube[i][0]) + (cosY * cube[i][2]) );

                    cube[i][0] = auxPos[0];
                    cube[i][2] = auxPos[2];
                }
                break;
            case 2:
                for (int i=0; i<8;i++)
                {
                    auxPos[0] = (int)( (cosZ * cube[i][0]) - (sinZ * cube[i][1]) );
                    auxPos[1] = (int)( (sinZ * cube[i][0]) + (cosZ * cube[i][1]) );

                    cube[i][1] = auxPos[1];
                    cube[i][0] = auxPos[0];
                }
                break;
        }


        return cube;
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