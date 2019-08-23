package pl.ncdc.billiard.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;
import org.springframework.stereotype.Service;

import pl.ncdc.billiard.models.Ball;

@Service
public class MathService {

    public double diameter = 20; // do zmiany


    /**
     *
     * @param start
     * @param end
     * @return
     */
    public double findDistance(Point start, Point end) {

        double length = Math.sqrt((start.x - end.x) * (start.x - end.x) + (start.y - end.y) * (start.y - end.y));
        return length;

    }

    /**
     *
     * @param white Pozycja bialej bili
     * @param target Pozycja Virtualnej bili
     * @param pocket Pozycja luzy
     * @return Zwraca kat miedzy biala bila, bila VIRTUALNA i luza.
     */
    public double findAngle(Point white, Point target, Point pocket) {

        double p0c = Math.sqrt(Math.pow(target.x - white.x, 2) + Math.pow(target.y - white.y, 2));
        double p1c = Math.sqrt(Math.pow(target.x - pocket.x, 2) + Math.pow(target.y - pocket.y, 2));
        double p0p1 = Math.sqrt(Math.pow(pocket.x - white.x, 2) + Math.pow(pocket.y - white.y, 2));
        return Math.acos((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c));
    }

    /**
     *
     * @param white Pozycja bialek bili
     * @param target Pozycja Virtualnej bili
     * @param pocket Pozycja luzy
     * @return Zwraca kat miedzy biala bila, bila VIRTUALNA i luza.
     */
    public double findAngleOfCollision(Point selected, Point disturb, Point pocket) {

        double p0c = Math.sqrt(Math.pow(disturb.x - selected.x, 2) + Math.pow(disturb.y - selected.y, 2));
        double p1c = Math.sqrt(Math.pow(disturb.x - pocket.x, 2) + Math.pow(disturb.y - pocket.y, 2));
        double p0p1 = Math.sqrt(Math.pow(pocket.x - selected.x, 2) + Math.pow(pocket.y - selected.y, 2));

        return Math.acos(((p1c * p1c + p0c * p0c - p0p1 * p0p1) / (2 * p1c * p0c)));

    }


    public List<Double> abOfFunction(double xBallWhite, double yBallWhite, double xBallSelected, double yBallSelected) {
        List<Double> listOfAB = new ArrayList<>();
        double a = (yBallSelected - yBallWhite) / (xBallSelected - xBallWhite);
        double b = yBallSelected - a * xBallSelected;

        listOfAB.add(a);
        listOfAB.add(b);
        return listOfAB;
    }






}
