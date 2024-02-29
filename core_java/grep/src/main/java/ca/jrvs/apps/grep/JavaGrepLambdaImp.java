package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JavaGrepLambdaImp extends JavaGrepImp {

    @Override
    public List<String> readLines(File inputFile) {
        try (BufferedReader bufferedReader = new BufferedReader (new FileReader(inputFile))) {
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<File> listFiles(String rootDir) {
        try {
            File dir = new File(rootDir);
            return Arrays.stream(dir.listFiles())
                    .filter(file -> file.isFile())
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            throw new NullPointerException("ERROR: Invalid Input Filepath");
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep rootPath outFile");
        }

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            javaGrepLambdaImp.process();
        } catch (Exception ex) {
            javaGrepLambdaImp.logger.error("Error: Unable to process", ex);
        }
    }
}
