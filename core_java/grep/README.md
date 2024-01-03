# Introduction
This project is a simple version of the classic grep command line application. This application takes in an input directory that contains files, these files are scanned line-by-line, matched with a given regex pattern and those matched lines are printed in a specified output file. This project was developed using Java and its dependencies were managed via Maven.

# Quick Start
The easiest way is to launch this application is to open it as an IntelliJ IDEA project and launch it with your input command line parameters. The order of the parameters is `{Regex} {InputDirectoryPath} {OutputFileName}`

You can also pull the application as a Docker image using:

`docker pull shailosingh/grep`

 And then run the image in a container using the following command, in the `grep` directory:

 ```docker run --rm -v `pwd`/data:/data -v `pwd`/log:/log shailosingh/grep {Regex} {InputDirectoryPath} {OutputFileName}```

# Implemenation
## Pseudocode
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
A common issue that you run into with this type of program, is memory constraints. In the standard implementation of this program, we copy the entire contents of the current file being read, into memory. If this file is very large, this will cause huge memory issues. To resolve this issue, I rewrote the part that read the lines in the file and listed the files in the input directory, to use streams. This ensures that the program will only hold the current file object and current line in memory at once. This dramatically reduced memory utilization.

# Test
This application was tested manually using the following as command line arguments:

`.*Romeo.*Juliet.* ./data ./out/grep.txt`

# Deployment
To make deployment easier, the program was published as a docker image. This was done by editing the pom file, so the project would be compiled as a fat jar, with all of its dependencies bundled. Then, it was compiled with maven, the docker image was built locally and then pushed to Docker Hub.

# Improvement
1. I could make this project like full grep and allow inputs to be piped in and outputs to be piped out
2. Implement Grep as a library that can be used by other programs without having to interact with command line
3. I could create a GUI program that visually displays your text files and highlights the matched strings (Very Advanced)