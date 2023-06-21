# Mulang: A Multilingual Search Engine for RDF data

<img src="https://github.com/AKSW/sante/raw/master/sante.png"/>
<img src="http://www.prowebdesign.ro/wp-content/uploads/2012/07/iStock_000019691100XSmall.jpg"/>

<img src="https://github.com/AKSW/sante/raw/master/sante.gif" width="600" height="420" />

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

Following we discuss some of the basic functionalities to help you to instantiate your own SANTé instance in few steps.

- [Showcases](https://github.com/AKSW/sante/blob/master/readme.md#showcases)
- [SANTé in 5 minutes](https://github.com/AKSW/sante/blob/master/readme.md#sant%C3%A9-in-5-minutes)
- [Creating an index](https://github.com/AKSW/sante#creating-an-index)
- [Running SANTé](https://github.com/AKSW/sante/blob/master/readme.md#running-sant%C3%A9)
- [APIs](https://github.com/AKSW/sante/blob/master/readme.md#apis)
- [Known Issues & Limitations](https://github.com/AKSW/sante/blob/master/readme.md#known-issues)

### Showcases

#### FOAF Ontology: http://foaf.aksw.org
In this showcase we demonstrate SANTé with basic functionalities (search and data browser) using the foaf ontology. 

[<img src="https://github.com/AKSW/sante/raw/master/sante_simple_foaf.png" width="709" height="275" />](http://foaf.aksw.org)

#### Pokemon dataset: http://pokemon.aksw.org/
In this showcase we demonstrate SANTé with full functionalities (search, APIs, filters and data browser) using the Pokemon dataset.

[<img src="https://github.com/AKSW/sante/raw/master/sante_full_pokemon.png"/>](http://pokemon.aksw.org)

### Requirements

#### maven setup 

Change your maven ``settings.xml`` adding the following lines:

```
 <mirrors>
    <mirror>
        <id>prime-repo</id>
        <mirrorOf>external:http:*</mirrorOf>
        <url>http://repository.primefaces.org</url>
        <blocked>false</blocked>
    </mirror>
  </mirrors>
```


### SANTé in 5 minutes 

In this 5 minutes tutorial we will help you to instantiate your first knowledge base search engine over FOAF ontology using KBox [https://github.com/AKSW/KBox](https://github.com/AKSW/KBox).

1) Download KBox and instantiate the FOAF knowledge graph.

Download the jar here [https://github.com/AKSW/KBox/releases](https://github.com/AKSW/KBox/releases)
and run the following command:

```
java -jar kbox-vXXXX.jar -server -kb "http://xmlns.com/foaf/0.1,https://www.w3.org/2000/01/rdf-schema,http://www.w3.org/2002/07/owl,http://www.w3.org/1999/02/22-rdf-syntax-ns,http://purl.org/dc/elements/1.1/,http://purl.org/dc/terms/,http://purl.org/dc/dcam/,http://purl.org/dc/dcmitype/" -install
Loading Model...
Publishing service on http://localhost:8080/kbox/query
Service up and running ;-) ...
```
You can now access and query your knowledge graph at [http://localhost:8080](http://localhost:8080).
Notice that in the example above we also include RDFS, RDF and OWL ontologies, that's because we need their information for correctly instantiate FOAF ontology.
In case the SPARQL endpoint does not contain all necessary information, SANTé will not be capable of retrieving or searching for it and will display the information as ```Unavailable```.

2) Download one of the available SANTé runnable jar files.

Download the jar here [https://github.com/AKSW/sante/releases](https://github.com/AKSW/sante/releases)

3) Create the index.

Assuming that you successfully performed step 1, 
```
java -jar sante-main-*.jar index -endpoint http://localhost:8080/kbox/query -path \foaf_kg
```

4) Download one of the available smile WAR files (same version as the step 2).

Download the WAR here [https://github.com/AKSW/sante/releases](https://github.com/AKSW/sante/releases)

5) Instantiate smile
```
java -jar sante.smile-*.war
 ____    _    _   _ _____  __   __        _______ ____       _
 / ___|  / \  | \ | |_   _|/_/_  \ \      / / ____| __ )     / \   _ __  _ __
 \___ \ / _ \ |  \| | | || ____|  \ \ /\ / /|  _| |  _ \    / _ \ | '_ \| '_ \
  ___) / ___ \| |\  | | ||  _|_    \ V  V / | |___| |_) |  / ___ \| |_) | |_) |
 |____/_/   \_\_| \_| |_||_____|    \_/\_/  |_____|____/  /_/   \_\ .__/| .__/
                                                                  |_|   |_|

2022-09-13 09:58:15.842  INFO 21938 --- [           main] org.aksw.sante.SanteWebApp               : Starting SanteWebApp v2.5.3 using Java 11.0.10 on ... with PID 21938
...
2022-09-13 09:58:15.846  INFO 21938 --- [           main] org.aksw.sante.SanteWebApp               : No active profile set, falling back to default profiles: default
```

If you correctly executed all the steps above, now you should be able to access SANTé at [http://localhost:9090](http://localhost:9090).

### Creating an index
Creating an index is pretty much straightforward, you can do it via code or command line.
However, SANté uses triple stores to feed data to the index.
That's useful for performing reasoning over the target knowledge graph.
To create the index, you will first need to index your data in a triple store of your preference.

1) Download one of the availabes SANTé runnable jar file or [generate one](https://github.com/AKSW/sante#creating-sant%C3%A9-runnable-jar-file).

2) Execute the following command:
```
java -jar sante.main-*.jar index -endpoint <endpoint> -path <path>
```
where:
  
    <endpoint> stands for the sparql endpoint.
	 
    <path>     stands for the target index directory.

### Running SANTé Web App
1) Download the availabes SANTé war file or [generate one](https://github.com/AKSW/sante#creating-sant%C3%A9-smile-war-file).

3) Execute the following command:
```
java -jar sante.smile-*.war
 ____    _    _   _ _____  __   __        _______ ____       _
 / ___|  / \  | \ | |_   _|/_/_  \ \      / / ____| __ )     / \   _ __  _ __
 \___ \ / _ \ |  \| | | || ____|  \ \ /\ / /|  _| |  _ \    / _ \ | '_ \| '_ \
  ___) / ___ \| |\  | | ||  _|_    \ V  V / | |___| |_) |  / ___ \| |_) | |_) |
 |____/_/   \_\_| \_| |_||_____|    \_/\_/  |_____|____/  /_/   \_\ .__/| .__/
                                                                  |_|   |_|

2022-09-13 09:58:15.842  INFO 21938 --- [           main] org.aksw.sante.SanteWebApp               : Starting SanteWebApp v2.5.3 using Java 11.0.10 on ... with PID 21938
...
2022-09-13 09:58:15.846  INFO 21938 --- [           main] org.aksw.sante.SanteWebApp               : No active profile set, falling back to default profiles: default
```

### Creating SANTé runnable jar file 

Run ```mvn clean install``` at the ```sante.main``` base directory:
```
...\sante.main\mvn clean install
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
The runnable jar file will be generated at ```sante.main/target/sante.main-0.0.2-SNAPSHOT-jar-with-dependencies.jar```

### Creating SANTé smile WAR file 

Run ```mvn clean install``` at the ```sante.smile``` base directory:
```
...\sante.smile\mvn clean install
...
[INFO] Packaging webapp
[INFO] Assembling webapp [sante.smile] in [...\sante.smile\target\sante.smile-2.5.3]
[INFO] Processing war project
[INFO] Copying webapp resources [...\sante.smile\src\main\webapp]
[INFO] Webapp assembled in [356 msecs]
[INFO] Building war: ...\sante.smile\target\sante.smile-2.5.3.war
[INFO] WEB-INF\web.xml already added, skipping
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  5.772 s
[INFO] Finished at: 2020-05-07T23:42:36+02:00
[INFO] ------------------------------------------------------------------------
```
The smile WAR file will be generated at ```sante.smile/target/sante.smile-2.5.3.war```

### Web-Interface backend APIs

SANTé implements the following APIs (which are also used by the Web-Interface):

#### API/search

Example 1: search for all occurrences of the word ```pokemon```:

[http://pokemon.aksw.org/API/search?q=%22pokemon%22](http://pokemon.aksw.org/API/search?q=%22pokemon%22) 

#### API/suggest

Example 1: suggest using the word ```pokemon```: 

[http://pokemon.aksw.org/API/suggest?q=%22pokemon%22](http://pokemon.aksw.org/API/suggest?q=%22pokemon%22) 

#### API/dbpedialookup 

Example 1: looking for all occurrences of the word ```pokemon```: 

[http://pokemon.aksw.org/API/dbpedialookup?MaxHits=5&QueryString=pokemon](http://pokemon.aksw.org/API/dbpedialookup?MaxHits=5&QueryString=pokemon) 

#### API/reconcile 

Example 1: looking for all occurrences of the word ```pokemon```: 

[http://pokemon.aksw.org/API/reconcile?search={%20%22q%22:%20{%20%22query%22:%20%22pokemon%22%20}}](http://pokemon.aksw.org/API/reconcile?search={%20%22q%22:%20{%20%22query%22:%20%22pokemon%22%20}}) 

### Standalone Spring Boot–implemented API

SANTé also has a standalone Spring Boot API with five different endpoints.
This Spring Boot application can be run by changing into the `sante.api` directory and executing
```bash
$ mvn spring-boot:run
```
Once the application is running, by default, the documentation (Swagger) for all the applications endpoints can be accessed via
```bash
http://localhost:8080/swagger
```

### Known Issues

#### Blank Nodes

There many issues regarding RDF blank nodes such as no uniformity or standard way of tackling them.
To avoid custom implementations, we have made a design decision not to support blank nodes.
If your dataset happens to have blank nodes, you should perform a canonization using the query below (customizing it according to your needs).
This query will assign an URI with the prefix ```blanknode://``` to every blank node of your dataset.

```
DELETE {?s ?p ?o}
INSERT {?s ?p ?cO}
WHERE
{
     ?s ?p ?o.
     FILTER(isblank(?o))
     bind (iri(concat("blanknode://",SHA1(STR(iri(?o))))) as ?cO)
};

DELETE {?s ?p ?o}
INSERT {?cS ?p ?o}
WHERE
{
    ?s ?p ?o.
    FILTER(isblank(?s))
	bind (iri(concat("blanknode://",SHA1(STR(iri(?s))))) as ?cS)
};
```
