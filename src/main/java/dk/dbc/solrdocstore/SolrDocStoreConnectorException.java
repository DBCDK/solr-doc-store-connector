/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.solrdocstore;

public class SolrDocStoreConnectorException extends Exception {
    public SolrDocStoreConnectorException(String message) {
        super(message);
    }

    public SolrDocStoreConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
