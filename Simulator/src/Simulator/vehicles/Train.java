/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.vehicles;

import Simulator.Container;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author erwin
 */
public class Train 
{
    private TrainControl trainControl;
    private float speed;
    
    public Node node;
    public Locomotive locomotive;
    public List<TrainCart> trainCarts;
    public boolean canDestroy = false;
    
    public Train(int length, Node rootNode, AssetManager assetManager, Spatial locomotiveModel, Spatial trainCartModel) 
    {
        float verticalRotation = FastMath.DEG_TO_RAD * -90.0f;
        this.node = new Node();
        this.trainCarts = new ArrayList<>();
        this.speed = 15.0f;
        
        this.locomotive = new Locomotive(this.node, assetManager, Vector3f.ZERO, locomotiveModel);
        
        this.initTrainCarts(length, assetManager, trainCartModel);
        
        rootNode.attachChild(this.node);
        this.trainControl = new TrainControl(this);
        this.node.setLocalTranslation(1715.0f, 0.0f, -724.5f);
        this.node.rotate(0.0f, verticalRotation, 0.0f);
        this.node.addControl(this.trainControl);
    }
    
    public void addContainers(List<Container> containers) {
        for (int i = 0; i < containers.size(); i++) {
            TrainCart trainCart = this.trainCarts.get(i);
            trainCart.attachContainer(containers.get(i));
        }
    }
    
    public void addContainer(Container container) {
        for (int i = 0; i < this.trainCarts.size(); i++) {
            this.trainCarts.get(i).attachContainer(container);
        }
    }
    
    public TrainCart getFirstFreeCart() {
        for (TrainCart cart : this.trainCarts) {
            if (cart.hasContainer()) {
                return cart;
            }
        }
        return null;
    }
    
    private void initTrainCarts(int length, AssetManager assetManager, Spatial trainCartModel) 
    {
        Vector3f position = Vector3f.ZERO;
        
        for (int i = 0; i < length; i++) {
            position = position.set(0.0f, 0.0f, -((this.locomotive.getPosition().x + 18.45f) * i) - 13.21f);
            this.trainCarts.add(new TrainCart(this.node, assetManager, position, trainCartModel.clone()));
        }
    }
    
    private void moveTo(Vector3f target) 
    {
        float distance = FastMath.abs(target.x - this.node.getLocalTranslation().x);
        MotionPath motionPath = new MotionPath();
        MotionEvent motionEvent;
        
        motionPath.addWayPoint(this.node.getLocalTranslation());
        motionPath.addWayPoint(target);
        
        motionEvent = new MotionEvent(this.node, motionPath);
        motionEvent.setInitialDuration(distance / this.speed);
        motionEvent.play();
    }
    
    public void moveOut() 
    {
        this.moveTo(new Vector3f(1715.0f, this.node.getLocalTranslation().y, this.node.getLocalTranslation().z));
    }
    
    public void moveIn() 
    {
        this.moveTo(new Vector3f(45.0f, this.node.getLocalTranslation().y, this.node.getLocalTranslation().z));
    }
    
}
