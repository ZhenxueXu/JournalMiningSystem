/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Think
 */
@Entity
@Table(name = "references_author")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReferencesAuthor.findAll", query = "SELECT r FROM ReferencesAuthor r")
    , @NamedQuery(name = "ReferencesAuthor.findByRTitle", query = "SELECT r FROM ReferencesAuthor r WHERE r.referencesAuthorPK.rTitle = :rTitle")
    , @NamedQuery(name = "ReferencesAuthor.findByRAuthor", query = "SELECT r FROM ReferencesAuthor r WHERE r.referencesAuthorPK.rAuthor = :rAuthor")})
public class ReferencesAuthor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReferencesAuthorPK referencesAuthorPK;

    public ReferencesAuthor() {
    }

    public ReferencesAuthor(ReferencesAuthorPK referencesAuthorPK) {
        this.referencesAuthorPK = referencesAuthorPK;
    }

    public ReferencesAuthor(String rTitle, String rAuthor) {
        this.referencesAuthorPK = new ReferencesAuthorPK(rTitle, rAuthor);
    }

    public ReferencesAuthorPK getReferencesAuthorPK() {
        return referencesAuthorPK;
    }

    public void setReferencesAuthorPK(ReferencesAuthorPK referencesAuthorPK) {
        this.referencesAuthorPK = referencesAuthorPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (referencesAuthorPK != null ? referencesAuthorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReferencesAuthor)) {
            return false;
        }
        ReferencesAuthor other = (ReferencesAuthor) object;
        if ((this.referencesAuthorPK == null && other.referencesAuthorPK != null) || (this.referencesAuthorPK != null && !this.referencesAuthorPK.equals(other.referencesAuthorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ReferencesAuthor[ referencesAuthorPK=" + referencesAuthorPK + " ]";
    }
    
}
