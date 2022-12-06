# CS271_project

#Compile Steps:
1. Go under the src folder
2. Input the following commands:

```sh
javac cs271project/*.java
jar cfe Project.jar src/cs271project.Main src/cs271project/*.class
```

After finish compiled, please move the jar file to the folder that contains the input files

#Build Environment:
Java Version >=8  (build and tested with JavaSE-17)

#Required hardware Environment:
RAM >= 9G (it used around 8.5G in DFS for heuristic when number of locations is large)