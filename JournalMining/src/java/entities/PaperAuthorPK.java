/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Think
 */
@Embeddable
public class PaperAuthorPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_number")
    private String jNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_author")
    private String jAuthor;

    public PaperAuthorPK() {
    }

    public PaperAuthorPK(String jNumber, String jAuthor) {
        this.jNumber = jNumber;
        this.jAuthor = jAuthor;
    }

    public String getJNumber() {
        return jNumber;
    }

    public void setJNumber(String jNumber) {
        this.jNumber = jNumber;
    }

    public String getJAuthor() {
        return jAuthor;
    }

    public void setJAuthor(String jAuthor) {
        this.jAuthor = jAuthor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jNumber != null ? jNumber.hashCode() : 0);
        hash += (jAuthor != null ? jAuthor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaperAuthorPK)) {
            return false;
        }
        PaperAuthorPK other = (PaperAuthorPK) object;
        if ((this.jNumber == null && other.jNumber != null) || (this.jNumber != null && !this.jNumber.equals(other.jNumber))) {
            return false;
        }
        if ((this.jAuthor == null && other.jAuthor != null) || (this.jAuthor != null && !this.jAuthor.equals(other.jAuthor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaperAuthorPK[ jNumber=" + jNumber + ", jAuthor=" + jAuthor + " ]";
    }
    
}
