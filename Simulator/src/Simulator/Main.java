package Simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

public class Main extends SimpleApplication
{
    Object AGV = new Object();
    Connection connection;
    Thread readThread;

    public static void main(String[] args)
    {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp()
    {
        flyCam.setEnabled(false);
        
        readThread = initReadThread();
        readThread.start();
        
        //Yes, i removed the stupid cube ;p
    }
    
    @Override
    public void simpleUpdate(float tpf)
    {
        //TODO Depending on wich way you're going (XYZ) 
        //float afstand = AGV.GetMaxSpeed()*tpf;
        //AGV.SetLocalTranslation(afstand);
        //AGV.afstandToGo -= afstand;
        //This kinda works, but it doesn't, since I don't specify the X, Y or Z
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
        
    }
    
    //This is important to properly close
    //the connection with the server.
    @Override
    public void destroy()
    {
        super.destroy();
        readThread.stop();
        connection.stop();
    }
    
    /**@param verplaatsing afstand die afgelegd moet worden
     * @param snelheid snelheid waarmee het object zich beweegt
     * @return
     */
    public float movementTijd(int verplaatsing,float snelheid)
    {
        //The AGV always moves at top speed, because reasons
        float tijd = verplaatsing/snelheid;
        return tijd;
    }
    
    private Thread initReadThread(){
        return new Thread(new Runnable()
        {
            public void run() {
                try
                {
                    connection = new Connection();
                    while(true)
                    {
                        //What to do with the input?
                        System.out.println(connection.read());
                    }
                }
                catch(Exception e)
                {
                    //Always throws a exception after the socket is closed.
                    //System.out.println(e);
                }
            }
        });
    }
}