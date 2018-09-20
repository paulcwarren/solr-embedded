# solr-embedded
An embedded Apache Solr instance for Spring Boot application.

Usage
-----
Add the following Maven dependencies to your `pom.xml`:
```
<dependency>
  <groupId>com.github.paulcwarren</groupId>
  <artifactId>solr-embedded</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Spring Boot (2.0.5.RELEASE) currently sets the version of solr dependencies to 6.6.5 so it is necessry to override those dependencies in your pom with the following dependency management section:
```
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.apache.solr</groupId>
      <artifactId>solr-core</artifactId>
      <version>7.2.1</version>
    </dependency>
    <dependency>
      <groupId>org.apache.solr</groupId>
      <artifactId>solr-cell</artifactId>
      <version>7.2.1</version>
    </dependency>
  </dependencies>
</dependencyManagement>
```

Add the following Java configuration to your application:
```
@Configuration
@EnableSolrEmbedded
public class SolrConfig {}	
```

This will start an embedded Apache Solr instance available at `http://localhost:8080/solr/solr`.

Published to maven central using:-
```
mvn clean deploy -Dgpg.passphrase=<passphrase>
```
