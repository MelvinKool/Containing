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
    private Vector3f maxIndex;
    private Vector3f containerSize;
    
    public SortField(Vector3f position, Vector3f containerSize, Vector3f maxIndex) {
        this.position = position;
        this.maxIndex = maxIndex;
        this.containerSize = containerSize;
    }
    
    /**
     * Translate sortfield index to wold coordinates
     * @param x index
     * @param y index
     * @param z index
     * @return world coordinates
     */
    public Vector3f indexToCoords(int x, int y, int z) throws IndexOutOfBoundsException
    {
        float xPos;
        float yPos;
        float zPos;
        
        if (x > this.maxIndex.x || y > this.maxIndex.y || z > this.maxIndex.y) {
            throw new IndexOutOfBoundsException("SortfieldIndexBounds: " + this.maxIndex);
        }
        
        xPos = x * this.containerSize.x + this.position.x + (this.containerSize.x / 2);
        yPos = y * this.containerSize.y + this.position.y;
        zPos = z * this.containerSize.z + this.position.z + (this.containerSize.z / 2);
        
        return new Vector3f(xPos, yPos, -zPos);
    }
}
