set "BASEDIR=D:\weile-lab\code_gnn\old-joern"
java -Xmx4G -cp "%BASEDIR%/projects/extensions/joern-fuzzyc/build/libs/joern-fuzzyc.jar;%BASEDIR%/projects/extensions/jpanlib/build/libs/jpanlib.jar;%BASEDIR%/projects/octopus/lib/*" tools.parser.ParserMain -outformat csv -outdir parsed %1
