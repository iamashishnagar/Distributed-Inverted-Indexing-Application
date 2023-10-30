#!/bin/sh

# Compile Mobile/*.java files and include Hazelcast library in the classpath
javac -cp Mobile.jar:../lab8/hazelcast-5.1.3/lib/hazelcast-5.1.3.jar Mobile/*.java

# Create a JAR file with the compiled Mobile/*.class files
jar cvf Mobile.jar Mobile/*.class

# Compile other Java files with the Mobile.jar and Hazelcast library in the classpath
javac -cp Mobile.jar:../lab8/hazelcast-5.1.3/lib/hazelcast-5.1.3.jar *.java