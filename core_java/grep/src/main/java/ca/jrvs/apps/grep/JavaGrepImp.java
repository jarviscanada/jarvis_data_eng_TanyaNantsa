package ca.jrvs.apps.grep;

import com.sun.corba.se.impl.orb.ORBConfiguratorImpl;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JavaGrepImp implements JavaGrep {

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
    private String regex;
    private String rootPath;
    private String outFile;

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();
        //List<File> listFilesRecursive = listFiles(getRootPath());
        for (File file : listFiles(getRootPath())) {
            //List<String> readLines = readLines(file);
            for (String readLine : readLines(file)) {
                if (containsPatterns(readLine)) {
                    matchedLines.add(readLine);
                }
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<>();
        File dir = new File(rootDir);
        fileList = Arrays.asList(Objects.requireNonNull(dir.listFiles()));
        return fileList;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader (new FileReader(inputFile))) {
            String line;
            while ((line = bufferedReader.readLine())!= null){
                lines.add(line);
            }
        } catch (IllegalArgumentException | IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    @Override
    public boolean containsPatterns(String line) {
        return getRegex().contains(line); // or try .matches
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        String filename = "./out/output.txt";
        try (BufferedWriter bufferedWriter = new BufferedWriter (new FileWriter(filename))) {
            for (String line : lines) {
                bufferedWriter.write(line);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep rootPath outFile");
        }

//        //Use default logger config
//        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setRootPath(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception ex) {
            javaGrepImp.logger.error("Error: Unable to process", ex);
        }
    }
}
