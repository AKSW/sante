

```
@InProceedings{Marx/KBox/ICSC/2017,
  Title                    = {{KBox}: {T}ransparently {S}hifting {Q}uery {E}xecution on {K}nowledge {G}raphs to the {E}dge},
  Author                   = {Edgard Marx and Ciro Baron and Tommaso Soru and S\"oren Auer},
  Booktitle                = {11th IEEE International Conference on Semantic Computing, Jan 30-Feb 1, 2017, San Diego, California, USA},
  Year                     = {2017}
}
```

SANTé stands for Semantic Search Engine and is designed to simplify RDF data access and exploration. 
SANTé covers different aspects of search engines, indexing, scoring functions as well as different interfaces.
You can use SANTé via command line or via a Web Interface available in our project sante.smile.

Following we discuss some of the basic functionalities to help you to instatiate your own SANTé instance in few steps.

- [Creating an index](https://github.com/AKSW/sante#creating-an-index)
- [Running SANTé](https://github.com/AKSW/sante#running-sante)

### Creating an index
Creating an index is pretty much streigh forward, you can do it via code or command line.
However, SANté uses triple stores to feed data to the index.
That's usefull for performing reasoning over the target knowledge graph.
To create the index you will first need to index your data in a triple store of your preference.

1) Download one of the availabes SANTé runnable jar file.

2) Execute the following command:
```
java -jar sante.jar index -endpoint <endpoint> -path <path>
```
whereas:
   <endpoint> is the sparql endpoint.
	 <path>	target index directory.

### Running SANTé
1) Download one of the availabes SANTé runnable jar file.

2) Download the available SANTé WAR file.

2) Execute the following command:
```
java -jar sante.jar server -war <smile.war> -path <path> [-port <port>]
```
whereas:
   <war> smile war file
	 <path> target index directory
	 <port> server publishing port (default 8080)


### Starting sante.smile using Jetty
Run the following command at sante.smile base directory (the directory where the project pom is).
Do not forget to point ```sante.smile``` to your index by changing the indexDir property locate at ```config.properties``` file in ```sante.smile/src/main/resources/``` directory.

```
mvn jetty:run

[INFO] Scanning for projects...
[INFO]
[INFO] ---------------------< org.aksw.sante:sante.smile >---------------------
[INFO] Building sante.smile 0.0.1-SNAPSHOT
[INFO] --------------------------------[ war ]---------------------------------
[INFO]
[INFO] >>> jetty-maven-plugin:9.4.8.v20171121:run (default-cli) > test-compile @ sante.smile >>>
[WARNING] The POM for commons-collections:commons-collections:jar:3.2.2 is invalid, transitive dependencies (if any) will not be available, enable debug logging for more details
```
