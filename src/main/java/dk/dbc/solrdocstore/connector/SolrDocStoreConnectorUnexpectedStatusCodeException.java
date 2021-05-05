/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.solrdocstore.connector;

import dk.dbc.solrdocstore.connector.model.Status;

public class SolrDocStoreConnectorUnexpectedStatusCodeException extends SolrDocStoreConnectorException {
    private final int statusCode;
    private Status status;

    public SolrDocStoreConnectorUnexpectedStatusCodeException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
