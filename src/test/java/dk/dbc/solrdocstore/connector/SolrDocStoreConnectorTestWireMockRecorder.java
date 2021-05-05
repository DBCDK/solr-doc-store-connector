/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.solrdocstore.connector;

public class SolrDocStoreConnectorTestWireMockRecorder {
    /*
        Steps to reproduce wiremock recording:

        * Start standalone runner
            java -jar wiremock-standalone-{WIRE_MOCK_VERSION}.jar --proxy-all="{SOLR_DOC_STORE_SERVICE_URL}" --port 8080 --record-mappings --verbose

        * Run the main method of this class

        * Replace content of src/test/resources/{__files|mappings} with that produced by the standalone runner
     */

    public static void main(String[] args) throws SolrDocStoreConnectorException {
        SolrDocStoreConnectorTest.solrDocStoreConnector = SolrDocStoreConnectorTest.createConnector(
                "http://localhost:8080", SolrDocStoreConnectorTest.NO_RETRY_POLICY);
        final SolrDocStoreConnectorTest solrDocStoreConnectorTest = new SolrDocStoreConnectorTest();
        recordRequestsForSetHoldings(solrDocStoreConnectorTest);
    }

    private static void recordRequestsForSetHoldings(SolrDocStoreConnectorTest solrDocStoreConnectorTest)
            throws SolrDocStoreConnectorException {
        solrDocStoreConnectorTest.setHoldings_ok();
        solrDocStoreConnectorTest.setHoldings_fail();
    }
}
