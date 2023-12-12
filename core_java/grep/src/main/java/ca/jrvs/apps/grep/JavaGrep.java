package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep
{
    /**
     * Execute GREP request
     * @throws IOException If error occurs with reading the input file or writing the output file
     */
    void process() throws IOException;

    /**
     * Traverse a given directory and return all files
     * @param rootDir Input Directory
     * @return Files under the rootDir
     */
    List<File> listFiles(String rootDir);

    /**
     * Read a file and return a list of all its lines
     * @param inputFile: File to be read
     * @return List of lines
     * @throws IllegalArgumentException If a given inputFile is not a file
     */
    List<String> readLines(File inputFile);

    /**
     * Checks if a line contains the regex pattern provided by the user
     * @param line Input line to be scanned
     * @return true if line contains pattern, false if not
     */
    boolean containsPattern(String line);

    /**
     * Writes list of lines to file
     * @param lines List of strings that shall be printed as lines to the file
     * @throws IOException If any writes fails
     */
    void writeToFile(List<String> lines) throws IOException;

    String getRootPath();

    void setRootPath(String rootPath);

    String getRegex();

    void setRegex(String regex);

    String getOutFile();

    void setOutFile(String outFile);
}
