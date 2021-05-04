solr-doc-store-connector
=============

A Java library providing a client to the solr-doc-store service

### usage

Add the dependency to your Maven pom.xml

```xml
<dependency>
  <groupId>dk.dbc</groupId>
  <artifactId>solr-doc-store-connector</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
In your Java code

```java
import dk.dbc.solrdocstore.connector.SolrDocStoreConnector;
import javax.inject.Inject;
...

// Assumes environment variable SOLR_DOC_STORE_SERVICE_URL
// is set to the base URL of the solr-doc-store api.
@Inject
SolrDocStoreConnector connector;

final IndexKeys indexKeys = new IndexKeys();
indexKeys.put("k1", Collections.singletonList("v1"));
indexKeys.put("k2", Arrays.asList("v2a", "v2b"));
final List<IndexKeys> indexKeysList = new ArrayList<>();
indexKeysList.add(indexKeys);

final HoldingsItems holdingsItems = new HoldingsItems();
holdingsItems.setAgencyId(12345678);
holdingsItems.setBibliographicRecordId("id1");
holdingsItems.setIndexKeys(indexKeysList);

connector.setHoldings(holdingsItems);
```

### License

Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3.
See license text in LICENSE.txt