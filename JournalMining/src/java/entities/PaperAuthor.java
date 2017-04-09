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
@Table(name = "paper_author")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaperAuthor.findAll", query = "SELECT p FROM PaperAuthor p")
    , @NamedQuery(name = "PaperAuthor.findByJNumber", query = "SELECT p FROM PaperAuthor p WHERE p.paperAuthorPK.jNumber = :jNumber")
    , @NamedQuery(name = "PaperAuthor.findByJAuthor", query = "SELECT p FROM PaperAuthor p WHERE p.paperAuthorPK.jAuthor = :jAuthor")})
public class PaperAuthor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PaperAuthorPK paperAuthorPK;

    public PaperAuthor() {
    }

    public PaperAuthor(PaperAuthorPK paperAuthorPK) {
        this.paperAuthorPK = paperAuthorPK;
    }

    public PaperAuthor(String jNumber, String jAuthor) {
        this.paperAuthorPK = new PaperAuthorPK(jNumber, jAuthor);
    }

    public PaperAuthorPK getPaperAuthorPK() {
        return paperAuthorPK;
    }

    public void setPaperAuthorPK(PaperAuthorPK paperAuthorPK) {
        this.paperAuthorPK = paperAuthorPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paperAuthorPK != null ? paperAuthorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaperAuthor)) {
            return false;
        }
        PaperAuthor other = (PaperAuthor) object;
        if ((this.paperAuthorPK == null && other.paperAuthorPK != null) || (this.paperAuthorPK != null && !this.paperAuthorPK.equals(other.paperAuthorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaperAuthor[ paperAuthorPK=" + paperAuthorPK + " ]";
    }
    
}
