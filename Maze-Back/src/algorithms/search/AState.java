package algorithms.search;

import java.io.Serializable;

public abstract class AState implements Comparable<AState>, Serializable {

    protected int cost;
    protected AState cameFrom;

    public AState(int cost) {
        this.cost = cost;
        this.cameFrom = null;
    }

    public void setS(AState s) {
        this.cameFrom = s;
    }

    public AState getS() {
        return cameFrom;
    }

    public abstract boolean equals(Object o);

    public int hashCode(){
        String state = this.toString();
        return state != null ? state.hashCode() : 0;
    }

    public int getCost() {
        return cost;
    }

    public abstract String toString();

    public int compareTo(AState o) {
            if (this.cost > o.cost)
                return 1;
            if (this.cost < o.cost)
                return -1 ;
            return 0;

        }

}