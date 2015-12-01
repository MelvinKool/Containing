package org.nhl.containing.vehicles;

public abstract class Transporter extends Vehicle {
    public Transporter(int id) {
        super(id);
    }
    
    public abstract void arrive(int location);
    public abstract void depart();
    
}
