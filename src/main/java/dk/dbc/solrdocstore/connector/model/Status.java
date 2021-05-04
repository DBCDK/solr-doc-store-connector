/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.solrdocstore.connector.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Status {
   /* true if the request was a success */
   private Boolean ok;

   /* true if the lack of success was intermittent */
   private Boolean intermittent;

   /* descriptive message of the status */
   private String text;

   public Boolean getOk() {
      return ok;
   }

   public void setOk(Boolean ok) {
      this.ok = ok;
   }

   public Boolean getIntermittent() {
      return intermittent;
   }

   public void setIntermittent(Boolean intermittent) {
      this.intermittent = intermittent;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      Status status = (Status) o;

      if (ok != null ? !ok.equals(status.ok) : status.ok != null) {
         return false;
      }
      if (intermittent != null ? !intermittent.equals(status.intermittent) : status.intermittent != null) {
         return false;
      }
      return text != null ? text.equals(status.text) : status.text == null;
   }

   @Override
   public int hashCode() {
      int result = ok != null ? ok.hashCode() : 0;
      result = 31 * result + (intermittent != null ? intermittent.hashCode() : 0);
      result = 31 * result + (text != null ? text.hashCode() : 0);
      return result;
   }

   @Override
   public String toString() {
      return "Status{" +
              "ok=" + ok +
              ", intermittent=" + intermittent +
              ", text='" + text + '\'' +
              '}';
   }
}
