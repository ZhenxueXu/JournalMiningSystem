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
@Table(name = "paper_address")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaperAddress.findAll", query = "SELECT p FROM PaperAddress p")
    , @NamedQuery(name = "PaperAddress.findByJNumber", query = "SELECT p FROM PaperAddress p WHERE p.paperAddressPK.jNumber = :jNumber")
    , @NamedQuery(name = "PaperAddress.findByJAddress", query = "SELECT p FROM PaperAddress p WHERE p.paperAddressPK.jAddress = :jAddress")})
public class PaperAddress implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PaperAddressPK paperAddressPK;

    public PaperAddress() {
    }

    public PaperAddress(PaperAddressPK paperAddressPK) {
        this.paperAddressPK = paperAddressPK;
    }

    public PaperAddress(String jNumber, String jAddress) {
        this.paperAddressPK = new PaperAddressPK(jNumber, jAddress);
    }

    public PaperAddressPK getPaperAddressPK() {
        return paperAddressPK;
    }

    public void setPaperAddressPK(PaperAddressPK paperAddressPK) {
        this.paperAddressPK = paperAddressPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paperAddressPK != null ? paperAddressPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaperAddress)) {
            return false;
        }
        PaperAddress other = (PaperAddress) object;
        if ((this.paperAddressPK == null && other.paperAddressPK != null) || (this.paperAddressPK != null && !this.paperAddressPK.equals(other.paperAddressPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaperAddress[ paperAddressPK=" + paperAddressPK + " ]";
    }
    
}
