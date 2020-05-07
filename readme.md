

```
@inproceedings{marx/starpath/sante/mtsr/2016,
  author = {Marx, Edgard and H{\"o}ffner, Konrad and Shekarpour, Saeedeh and Ngomo, Axel-Cyrille Ngonga and Lehmann, Jens and Auer, S{\"o}ren},
  booktitle = {Metadata and Semantics Research: 10th International Conference, MTSR 2016, G{\"o}ttingen, Germany, November 22-25, 2016, Proceedings},
  doi = {10.1007/978-3-319-49157-8_22},
  isbn = {978-3-319-49157-8},
  pages = {249--261},
  publisher = {Springer International Publishing},
  title = {Exploring Term Networks for Semantic Search over RDF Knowledge Graphs},
  url = {https://www.researchgate.net/publication/309700280_Exploring_Term_Networks_for_Semantic_Search_over_RDF_Knowledge_Graphs},
  year = 2016
}
```

SANTé stands for Semantic Search Engine and is designed to simplify RDF data access and exploration. 
SANTé covers different aspects of search engines such as indexing, ranking as well as interaction.
You can use SANTé via command line or via SANTé Web Interface (smile).

Following we discuss some of the basic functionalities to help you to instatiate your own SANTé instance in few steps.

- [SANTé in 5 minutes](https://github.com/AKSW/sante/blob/master/readme.md#sant%C3%A9-in-5-minutes)
- [Creating an index](https://github.com/AKSW/sante#creating-an-index)
- [Running SANTé](https://github.com/AKSW/sante/blob/master/readme.md#running-sant%C3%A9)

### SANTé in 5 minutes 

In this 5 minutes tutorial we will help you to instatiate your first knowledge base search engine over FOAF ontology using KBox [https://github.com/AKSW/KBox](https://github.com/AKSW/KBox).

1) Download KBox and instatiate the FOAF knowledge graph.

Download the jar here [https://github.com/AKSW/KBox/releases](https://github.com/AKSW/KBox/releases)
and run the following command:

```
java -jar kbox-vXXXX.jar -server -kb "http://xmlns.com/foaf/0.1,https://www.w3.org/2000/01/rdf-schema,http://www.w3.org/2002/07/owl,http://www.w3.org/1999/02/22-rdf-syntax-ns" -install
Loading Model...
Publishing service on http://localhost:8080/kbox/query
Service up and running ;-) ...
```
You can now access and query your knowledge graph at [http://localhost:8080](http://localhost:8080).
Notice that in the example above we also include RDFS, RDF and OWL ontologies, that's because we need their information for correctly instatiate FOAF ontology.
In case the SPARQL endpoint does not contain all necessary informaiton, SANTé will not be capable of retrieving or searching for it and will display the information as ```Unavailable```.

2) Download one of the availabes SANTé runnable jar files.

Download the jar here [https://github.com/AKSW/sante/releases](https://github.com/AKSW/sante/releases)

3) Create the index.

Assuming that you successfully performed step 1, 
```
java -jar sante-vXXX.jar index -endpoint http://localhost:8080/kbox/query -path \foaf_kg
```

4) Download one of the available smile WAR files (same version as the step 2).

Download the WAR here [https://github.com/AKSW/sante/releases](https://github.com/AKSW/sante/releases)

5) Instatiate smile
```
java -jar sante-vXXX.jar server -war sante-vXXX.war -path \foaf_kg -port 9090
```

If you correctly executed all the steps above, now you should be able to access SANTé at [http://localhost:9090](http://localhost:9090).

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
where:
  
    <endpoint> stands for the sparql endpoint.
	 
    <path>     stands for the target index directory.

### Running SANTé
1) Download one of the availabes SANTé runnable jar file.

2) Download the available SANTé WAR file.

2) Execute the following command:
```
java -jar sante.jar server -war <smile.war> -path <path> [-port <port>]
```
where:

    <war>  stands for smile war file.
	
    <path> stands for the target index directory.
 
    <port> stands for the server publishing port (default 8080).


### Starting smile using Jetty
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

### Create sante.main runnable jar file 

Run ```mvn package``` at the ```sante.main``` base directory:
```
mvn package
...
[INFO] org/apache/lucene/analysis/ already added, skipping
[INFO] org/apache/lucene/analysis/standard/ already added, skipping
[INFO] META-INF/LICENSE.txt already added, skipping
[INFO] META-INF/NOTICE.txt already added, skipping
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  43.329 s
[INFO] Finished at: 2020-05-07T23:31:50+02:00
[INFO] ------------------------------------------------------------------------
```
The runnable jar file will be generated at ```sante.main/target/sante.main-0.0.1-SNAPSHOT-jar-with-dependencies.jar```
