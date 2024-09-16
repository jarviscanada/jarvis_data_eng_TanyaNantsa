package ca.jrvs.apps.jdbc;

public class SimpleCalculatorImp implements SimpleCalculator {

    @Override
    public int add(int x, int y) {
        return x+y;
    }

    @Override
    public int subtract(int x, int y) {
        return x-y;
    }

    @Override
    public int multiply(int x, int y) {
        return x*y;
    }

    @Override
    public double divide(int x, int y) {
        return (double) x /y;
    }

    @Override
    public int power(int x, int y) {
        return (int) Math.pow(x,y);
    }

    @Override
    public double abs(double x) {
        return Math.abs(x);
    }

//    public static void main(String[] args) {
//        SimpleCalculatorImp sc = new SimpleCalculatorImp();
//
//        System.out.println(sc.power(4, 2));
//    }

}