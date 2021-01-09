package agh.cs.oop.engine;

public class Stats {
    public int days;    // public?
    public double avgAliensQuantity;
    public double avgMushroomsQuantity;
    public double avgAliveEnergy;
    public double avgDeadLifetime;
    public double avgAliveChildrenQuantity;
    public long dominantLongGenotype;
    public String dominantPrettyGenotype;

    public Stats(int days, double avgAliensQuantity, double avgMushroomsQuantity, double avgAliveEnergy,
                 double avgDeadLifetime, double avgAliveChildrenQuantity, long dominantLongGenotype,
                 String dominantPrettyGenotype) {
        this.days = days;
        this.avgAliensQuantity = avgAliensQuantity;
        this.avgMushroomsQuantity = avgMushroomsQuantity;
        this.avgAliveEnergy = avgAliveEnergy;
        this.avgDeadLifetime = avgDeadLifetime;
        this.avgAliveChildrenQuantity = avgAliveChildrenQuantity;
        this.dominantLongGenotype = dominantLongGenotype;
        this.dominantPrettyGenotype = dominantPrettyGenotype;
    }
}
