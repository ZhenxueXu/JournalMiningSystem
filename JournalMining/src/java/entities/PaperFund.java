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
@Table(name = "paper_fund")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaperFund.findAll", query = "SELECT p FROM PaperFund p")
    , @NamedQuery(name = "PaperFund.findByJNumber", query = "SELECT p FROM PaperFund p WHERE p.paperFundPK.jNumber = :jNumber")
    , @NamedQuery(name = "PaperFund.findByJFund", query = "SELECT p FROM PaperFund p WHERE p.paperFundPK.jFund = :jFund")})
public class PaperFund implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PaperFundPK paperFundPK;

    public PaperFund() {
    }

    public PaperFund(PaperFundPK paperFundPK) {
        this.paperFundPK = paperFundPK;
    }

    public PaperFund(String jNumber, String jFund) {
        this.paperFundPK = new PaperFundPK(jNumber, jFund);
    }

    public PaperFundPK getPaperFundPK() {
        return paperFundPK;
    }

    public void setPaperFundPK(PaperFundPK paperFundPK) {
        this.paperFundPK = paperFundPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paperFundPK != null ? paperFundPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaperFund)) {
            return false;
        }
        PaperFund other = (PaperFund) object;
        if ((this.paperFundPK == null && other.paperFundPK != null) || (this.paperFundPK != null && !this.paperFundPK.equals(other.paperFundPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaperFund[ paperFundPK=" + paperFundPK + " ]";
    }
    
}
