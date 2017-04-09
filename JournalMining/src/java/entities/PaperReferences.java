/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Think
 */
@Entity
@Table(name = "paper_references")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaperReferences.findAll", query = "SELECT p FROM PaperReferences p")
    , @NamedQuery(name = "PaperReferences.findByJNumber", query = "SELECT p FROM PaperReferences p WHERE p.paperReferencesPK.jNumber = :jNumber")
    , @NamedQuery(name = "PaperReferences.findByRTitle", query = "SELECT p FROM PaperReferences p WHERE p.paperReferencesPK.rTitle = :rTitle")
    , @NamedQuery(name = "PaperReferences.findByRJournal", query = "SELECT p FROM PaperReferences p WHERE p.rJournal = :rJournal")
    , @NamedQuery(name = "PaperReferences.findByRYear", query = "SELECT p FROM PaperReferences p WHERE p.rYear = :rYear")
    , @NamedQuery(name = "PaperReferences.findByRPages", query = "SELECT p FROM PaperReferences p WHERE p.rPages = :rPages")
    , @NamedQuery(name = "PaperReferences.findByRType", query = "SELECT p FROM PaperReferences p WHERE p.rType = :rType")})
public class PaperReferences implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PaperReferencesPK paperReferencesPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "r_journal")
    private String rJournal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "r_year")
    private String rYear;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "r_pages")
    private String rPages;
    @Basic(optional = false)
    @NotNull
    @Column(name = "r_type")
    private int rType;

    public PaperReferences() {
    }

    public PaperReferences(PaperReferencesPK paperReferencesPK) {
        this.paperReferencesPK = paperReferencesPK;
    }

    public PaperReferences(PaperReferencesPK paperReferencesPK, String rJournal, String rYear, String rPages, int rType) {
        this.paperReferencesPK = paperReferencesPK;
        this.rJournal = rJournal;
        this.rYear = rYear;
        this.rPages = rPages;
        this.rType = rType;
    }

    public PaperReferences(String jNumber, String rTitle) {
        this.paperReferencesPK = new PaperReferencesPK(jNumber, rTitle);
    }

    public PaperReferencesPK getPaperReferencesPK() {
        return paperReferencesPK;
    }

    public void setPaperReferencesPK(PaperReferencesPK paperReferencesPK) {
        this.paperReferencesPK = paperReferencesPK;
    }

    public String getRJournal() {
        return rJournal;
    }

    public void setRJournal(String rJournal) {
        this.rJournal = rJournal;
    }

    public String getRYear() {
        return rYear;
    }

    public void setRYear(String rYear) {
        this.rYear = rYear;
    }

    public String getRPages() {
        return rPages;
    }

    public void setRPages(String rPages) {
        this.rPages = rPages;
    }

    public int getRType() {
        return rType;
    }

    public void setRType(int rType) {
        this.rType = rType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paperReferencesPK != null ? paperReferencesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaperReferences)) {
            return false;
        }
        PaperReferences other = (PaperReferences) object;
        if ((this.paperReferencesPK == null && other.paperReferencesPK != null) || (this.paperReferencesPK != null && !this.paperReferencesPK.equals(other.paperReferencesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaperReferences[ paperReferencesPK=" + paperReferencesPK + " ]";
    }
    
}
