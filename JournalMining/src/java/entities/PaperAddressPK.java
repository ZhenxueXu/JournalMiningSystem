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
public class PaperAddressPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_number")
    private String jNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_address")
    private String jAddress;

    public PaperAddressPK() {
    }

    public PaperAddressPK(String jNumber, String jAddress) {
        this.jNumber = jNumber;
        this.jAddress = jAddress;
    }

    public String getJNumber() {
        return jNumber;
    }

    public void setJNumber(String jNumber) {
        this.jNumber = jNumber;
    }

    public String getJAddress() {
        return jAddress;
    }

    public void setJAddress(String jAddress) {
        this.jAddress = jAddress;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jNumber != null ? jNumber.hashCode() : 0);
        hash += (jAddress != null ? jAddress.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaperAddressPK)) {
            return false;
        }
        PaperAddressPK other = (PaperAddressPK) object;
        if ((this.jNumber == null && other.jNumber != null) || (this.jNumber != null && !this.jNumber.equals(other.jNumber))) {
            return false;
        }
        if ((this.jAddress == null && other.jAddress != null) || (this.jAddress != null && !this.jAddress.equals(other.jAddress))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaperAddressPK[ jNumber=" + jNumber + ", jAddress=" + jAddress + " ]";
    }
    
}
