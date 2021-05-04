/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.solrdocstore.connector.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HoldingsItems {
    /* The item to which these holdings items are related */
    private String bibliographicRecordId;

    /* The agency that owns the holdings items */
    private Integer agencyId;

    private List<IndexKeys> indexKeys;

    private String trackingId;

    public String getBibliographicRecordId() {
        return bibliographicRecordId;
    }

    public void setBibliographicRecordId(String bibliographicRecordId) {
        this.bibliographicRecordId = bibliographicRecordId;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

    public List<IndexKeys> getIndexKeys() {
        return indexKeys;
    }

    public void setIndexKeys(List<IndexKeys> indexKeys) {
        this.indexKeys = indexKeys;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HoldingsItems that = (HoldingsItems) o;

        if (bibliographicRecordId != null ? !bibliographicRecordId.equals(that.bibliographicRecordId) : that.bibliographicRecordId != null) {
            return false;
        }
        if (agencyId != null ? !agencyId.equals(that.agencyId) : that.agencyId != null) {
            return false;
        }
        if (indexKeys != null ? !indexKeys.equals(that.indexKeys) : that.indexKeys != null) {
            return false;
        }
        return trackingId != null ? trackingId.equals(that.trackingId) : that.trackingId == null;
    }

    @Override
    public int hashCode() {
        int result = bibliographicRecordId != null ? bibliographicRecordId.hashCode() : 0;
        result = 31 * result + (agencyId != null ? agencyId.hashCode() : 0);
        result = 31 * result + (indexKeys != null ? indexKeys.hashCode() : 0);
        result = 31 * result + (trackingId != null ? trackingId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HoldingsItems{" +
                "bibliographicRecordId='" + bibliographicRecordId + '\'' +
                ", agencyId=" + agencyId +
                ", indexKeys=" + indexKeys +
                ", trackingId='" + trackingId + '\'' +
                '}';
    }
}
