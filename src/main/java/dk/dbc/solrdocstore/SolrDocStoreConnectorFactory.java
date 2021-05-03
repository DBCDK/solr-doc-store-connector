/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.solrdocstore;

import dk.dbc.httpclient.HttpClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.client.Client;

/**
 * SolrDocStoreConnector factory
 * <p>
 * Synopsis:
 * </p>
 * <pre>
 *    // New instance
 *    SolrDocStoreConnector connector = SolrDocStoreConnectorFactory.create("http://solr-doc-store");
 *
 *    // Singleton instance in CDI enabled environment
 *    {@literal @}Inject
 *    SolrDocStoreConnectorFactory factory;
 *    ...
 *    SolrDocStoreConnector connector = factory.getInstance();
 *
 *    // or simply
 *    {@literal @}Inject
 *    SolrDocStoreConnector connector;
 * </pre>
 * <p>
 * The CDI case depends on the solr-doc-store service base-url being defined as
 * the value of either a system property or environment variable
 * named SOLR_DOC_STORE_SERVICE_URL.
 * </p>
 */
@ApplicationScoped
public class SolrDocStoreConnectorFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrDocStoreConnectorFactory.class);

    public static SolrDocStoreConnector create(String baseUrl) throws SolrDocStoreConnectorException {
        final Client client = HttpClient.newClient(new ClientConfig()
                .register(new JacksonFeature()));
        LOGGER.info("Creating SolrDocStoreConnector for: {}", baseUrl);
        return new SolrDocStoreConnector(client, baseUrl);
    }

    @Inject
    @ConfigProperty(name = "SOLR_DOC_STORE_SERVICE_URL")
    private String baseUrl;

    SolrDocStoreConnector connector;

    @PostConstruct
    public void initializeConnector() {
        try {
            connector = SolrDocStoreConnectorFactory.create(baseUrl);
        } catch (SolrDocStoreConnectorException e) {
            throw new IllegalStateException(e);
        }
    }

    @Produces
    public SolrDocStoreConnector getInstance() {
        return connector;
    }

    @PreDestroy
    public void tearDownConnector() {
        connector.close();
    }
}
