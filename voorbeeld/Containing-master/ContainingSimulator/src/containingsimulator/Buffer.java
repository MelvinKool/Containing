/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Buffer row class for simulation, contains a spatial array of containers with
 * helper functions for proper usage inside the simulator
 * @author Wessel
 */
public class Buffer extends Node{
    Vector3f bufferSize;                //size of this buffer
    Container[][][] bufferArray;     	//spatial array off SimContainers
    Node bufferNode;                    //node to attach all containers to
    ParkingSpot[] pSpots;               //array of parking spots
    
    /**
     * Constructor, initializes full buffer for stress testing
     * @param bufferSize size of this buffer
     */
    public Buffer(Vector3f bufferSize){
        this.bufferSize = bufferSize;
        
        Vector3f csize = new Vector3f(1.22f, 1.22f, 6.705f); //container size in m divided by 2 because box size grows both ways
        bufferArray = new Container[(int)bufferSize.x][(int)bufferSize.y][(int)bufferSize.z];
        
        bufferNode = new Node();
        
        for(int i = 0; i < bufferSize.x; i++){
            for(int j = 0; j < bufferSize.y; j++){
                for (int k = 0; k < bufferSize.z; k++){
                    bufferArray[i][j][k] = new Container("asdf",
                            new Vector3f(i * 2 * csize.x, j * 2 * csize.y, k * 2 * csize.z),
                            csize);
                    bufferArray[i][j][k].setIndexPosition(new Vector3f(i, j, k));
                    bufferNode.attachChild(bufferArray[i][j][k]);
                }
            }
        }
        
        setRendering();
        this.attachChild(bufferNode);
    }
    
    /**
     * Empty constructor, initalizes empty buffer
     */
    public Buffer(){
        this.bufferSize = new Vector3f(6, 6, 26);
        bufferArray = new Container[(int)bufferSize.x][(int)bufferSize.y][(int)bufferSize.z];
        bufferNode = new Node();
        this.attachChild(bufferNode);
    }

    /**
     * Attach parking spots to both ends of the buffer
     * @param loc 
     */
    public void addParkingSpots(Vector3f loc){
        pSpots = new ParkingSpot[12];
        loc.z -= 20;
        for(int i = 0; i < pSpots.length; i++){
            if(i < 6){  //spots at north end of the buffer
                pSpots[i] = new ParkingSpot(new Vector3f(loc.x + 5.4f, 10, loc.z + 8 - (i * 5)), (float)Math.PI * 0.5f);
            }else{      //spots at opposite side
                pSpots[i] = new ParkingSpot(new Vector3f(loc.x + 5.4f, 10, loc.z + 338 + (i * 5)), (float)Math.PI * 0.5f);
            }
        }
    }
     
     /**
      * Find best ParkingSpot at this buffer
      * @return the most optimal ParkingSpot
      */
    public ParkingSpot getBestParkingSpot()
     {
     for (int i = 0; i < pSpots.length; i++) {
            if(pSpots[i].occupied == false) {
                return pSpots[i];
            }
        }
        return null;
     }
    
    /**
     * Find the best ParkingSpot at this buffer
     * @param up whether the AGV's home is at the top or bottom end of the buffer
     * @return the most optimal parking spot
     */
    public ParkingSpot getBestParkingSpot(boolean up){
        //up buffer or downside of the buffer
       int i = up ? 0 : pSpots.length/2;
       int max = up ? pSpots.length/2 : pSpots.length;
       
             while(i < max) 
             {
                 if(pSpots[i].occupied == false) 
                 {
                    return pSpots[i];
                 }
                  i++;
             }
           
      /* Original
        for (int i = 0; i < pSpots.length; i++) {
            if(pSpots[i].occupied == false) {
                return pSpots[i];
            }
        }*/
        return null;
    }
    
    /**
     * Gets the location of a ParkingSpot at this buffer for BufferCrane
     * @param up whether to check for a ParkingSpot at the top or the bottom of the buffer
     * @return the position of the parking spot
     */
    public Vector3f getSideVector3f(boolean up)
    {
       int i = up ? 0 : pSpots.length/2;
       return pSpots[i].translation; //accuracy doesn't matter, just a spot up ur down the buffer matters
    }
    
    /**
     * Find the actual position of a container inside this simulation
     * @param indexpos index of the container for which to find the position inside the simulation
     * @return the position of the container with this index inside the simulation
     */
    public Vector3f getRealContainerPosition(Vector3f indexpos){
        Vector3f csize = new Vector3f(1.22f, 1.22f, 6.705f);
        Vector3f pos = new Vector3f(indexpos.z * 2 * csize.x, indexpos.y * 2 *
                csize.y, indexpos.x * 2 * csize.z);
        return pos.add(this.getWorldTranslation());
    }
    
    /**
     * Set which geometries have to be drawn and which not
     */
    private void setRendering(){
        //cull walled-in containers
        for (int i = 0; i < bufferSize.x; i++){
            for (int j = 0; j < bufferSize.y; j++){
                for (int k = 0; k < bufferSize.z; k++){
                    if(bufferArray[i][j][k] != null){
                        bufferArray[i][j][k].updateRendering(bufferArray);
                    }
                }
            }
        }
    }
    
    /**
     * Fetch a container from the buffer
     * @param index buffer of container to fetch
     * @return the container that has been removed from this buffer
     */
    public Container removeContainer(Vector3f index){
        Container c1 = bufferArray[(int)index.x][(int)index.y][(int)index.z];
        if(c1 == null)
        {
            int a =0;
        }
        Container c2 = (Container)c1.clone();
        bufferNode.detachChild(c1);
        bufferArray[(int)index.x][(int)index.y][(int)index.z] = null;
        setRendering();
        return c2;
    }
    
    /**
     * Add a container to the buffer
     * @param index index where you want to add the container
     * @param c1  
     */
    public void addContainer(Vector3f index, Container c1){
        Container c2 = (Container)c1;
        bufferArray[(int)index.z][(int)index.y][(int)index.x] = c2;
        c2.setIndexPosition(new Vector3f(index.z, index.y, index.x));
        c2.setLocalTranslation(new Vector3f(index.z * 2 * c2.size.x,
                index.y * 2 * c2.size.y, index.x * 2 * c2.size.z)); //fit container into buffer
        bufferNode.attachChild(c2);
        setRendering();
    }
    
    /**
     * Find a container with a certain ID
     * @param id the container ID which to search for
     * @return a reference to the container with a matching ID
     */
    public Container getContainerByID(String id){
        Container tempCont = null;
        for(int i = 0; i < bufferArray.length; i++){
            for(int j = 0; j < bufferArray[0].length; j++){
                for(int k = 0; k < bufferArray[0][0].length; k++){
                    if(bufferArray[i][j][k] != null && bufferArray[i][j][k].id.equalsIgnoreCase(id)){
                        tempCont = bufferArray[i][j][k];
                        return tempCont;
                    }
                }
            }
        }
        return tempCont;
    }
}
