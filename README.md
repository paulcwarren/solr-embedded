# solr-embedded
An embedded Apache Solr instance and @EnableEmbeddedSolr Spring configuration annotation  

Usage
-----
```
@Configuration
@EnableSolrEmbedded
public class SolrConfig {}	
```

This will start a embedded Apacahe Solr instance inside your application available at `http://localhost:8080/solr/solr`.

Published to maven central using:-
```
mvn clean deploy -Dgpg.passphrase=<passphrase>
```
