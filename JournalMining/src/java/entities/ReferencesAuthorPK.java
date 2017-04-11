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
public class ReferencesAuthorPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "r_title")
    private String rTitle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "r_author")
    private String rAuthor;

    public ReferencesAuthorPK() {
    }

    public ReferencesAuthorPK(String rTitle, String rAuthor) {
        this.rTitle = rTitle;
        this.rAuthor = rAuthor;
    }

    public String getRTitle() {
        return rTitle;
    }

    public void setRTitle(String rTitle) {
        this.rTitle = rTitle;
    }

    public String getRAuthor() {
        return rAuthor;
    }

    public void setRAuthor(String rAuthor) {
        this.rAuthor = rAuthor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rTitle != null ? rTitle.hashCode() : 0);
        hash += (rAuthor != null ? rAuthor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReferencesAuthorPK)) {
            return false;
        }
        ReferencesAuthorPK other = (ReferencesAuthorPK) object;
        if ((this.rTitle == null && other.rTitle != null) || (this.rTitle != null && !this.rTitle.equals(other.rTitle))) {
            return false;
        }
        if ((this.rAuthor == null && other.rAuthor != null) || (this.rAuthor != null && !this.rAuthor.equals(other.rAuthor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ReferencesAuthorPK[ rTitle=" + rTitle + ", rAuthor=" + rAuthor + " ]";
    }
    
}
