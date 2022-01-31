# Install instructions

1. Install Java 1.8.0_141 or similar
2. Download and unzip gradle 2.0 from: https://services.gradle.org/distributions/gradle-2.0-bin.zip
4. Run `gradle-2.0/bin/gradle build -x joernTools`

# joern
====

**This version of joern has been discontinued.**

**Joern lives on at https://github.com/ShiftLeftSecurity/joern**

Source code analysis is full of graphs: abstract syntax trees, control
flow graphs, call graphs, program dependency graphs and directory
structures, to name a few. Joern analyzes a code base using a robust
parser for C/C++ and represents the entire code base by one large
property graph stored in a Neo4J graph database. This allows code to
be mined using complex queries formulated in the graph traversal
languages Gremlin and Cypher.

The documentation can be found [here](http://joern.readthedocs.org/en/latest/)
