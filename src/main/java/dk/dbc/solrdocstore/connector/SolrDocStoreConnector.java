/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.solrdocstore.connector;

import dk.dbc.httpclient.FailSafeHttpClient;
import dk.dbc.httpclient.HttpGet;
import dk.dbc.httpclient.HttpPost;
import dk.dbc.invariant.InvariantUtil;
import dk.dbc.solrdocstore.connector.model.ExistenceResponse;
import dk.dbc.solrdocstore.connector.model.HoldingsItems;
import dk.dbc.solrdocstore.connector.model.Status;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Response;
import net.jodah.failsafe.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class SolrDocStoreConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrDocStoreConnector.class);

    private static final RetryPolicy<Response> RETRY_POLICY = new RetryPolicy<Response>()
            .handle(ProcessingException.class)
            .handleResultIf(response ->
                    response.getStatus() == 404
                            || response.getStatus() == 500
                            || response.getStatus() == 502)
            .withDelay(Duration.ofSeconds(5))
            .withMaxRetries(3);
    private final FailSafeHttpClient failSafeHttpClient;
    private final String baseUrl;

    public SolrDocStoreConnector(Client httpClient, String baseUrl) {
        this(FailSafeHttpClient.create(httpClient, RETRY_POLICY), baseUrl);
    }

    /**
     * Returns new instance with custom retry policy
     *
     * @param failSafeHttpClient web resources client with custom retry policy
     * @param baseUrl            base URL for record service endpoint
     */
    public SolrDocStoreConnector(FailSafeHttpClient failSafeHttpClient, String baseUrl) {
        this.failSafeHttpClient = InvariantUtil.checkNotNullOrThrow(
                failSafeHttpClient, "failSafeHttpClient");
        this.baseUrl = InvariantUtil.checkNotNullNotEmptyOrThrow(
                baseUrl, "baseUrl");
    }

    /**
     * Sets holdings items and connects them to a bibliographic item, if possible
     * @param holdingsItems {@link HoldingsItems}
     * @return {@link Status}
     * @throws SolrDocStoreConnectorException on failure to complete operation
     * @throws SolrDocStoreConnectorUnexpectedStatusCodeException when response status code differs from 200 OK
     */
    public Status setHoldings(HoldingsItems holdingsItems) throws SolrDocStoreConnectorException {
        try {
            final Response response = new HttpPost(failSafeHttpClient)
                    .withBaseUrl(baseUrl)
                    .withPathElements("api", "holdings")
                    .withJsonData(holdingsItems)
                    .execute();
            verifyResponseStatus("setHoldings", response, Response.Status.OK, Status.class);
            final Status status = readResponseEntity(response, Status.class);
            if (status == null) {
                LOGGER.warn("setHoldings operation returned null valued status");
            }
            return status;
        } catch (ProcessingException e) {
            throw new SolrDocStoreConnectorException("Failed to complete setHoldings operation", e);
        }
    }

    /**
     * Tests if an agency has a 'live' holding for a bibliographic item
     * @param agencyId agency ID
     * @param bibliographicRecordId ID of bibliographic record
     * @return true if a holding exists, otherwise false
     * @throws SolrDocStoreConnectorException on failure to complete operation
     * @throws SolrDocStoreConnectorUnexpectedStatusCodeException when response status code differs from 200 OK
     */
    public boolean holdingExists(int agencyId, String bibliographicRecordId) throws SolrDocStoreConnectorException {
        try {
            final Response response = new HttpGet(failSafeHttpClient)
                    .withBaseUrl(baseUrl)
                    .withPathElements("api", "exists", "holdingsitem", String.format("%d:%s", agencyId, bibliographicRecordId))
                    .execute();
            verifyResponseStatus("holdingsExists", response, Response.Status.OK, ExistenceResponse.class);
            final ExistenceResponse existenceResponse = readResponseEntity(response, ExistenceResponse.class);
            if (existenceResponse == null) {
                throw new SolrDocStoreConnectorException("holdingExists operation returned null valued response");
            }
            return existenceResponse.exists();
        } catch (ProcessingException e) {
            throw new SolrDocStoreConnectorException("Failed to complete holdingExists operation", e);
        }
    }

    private <T> void verifyResponseStatus(String operationId, Response response, Response.Status expectedStatus, Class<T> entityClass)
            throws SolrDocStoreConnectorUnexpectedStatusCodeException {
        final Response.Status actualStatus = Response.Status.fromStatusCode(response.getStatus());
        if (actualStatus != expectedStatus) {
            final SolrDocStoreConnectorUnexpectedStatusCodeException exception =
                    new SolrDocStoreConnectorUnexpectedStatusCodeException(
                            String.format("solr-doc-store service %s operation returned with unexpected status code: %s",
                                    operationId, actualStatus),
                            actualStatus.getStatusCode());

            if (response.hasEntity()) {
                if (entityClass == null) {
                    LOGGER.error("solr-doc-store service %s operation returned status which was not read by client");
                    // force read to avoid http client resource leakage
                    readResponseEntity(response, Object.class);
                }
                final T t = readResponseEntity(response, entityClass);
                if (t instanceof Status) {
                    exception.setStatus((Status) t);
                }
            }
            throw exception;
        }
    }

    private <T> T readResponseEntity(Response response, Class<T> tClass) {
        return response.readEntity(tClass);
    }

    public void close() {
        failSafeHttpClient.getClient().close();
    }
}
