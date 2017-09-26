package cz.schutzpetr.pro.fence;

import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Petr Schutz on 25.09.2017
 *
 * @author Petr Schutz
 * @version 1.0
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            List<Point2D> q = QuickHull.quickHull(loadData());

            double distance = 0;

            for (int j = 0; j < q.size(); j++) {
                System.out.println(q.get(j == 0 ? q.size() - 1 : j - 1) + " - " + q.get(j));
                distance += lineLength(q.get(j == 0 ? q.size() - 1 : j - 1), q.get(j));
            }
            System.out.println(Math.round(distance));

        }
    }

    /**
     * Compute
     *
     * @param a point a
     * @param b point b
     *
     * @return return length of line between point a and b
     */
    private static double lineLength(Point2D a, Point2D b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }

    private static SortedSet<Point2D> loadData() {
        int size = sc.nextInt();

        SortedSet<Point2D> points = new TreeSet<>();

        for (int i = 1; i <= size; i++) {
            points.add(new Point2D(sc.nextInt(), i));
        }

        return points;
    }
}
