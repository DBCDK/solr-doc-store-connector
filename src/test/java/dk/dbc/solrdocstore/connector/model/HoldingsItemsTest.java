/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.solrdocstore.connector.model;

import dk.dbc.commons.jsonb.JSONBContext;
import dk.dbc.commons.jsonb.JSONBException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class HoldingsItemsTest {
    private final JSONBContext jsonbContext = new JSONBContext();

    @Test
    void marshalling() throws JSONBException {
        final IndexKeys indexKeys = new IndexKeys();
        indexKeys.put("k1", Collections.singletonList("v1"));
        indexKeys.put("k2", Arrays.asList("v2a", "v2b"));
        final List<IndexKeys> indexKeysList = new ArrayList<>();
        indexKeysList.add(indexKeys);

        final HoldingsItems holdingsItems = new HoldingsItems();
        holdingsItems.setAgencyId(12345678);
        holdingsItems.setBibliographicRecordId("id1");
        holdingsItems.setIndexKeys(indexKeysList);

        assertThat(jsonbContext.marshall(holdingsItems),
                is("{\"bibliographicRecordId\":\"id1\",\"agencyId\":12345678,\"indexKeys\":[{\"k1\":[\"v1\"],\"k2\":[\"v2a\",\"v2b\"]}]}"));
    }
    
    @Test
    void unmarshalling() throws JSONBException {
        final IndexKeys indexKeys = new IndexKeys();
        indexKeys.put("k1", Collections.singletonList("v1"));
        indexKeys.put("k2", Arrays.asList("v2a", "v2b"));
        final List<IndexKeys> indexKeysList = new ArrayList<>();
        indexKeysList.add(indexKeys);

        final HoldingsItems expectedHoldingsItems = new HoldingsItems();
        expectedHoldingsItems.setAgencyId(12345678);
        expectedHoldingsItems.setBibliographicRecordId("id1");
        expectedHoldingsItems.setIndexKeys(indexKeysList);

        assertThat(jsonbContext.unmarshall("{\"bibliographicRecordId\":\"id1\",\"agencyId\":12345678,\"indexKeys\":[{\"k1\":[\"v1\"],\"k2\":[\"v2a\",\"v2b\"]}]}", HoldingsItems.class),
                is(expectedHoldingsItems));
    }
}