package tasks;

import java.io.*;
import java.util.StringTokenizer;

public class FileTreeSetInterpol extends TreeSetInterpol{

    public FileTreeSetInterpol() {
        super();
    }

    public void readFromFile(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String s = in.readLine();
        clear();
        while ((s = in.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(s);
            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());
            addPoint(new Point2D(x, y));
        }
        in.close();
    }
    public void writeToFile(String fileName) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        out.printf("%9s%25s\n", "x", "y");

        var iterator =  getIterator();
        while (iterator.hasNext()) {
            var val = iterator.next();
            out.println(val.getX() + "\t" + val.getY());
        }
        out.close();
    }
}