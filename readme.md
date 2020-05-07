

```
@inproceedings{marx/starpath/smart/mtsr/2016,
  added-at = {2020-05-04T15:15:21.000+0200},
  address = {Cham},
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
