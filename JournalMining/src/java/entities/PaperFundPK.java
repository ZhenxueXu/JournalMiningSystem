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
public class PaperFundPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_number")
    private String jNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_fund")
    private String jFund;

    public PaperFundPK() {
    }

    public PaperFundPK(String jNumber, String jFund) {
        this.jNumber = jNumber;
        this.jFund = jFund;
    }

    public String getJNumber() {
        return jNumber;
    }

    public void setJNumber(String jNumber) {
        this.jNumber = jNumber;
    }

    public String getJFund() {
        return jFund;
    }

    public void setJFund(String jFund) {
        this.jFund = jFund;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jNumber != null ? jNumber.hashCode() : 0);
        hash += (jFund != null ? jFund.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaperFundPK)) {
            return false;
        }
        PaperFundPK other = (PaperFundPK) object;
        if ((this.jNumber == null && other.jNumber != null) || (this.jNumber != null && !this.jNumber.equals(other.jNumber))) {
            return false;
        }
        if ((this.jFund == null && other.jFund != null) || (this.jFund != null && !this.jFund.equals(other.jFund))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaperFundPK[ jNumber=" + jNumber + ", jFund=" + jFund + " ]";
    }
    
}
