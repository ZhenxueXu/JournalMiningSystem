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
public class PaperReferencesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_number")
    private String jNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "r_title")
    private String rTitle;

    public PaperReferencesPK() {
    }

    public PaperReferencesPK(String jNumber, String rTitle) {
        this.jNumber = jNumber;
        this.rTitle = rTitle;
    }

    public String getJNumber() {
        return jNumber;
    }

    public void setJNumber(String jNumber) {
        this.jNumber = jNumber;
    }

    public String getRTitle() {
        return rTitle;
    }

    public void setRTitle(String rTitle) {
        this.rTitle = rTitle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jNumber != null ? jNumber.hashCode() : 0);
        hash += (rTitle != null ? rTitle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaperReferencesPK)) {
            return false;
        }
        PaperReferencesPK other = (PaperReferencesPK) object;
        if ((this.jNumber == null && other.jNumber != null) || (this.jNumber != null && !this.jNumber.equals(other.jNumber))) {
            return false;
        }
        if ((this.rTitle == null && other.rTitle != null) || (this.rTitle != null && !this.rTitle.equals(other.rTitle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaperReferencesPK[ jNumber=" + jNumber + ", rTitle=" + rTitle + " ]";
    }
    
}
