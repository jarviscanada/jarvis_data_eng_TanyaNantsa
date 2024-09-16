# Introduction
The Grep Java application behaves similarly to the Linux grep command, 
identifying string patterns within files and returning lines containing 
the matching pattern. The application, implemented in Java, utilized Maven 
for building the java project (including compiling code and packaging 
artifacts), managing dependencies, enforcing a consistent project structure,
 and cleaning the package artifacts in the project. IntelliJ IDE facilitated
 code editing, running, and debugging. Lambda and Stream API enhanced the 
 application, integrating lazy evaluations and concise code for increased
 readability. Finally, Docker was employed to build a Docker image for the
 app and then push the image to Docker Hub.  

# Quick Start
First pull the Docker image from Docker Hub:  
`docker pull tanyanantsa/grep:latest`  
Run the Container instance:  
`docker run -it --rm -v `pwd`/data:/data -v `pwd`/out:/out tanyanantsa/grep:latest .*Shakespeare.* /data /out/grep.out`  


# Implementation
## Pseudocode
```
matchedLines = []  
For each file in listFiles(getRootPath())  
	For each line in readLines(file)  
		matchedLines.add(line)  
	End for  
End for  
writeToFile(matchedLines)  
```

## Performance Issue
The memory usage is dependent of the input size and issue arises when 
the grep app needs to process data larger than heap memory. One potential
 solution would be to implement streams since they process data only when
 necessary and doesn?t need to load the entire dataset into memory at once. 

# Test
I conducted manual testing on my application by first preparing sample data
 in the data directory. I then executed test cases, comparing the results of
 my application with those obtained using the grep Linux terminal command.
 A test was considered passed if the results matched.

# Deployment
To enable easy distribution and deployment, this application has been Dockerized.  
The steps below explain how I built Docker image:

### Package the java app  
`mvn clean package`

### Build a new docker image locally  
`docker build -t tanyanantsa/grep .`

### Push the image to Docker Hub  
`docker push tanyanantsa/grep `


# Improvement
- Enhance the application to return streams, allowing for efficient processing of large datasets by utilizing lazy evaluation.  
- Improve error checking within the application to ensure more robust handling of various input scenarios, providing better feedback to users.  
- Implement an option to return line numbers and file name along with the lines that match the pattern, offering users additional context and flexibility in analyzing results.  


