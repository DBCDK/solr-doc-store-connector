/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.solrdocstore.connector;

import com.github.tomakehurst.wiremock.WireMockServer;
import dk.dbc.commons.jsonb.JSONBContext;
import dk.dbc.commons.jsonb.JSONBException;
import dk.dbc.httpclient.FailSafeHttpClient;
import dk.dbc.httpclient.HttpClient;
import dk.dbc.solrdocstore.connector.model.HoldingsItems;
import dk.dbc.solrdocstore.connector.model.Status;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Response;
import net.jodah.failsafe.RetryPolicy;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class SolrDocStoreConnectorTest {
    private static WireMockServer wireMockServer;
    private static String wireMockHost;

    static final RetryPolicy<Response> NO_RETRY_POLICY = new RetryPolicy<>();
    static SolrDocStoreConnector solrDocStoreConnector;

    static JSONBContext jsonbContext = new JSONBContext();

    @BeforeAll
    static void startWireMockServer() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        wireMockHost = "http://localhost:" + wireMockServer.port();
        configureFor("localhost", wireMockServer.port());
    }

    @BeforeAll
    static void setupConnector() {
        solrDocStoreConnector = createConnector(wireMockHost, NO_RETRY_POLICY);
    }

    static SolrDocStoreConnector createConnector(String baseurl, RetryPolicy<Response> retryPolicy) {
        final Client client = HttpClient.newClient(new ClientConfig()
                .register(new JacksonFeature()));
        final FailSafeHttpClient failSafeHttpClient = FailSafeHttpClient.create(client, retryPolicy);
        return new SolrDocStoreConnector(failSafeHttpClient, baseurl);
    }

    @AfterAll
    static void stopWireMockServer() {
        wireMockServer.stop();
    }

    @Test
    void setHoldings_ok() throws SolrDocStoreConnectorException {
        final HoldingsItems holdingsItems = jsonFileToObject(
                Paths.get("src/test/resources/setHoldings_ok.json"), HoldingsItems.class);

        final Status expectedStatus = new Status();
        expectedStatus.setOk(true);
        expectedStatus.setText("Success");

        final Status status = solrDocStoreConnector.setHoldings(holdingsItems);
        assertThat(status, is(expectedStatus));
    }

    @Test
    void setHoldings_fail() throws SolrDocStoreConnectorException {
        final HoldingsItems holdingsItems = jsonFileToObject(
                Paths.get("src/test/resources/setHoldings_fail.json"), HoldingsItems.class);

        try {
            solrDocStoreConnector.setHoldings(holdingsItems);
            fail("no exception thrown");
        } catch (SolrDocStoreConnectorUnexpectedStatusCodeException e) {
            assertThat("HTTP response code", e.getStatusCode(), is(500));
            assertThat("service status", e.getStatus(), is(notNullValue()));
        }
    }

    @Test
    void holdingExists_true() throws SolrDocStoreConnectorException {
        assertThat(solrDocStoreConnector.holdingExists(800010, "99123304158205763__1"),
                is(true));
    }

    @Test
    void holdingExists_false() throws SolrDocStoreConnectorException {
        assertThat(solrDocStoreConnector.holdingExists(800010, "99123304158205763__2"),
                is(false));
    }

    private static <T> T jsonFileToObject(Path filename, Class<T> tClass) {
        try {
            return jsonbContext.unmarshall(new String(Files.readAllBytes(filename), StandardCharsets.UTF_8), tClass);
        } catch (IOException | JSONBException e) {
            throw new IllegalStateException(e);
        }
    }
}
