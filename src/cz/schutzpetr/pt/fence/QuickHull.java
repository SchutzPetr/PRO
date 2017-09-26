package cz.schutzpetr.pt.fence;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by Petr Schutz on 25.09.2017
 * <p>
 * Pseudocode
 * Input = a set S of n points
 * Assume that there are at least 2 points in the input set S of points
 * QuickHull (S)
 * {
 * // Find convex hull from the set S of n points
 * Convex Hull := {}
 * Find left and right most points, say A & B, and add A & B to convex hull
 * Segment AB divides the remaining (n-2) points into 2 groups S1 and S2
 * where S1 are points in S that are on the right side of the oriented line from A to B,
 * and S2 are points in S that are on the right side of the oriented line from B to A
 * FindHull (S1, A, B)
 * FindHull (S2, B, A)
 * }
 * FindHull (Sk, P, Q)
 * {
 * // Find points on convex hull from the set Sk of points
 * // that are on the right side of the oriented line from P to Q
 * If Sk has no point, then return.
 * From the given set of points in Sk, find farthest point, say C, from segment PQ
 * Add point C to convex hull at the location between P and Q
 * Three points P, Q, and C partition the remaining points of Sk into 3 subsets: S0, S1, and S2
 * where S0 are points inside triangle PCQ, S1 are points on the right side of the oriented
 * line from  P to C, and S2 are points on the right side of the oriented line from C to Q.
 * FindHull(S1, P, C)
 * FindHull(S2, C, Q)
 * }
 * Output = Convex Hull
 *
 * @author Petr Schutz
 * @version 1.0
 */
class QuickHull {

    /**
     * Algoritmus vrátí body, tvořící konvexní schránku zadané množiny bodů
     *
     * @param points množina bodů
     * @return body tvořící konvexní schránku
     */
    static List<Point2D> quickHull(SortedSet<Point2D> points) {

        if (points.size() < 3) return new ArrayList<>(points);

        List<Point2D> convexHull = new ArrayList<>();

        final Point2D a = points.last();
        final Point2D b = points.first();

        convexHull.add(a);
        convexHull.add(b);

        points.remove(a);
        points.remove(b);

        List<Point2D> leftSet = new ArrayList<>();
        List<Point2D> rightSet = new ArrayList<>();

        points.forEach(point2D -> {
            byte pl = pointLocation(a, b, point2D);

            if (pl == -1) {
                leftSet.add(point2D);
            } else if (pl == 1) {
                rightSet.add(point2D);
            }
        });


        hullSet(a, b, rightSet, convexHull);
        hullSet(b, a, leftSet, convexHull);
        return convexHull;
    }

    /**
     * Vypočte vzdálenost bodu od přímky
     *
     * @param a bod a - přímka
     * @param b bod b - přímka
     * @param c bod, jehož vzdálenost od přímky chceme zjistit
     * @return vzdálenost bodu od přímky
     */
    private static int distance(Point2D a, Point2D b, Point2D c) {
        int num = (b.getX() - a.getX()) * (a.getY() - c.getY()) - (b.getY() - a.getY()) * (a.getX() - c.getX());
        return num < 0 ? -num : num;
    }

    /**
     * @param a       bod a - přímka
     * @param b       bod b - přímka
     * @param point2D bod, u kterého chceme zjisti, zda je na pravé či levé straně, případně zda leží na přímce
     * @return -1 pokud je na levé straně, 1 pokud je na pravé a 0 pokud leží na přímce
     */
    private static byte pointLocation(Point2D a, Point2D b, Point2D point2D) {
        int cp1 = (b.getX() - a.getX()) * (point2D.getY() - a.getY()) - (b.getY() - a.getY()) * (point2D.getX() - a.getX());
        return (byte) Integer.compare(cp1, 0);
    }

    /**
     * Zužuje množinu potenciálních bodů konvexního obalu dokud nezbyde jen ten
     *
     * @param a bod a přímky
     * @param b bod b přímky
     * @param set zbývající body
     * @param hull výsledek
     */
    private static void hullSet(Point2D a, Point2D b, List<Point2D> set, List<Point2D> hull) {
        int insertPosition = hull.indexOf(b);

        if (set.size() == 0) return;
        if (set.size() == 1) {
            set.remove(0);
            hull.add(insertPosition, set.get(0));
            return;
        }
        final int[] dist = {Integer.MIN_VALUE};
        final Point2D[] furthestP = {null};
        set.forEach(point2D -> {
            int distance = distance(a, b, point2D);
            if (distance > dist[0]) {
                dist[0] = distance;
                furthestP[0] = point2D;
            }
        });

        set.remove(furthestP[0]);
        hull.add(insertPosition, furthestP[0]);
        ArrayList<Point2D> leftSetAP = new ArrayList<>();
        ArrayList<Point2D> leftSetPB = new ArrayList<>();

        set.forEach(point2D -> {
            if (pointLocation(a, furthestP[0], point2D) == 1) {
                leftSetAP.add(point2D);
            }
            if (pointLocation(furthestP[0], b, point2D) == 1) {
                leftSetPB.add(point2D);
            }
        });

        hullSet(a, furthestP[0], leftSetAP, hull);
        hullSet(furthestP[0], b, leftSetPB, hull);
    }
}
