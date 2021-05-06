# Zendesk Intern Code Challenge
# Version 1.0 of Ticket Viewer is here!
### [Download Ticket Viewer v1.0](https://github.com/akhsi1/Zendesk_Challenge/releases/download/1.0/Zendesk_Challenge_v1.0.zip)
### [Test Report v1.0](https://github.com/akhsi1/Zendesk_Challenge/raw/master/Test%20Report%20(Ticket%20Viewer%20v.1.0).pdf)

# Installation
## Mac OS
1. Open Terminal
2. Enter ```java -jar```
3. Drag the ```Zendesk_Challenge.jar``` file into the terminal and press enter

## Windows
  Run the ```Zendesk_Challenge.bat``` file
## IDE - IntelliJ
  Download the [Source code](https://github.com/akhsi1/Zendesk_Challenge) and open the project in IntelliJ.
  
  > Note: You will need a valid auth token for the program to work via source code
  > ```java private final String zendeskToken = ""; // Enter the Authentication Token here```

## Alternatively
  Open Console and ```cd``` to file directory
  Enter ```java -jar Zendesk_Challenge.jar```

> Note: Java JDK must be installed on your device for this program to work

# How To Use
There are only 4 operations!

### 1. Next Page (`N`)
  Enter the ```N``` key into the console to see the next page

### 2. Previous Page (`P`)
  Enter the ```P``` key into the console to see the next page

### 3. Ticket ID Number (`#ID number`)
  Enter a number. E.g. ```1``` to view ticket details of ticket with ID = 1

### 4. Quit (`Q`)
  Enter the ```Q``` key into the console to quit the program!

# Additional Libraries
### [Gson](https://github.com/google/gson)
#### Justification
Java JDK does not come with a standard JSON parsing library. 
- [GSON has the best performance in terms of parsing speed](https://www.overops.com/blog/the-ultimate-json-library-json-simple-vs-gson-vs-jackson-vs-json/) while being very lightweight.
- One of the most popular JSON libraries in Java, rivaled by [Jackson](https://github.com/FasterXML/jackson).
- Standardized − Gson is a standardized library that is managed by Google.
- Efficient − It is a reliable, fast, and efficient extension to the Java standard library. Optimized − The library is highly optimized.
- Support Generics − It provides extensive support for generics.
- Supports complex inner classes − It supports complex objects with deep inheritance hierarchies.
