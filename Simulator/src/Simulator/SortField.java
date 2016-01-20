/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.math.Vector3f;

/**
 *
 * @author erwin
 */
public class SortField
{
    private Vector3f position;
    private Vector3f size;
    private Vector3f containerSize;
    
    public SortField(Vector3f position, Vector3f size, Vector3f containerSize) {
        this.position = position;
        this.size = size;
        this.containerSize = containerSize;
    }
    
    /**
     * Translate sortfield index to wold coordinates
     * @param x index
     * @param y index
     * @param z index
     * @return world coordinates
     */
    public Vector3f indexToCoords(int x, int y, int z) 
    {
        float xPos = x * (this.size.x / this.containerSize.x) + this.position.x;
        float yPos = y * (this.size.y / this.containerSize.y) + this.position.y;
        float zPos = z * (this.size.z / this.containerSize.z) + this.position.z;
        return new Vector3f(xPos, yPos, zPos);
    }
}
