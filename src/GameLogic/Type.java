package GameLogic;

import java.io.Serializable;

public class Type implements Serializable {
    private String typename;
    private int defence;
    private int movementcost;


    public Type (String tn, int def, int mc){
        this.typename = tn;
        this.defence = def;
        this.movementcost = mc;
    }

    
    public void setTypename(String typename1) {
        this.typename = typename1;
    }
    
    public String getTypename() {
        return this.typename;
    }

    
    public void setDefence(int defence1) {
        this.defence = defence1;
    }

    
    public int getDefence() {
        return this.defence;
    }

    
    public void setMovementcost(int movementcost1) {
        this.movementcost = movementcost1;
    }

    
    public int getMovementcost() {
        return this.movementcost;
    }
}
