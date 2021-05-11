/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.solrdocstore.connector.model;

public class ExistenceResponse {
    private boolean exists;

    public boolean exists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExistenceResponse that = (ExistenceResponse) o;

        return exists == that.exists;
    }

    @Override
    public int hashCode() {
        return exists ? 1 : 0;
    }

    @Override
    public String toString() {
        return "ExistenceResponse{" +
                "exists=" + exists +
                '}';
    }
}
