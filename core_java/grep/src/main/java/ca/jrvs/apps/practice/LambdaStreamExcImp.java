package ca.jrvs.apps.practice;

import com.sun.imageio.plugins.jpeg.JPEGStreamMetadataFormat;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc{
    @Override
    //Stream.of converts strings to Stream
    public Stream<String> createStrStream(String... strings) {
        return Stream.of(strings);
    }

    @Override
    //map operation applies function to elements in strStream
    public Stream<String> toUpperCase(String... strings) {
        Stream<String> strStream = createStrStream(strings);
        return strStream.map(String::toUpperCase);
    }

    @Override
    //filter filters out values in stringStream based on a condition
    //  filter arg is a lambda expression (parameter -> statement)
    //                              or (parameter -> {statements;})
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return stringStream.filter(str -> !str.contains(pattern));
    }

    @Override
    //IntStream.of converts int array to IntStream
    public IntStream createIntStream(int[] arr) {
        return IntStream.of(arr);
    }

    @Override
    //use collect to convert Stream to list
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    @Override
    //boxed() converts IntStream to Stream<Integer>
    //used collect to convert to list
    public List<Integer> toList(IntStream intStream) {
        return intStream.boxed().collect(Collectors.toList());
    }

    @Override
    //IntStream.range method creates an ordered stream of
    //  sequential integers within a specified range. The range is exclusive of the upper bound
    public IntStream createIntStream(int start, int end) {
        return IntStream.range(start, end);
    }

    @Override
    //asDoubleStream() converts intstream to double stream
    // map() function takes every value in stream and applies sqrt fn
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream.asDoubleStream().map(Math::sqrt);
    }

    @Override
    //filter() Returns a stream consisting of the elements
    //  that match the given condition/predicate
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(x -> x % 2 != 0);
    }

    @Override
    //returns the lambda expression I want accept(msg) method to implement
    //  from functional interface Consumer<String>
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        return str -> System.out.println(prefix + str + suffix);
    }

    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        for (String message : messages) {
            printer.accept(message);
        }
    }

    @Override
    //first filters out odd numbers then, apply accept() to each element in stream
    //  x is a parameter and in this case represents an int in the stream
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        getOdd(intStream).forEach(x -> printer.accept(String.valueOf(x)));
    }

    @Override
    //flatmap applies stream conversion function and map function to each list
    //  in the stream, these resulting streams are flattened into one stream
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints.flatMap(list -> list.stream().map(num -> num * num));
    }

    public static void main(String[] args) {
        LambdaStreamExcImp streamExample = new LambdaStreamExcImp();

        // Example usage:
        String[] stringArray = {"Java", "is", "fun", "with", "streams"};
        int[]  array = {3, 45, 16, 6, 9, 0};

        Stream<String> stringStream = streamExample.createStrStream(stringArray);
        stringStream.forEach(System.out::println);

        Stream<String> upperCaseStream = streamExample.toUpperCase(stringArray);
        upperCaseStream.forEach(System.out::println);

        stringStream = streamExample.createStrStream(stringArray);
        Stream<String> filter = streamExample.filter(stringStream, "a");
        filter.forEach(System.out::println);

        IntStream intStreamExample = streamExample.createIntStream(array);
        intStreamExample.forEach(System.out::println);

        stringStream = streamExample.createStrStream(stringArray);
        List<String> list = streamExample.toList(stringStream);
        System.out.println(list);

        IntStream intStream = streamExample.createIntStream(array);
        List<Integer> intList = streamExample.toList(intStream);
        System.out.println(intList);

        intStream = streamExample.createIntStream(0,5);
        intStream.forEach(System.out::println);

        intStream = streamExample.createIntStream(0,5);
        DoubleStream doubleStream = streamExample.squareRootIntStream(intStream);
        doubleStream.forEach(System.out::println);

        intStream = streamExample.createIntStream(0,5);
        intStream = streamExample.getOdd(intStream);
        intStream.forEach(System.out::println);

        String[] messages = {"a","b", "c"};
        streamExample.printMessages(messages, streamExample.getLambdaPrinter("msg:", "!"));

        streamExample.printOdd(streamExample.createIntStream(0, 5), streamExample.getLambdaPrinter("odd number:", "!"));

        Stream<List<Integer>> nestedList = Stream.of(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9),
                Arrays.asList(10, 11, 12));
        Stream<Integer> integerStream = streamExample.flatNestedInt(nestedList);
    }
}
