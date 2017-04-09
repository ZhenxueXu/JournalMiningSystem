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
@Table(name = "paper_keywords")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaperKeywords.findAll", query = "SELECT p FROM PaperKeywords p")
    , @NamedQuery(name = "PaperKeywords.findByJNumber", query = "SELECT p FROM PaperKeywords p WHERE p.paperKeywordsPK.jNumber = :jNumber")
    , @NamedQuery(name = "PaperKeywords.findByKeyword", query = "SELECT p FROM PaperKeywords p WHERE p.paperKeywordsPK.keyword = :keyword")})
public class PaperKeywords implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PaperKeywordsPK paperKeywordsPK;

    public PaperKeywords() {
    }

    public PaperKeywords(PaperKeywordsPK paperKeywordsPK) {
        this.paperKeywordsPK = paperKeywordsPK;
    }

    public PaperKeywords(String jNumber, String keyword) {
        this.paperKeywordsPK = new PaperKeywordsPK(jNumber, keyword);
    }

    public PaperKeywordsPK getPaperKeywordsPK() {
        return paperKeywordsPK;
    }

    public void setPaperKeywordsPK(PaperKeywordsPK paperKeywordsPK) {
        this.paperKeywordsPK = paperKeywordsPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paperKeywordsPK != null ? paperKeywordsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaperKeywords)) {
            return false;
        }
        PaperKeywords other = (PaperKeywords) object;
        if ((this.paperKeywordsPK == null && other.paperKeywordsPK != null) || (this.paperKeywordsPK != null && !this.paperKeywordsPK.equals(other.paperKeywordsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaperKeywords[ paperKeywordsPK=" + paperKeywordsPK + " ]";
    }
    
}
