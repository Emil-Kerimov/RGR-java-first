package tasks;


public abstract class Point {
    private double[] coords = null;

    public Point(int num) {
        this.coords = new double[num];
    }

    public double getCoord(int num) {
        return coords[num - 1];
    }

    public void setCoord(int num, double x) {
        coords[num - 1] = x;
    }

    @Override
    public String toString() {
        String res = "(";
        for(double x : coords){
            res += x + ", ";
        }

        return res.substring(0, res.length()-2) + ")";
    }
}
