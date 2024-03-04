package ca.jrvs.apps.grep;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JavaGrepImp implements JavaGrep {

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
        for (File file : listFiles(getRootPath())) {
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
        try {
            File dir = new File(rootDir);
            if (!dir.exists()) {
                throw new FileNotFoundException("ERROR: Invalid Input Filepath");
            }
            recursiveListFile(dir, fileList);
        } catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch(NullPointerException e) {
            throw new NullPointerException("ERROR: Invalid Input Filepath");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileList;
    }

    private void recursiveListFile(File dir, List<File> fileList) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    recursiveListFile(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
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
        return line.matches(getRegex());
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        String filename = getOutFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter (new FileWriter(filename))) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new IOException("ERROR: Invalid Output Filepath");
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep rootPath outFile");
        }

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception ex) {
            System.err.println("Error: Unable to process");
            // Print the exception stack trace to the standard error stream
            ex.printStackTrace(System.err);
        }
    }
}
