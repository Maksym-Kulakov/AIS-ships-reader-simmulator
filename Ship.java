package simulator;

public class Ship {
    int mmsi;
    double latid;
    double longit;
    String destin;

    public Ship() {
    }

    public Ship(int mmsi, double latid, double longit, String destin) {
        this.mmsi = mmsi;
        this.latid = latid;
        this.longit = longit;
        this.destin = destin;
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(int mmsi) {
        this.mmsi = mmsi;
    }

    public double getLatid() {
        return latid;
    }

    public void setLatid(double latid) {
        this.latid = latid;
    }

    public double getLongit() {
        return longit;
    }

    public void setLongit(double longit) {
        this.longit = longit;
    }

    public String getDestin() {
        return destin;
    }

    public void setDestin(String destin) {
        this.destin = destin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ship)) return false;

        Ship ship = (Ship) o;

        if (mmsi != ship.mmsi) return false;
        if (Double.compare(ship.latid, latid) != 0) return false;
        if (Double.compare(ship.longit, longit) != 0) return false;
        return destin != null ? destin.equals(ship.destin) : ship.destin == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mmsi;
        temp = Double.doubleToLongBits(latid);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longit);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (destin != null ? destin.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "mmsi=" + mmsi +
                ", latid=" + latid +
                ", longit=" + longit +
                ", destin='" + destin + '\'' +
                '}';
    }
}

