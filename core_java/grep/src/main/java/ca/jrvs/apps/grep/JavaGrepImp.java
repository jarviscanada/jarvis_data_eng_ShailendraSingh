package ca.jrvs.apps.grep;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep
{
    //Datafields
    final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);
    private String Regex;
    private String RootPath;
    private String OutFile;

    public static void main(String[] args)
    {
        //Load arguments into program
        if(args.length != 3)
        {
            throw new IllegalArgumentException("INCORRECT USAGE! MUST BE USED LIKE: JavaGrep [regex] [rootPath] [outFile]");
        }
        String regex = args[0];
        String rootPath = args[1];
        String outPath = args[2];

        //Initialize logger
        BasicConfigurator.configure();

        //Setup the grep object
        JavaGrepImp grep = new JavaGrepImp();
        grep.setRegex(regex);
        grep.setRootPath(rootPath);
        grep.setOutFile(outPath);

        try
        {
            grep.process();
        }
        catch(Exception ex)
        {
            //REMEMBER to use log4j here
            grep.logger.error("ERROR OCCURED, COULD NOT PROCESS FILE: ", ex);
        }
    }

    @Override
    public void process() throws IOException
    {
        //Initialize variables
        ArrayList<String> matchedLines = new ArrayList<>();

        //Iterate through all files
        List<File> allFiles = listFiles(RootPath);
        logger.debug(String.format("Number of input files found: %d", allFiles.size()));
        for(File currentFile : allFiles)
        {
            //Go through every line of each file
            List<String> allLines = readLines(currentFile);
            for(String currentLine : allLines)
            {
                //If it contains the regex pattern, it is a matched line
                if(containsPattern(currentLine))
                {
                    matchedLines.add(currentLine);
                }
            }
        }

        //Write the final list of matched lines to the out file
        logger.debug("Number of matched lines: " + matchedLines.size());
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir)
    {
        //Ensures rootDir is a real directory
        Path rootDirectoryPath = Paths.get(rootDir);
        if(!Files.isDirectory(rootDirectoryPath))
        {
            throw new IllegalArgumentException();
        }

        //Iterate through all paths (FOLLOW_LINKS ensures an error will be thrown if there are cycles in the file tree)
        List<File> normalFilePaths;
        try(Stream<Path> filePathStream = Files.walk(rootDirectoryPath, FileVisitOption.FOLLOW_LINKS))
        {
            //Filter the filePaths to only be normal files and convert them to a list of File objects
            normalFilePaths = filePathStream.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(e);
        }

        return normalFilePaths;
    }

    @Override
    public List<String> readLines(File inputFile)
    {
        List<String> listOfLines;
        try
        {
            //This function uses UTF-8 charset by default
            listOfLines = Files.readAllLines(inputFile.toPath());
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(e);
        }

        return listOfLines;
    }

    @Override
    public boolean containsPattern(String line)
    {
        Pattern patternObject = Pattern.compile(Regex);
        Matcher matcherObject = patternObject.matcher(line);

        return matcherObject.find();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException
    {
        //Open Java file for writing
        try(PrintWriter writer = new PrintWriter(OutFile, "UTF-8"))
        {
            //Write every line
            for(String currentLine : lines)
            {
                writer.println(currentLine);
            }
        }
    }

    //Getters and Setters--------------------------------------------------------------------------
    @Override
    public String getRootPath()
    {
        return RootPath;
    }

    @Override
    public void setRootPath(String rootPath)
    {
        //Ensure the path is actually to a folder
        Path rootDirectoryPath = Paths.get(rootPath);
        if(!Files.isDirectory(rootDirectoryPath))
        {
            throw new IllegalArgumentException();
        }

        RootPath = rootPath;
    }

    @Override
    public String getRegex()
    {
        return Regex;
    }

    @Override
    public void setRegex(String regex)
    {
        Regex = regex;
    }

    @Override
    public String getOutFile()
    {
        return OutFile;
    }

    @Override
    public void setOutFile(String outFile)
    {
        File outputFile = new File(outFile);
        try
        {
            boolean isFileNewlyCreated = outputFile.createNewFile();
            OutFile = outFile;

            if(isFileNewlyCreated)
            {
                logger.debug(String.format("File Created: %s", outputFile.getAbsolutePath()));
            }
            else
            {
                logger.debug(String.format("File Already Exists: %s", outputFile.getAbsolutePath()));
            }
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}
