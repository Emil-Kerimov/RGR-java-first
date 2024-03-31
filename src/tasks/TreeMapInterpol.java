package tasks;

import java.util.TreeMap;

public class TreeMapInterpol extends Interpolator {

    private TreeMap<Integer,Point2D> map;

    public TreeMapInterpol() {
        this.map = new TreeMap<>();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int numPoints() {
        return map.size();
    }

    @Override
    public void addPoint(Point2D pt) {
        if (map.isEmpty())
            map.put(0,pt);
        else
            map.put(getLastIndex()+1,pt);
    }

    @Override
    public Point2D getPoint(int i) {
        return map.get(i);
    }

    private Integer getLastIndex(){
        return map.lastKey();
    }

    @Override
    public void setPoint(int i, Point2D pt) {
        map.replace(i,pt);
    }

    @Override
    public void removeLastPoint() {
        map.remove(getLastIndex());
    }

    @Override
    public void sort() {
        for (int i = 0; i < numPoints(); i++) {
            for (int j = 0; j < numPoints()-1-i; j++) {
                if (getPoint(j).getX() >= getPoint(j+1).getX()){
                    var tmp = getPoint(j);
                    map.replace(j,getPoint(j+1));
                    map.replace(j+1,tmp);
                }
            }
        }
    }
}