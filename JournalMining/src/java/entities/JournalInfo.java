/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "journal_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JournalInfo.findAll", query = "SELECT j FROM JournalInfo j")
    , @NamedQuery(name = "JournalInfo.findByJNumber", query = "SELECT j FROM JournalInfo j WHERE j.jNumber = :jNumber")
    , @NamedQuery(name = "JournalInfo.findByJTitle", query = "SELECT j FROM JournalInfo j WHERE j.jTitle = :jTitle")
    , @NamedQuery(name = "JournalInfo.findByJCitationFrequency", query = "SELECT j FROM JournalInfo j WHERE j.jCitationFrequency = :jCitationFrequency")
    , @NamedQuery(name = "JournalInfo.findByJOthersCitation", query = "SELECT j FROM JournalInfo j WHERE j.jOthersCitation = :jOthersCitation")
    , @NamedQuery(name = "JournalInfo.findByJPages", query = "SELECT j FROM JournalInfo j WHERE j.jPages = :jPages")
    , @NamedQuery(name = "JournalInfo.findByJclassNo", query = "SELECT j FROM JournalInfo j WHERE j.jclassNo = :jclassNo")
    , @NamedQuery(name = "JournalInfo.findByJYear", query = "SELECT j FROM JournalInfo j WHERE j.jYear = :jYear")})
public class JournalInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_number")
    private String jNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "j_title")
    private String jTitle;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "j_abstract")
    private String jAbstract;
    @Basic(optional = false)
    @NotNull
    @Column(name = "j_citation_frequency")
    private int jCitationFrequency;
    @Basic(optional = false)
    @NotNull
    @Column(name = "j_others_citation")
    private int jOthersCitation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_pages")
    private String jPages;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_class_No")
    private String jclassNo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_year")
    private String jYear;

    public JournalInfo() {
    }

    public JournalInfo(String jNumber) {
        this.jNumber = jNumber;
    }

    public JournalInfo(String jNumber, String jTitle, String jAbstract, int jCitationFrequency, int jOthersCitation, String jPages, String jclassNo, String jYear) {
        this.jNumber = jNumber;
        this.jTitle = jTitle;
        this.jAbstract = jAbstract;
        this.jCitationFrequency = jCitationFrequency;
        this.jOthersCitation = jOthersCitation;
        this.jPages = jPages;
        this.jclassNo = jclassNo;
        this.jYear = jYear;
    }

    public String getJNumber() {
        return jNumber;
    }

    public void setJNumber(String jNumber) {
        this.jNumber = jNumber;
    }

    public String getJTitle() {
        return jTitle;
    }

    public void setJTitle(String jTitle) {
        this.jTitle = jTitle;
    }

    public String getJAbstract() {
        return jAbstract;
    }

    public void setJAbstract(String jAbstract) {
        this.jAbstract = jAbstract;
    }

    public int getJCitationFrequency() {
        return jCitationFrequency;
    }

    public void setJCitationFrequency(int jCitationFrequency) {
        this.jCitationFrequency = jCitationFrequency;
    }

    public int getJOthersCitation() {
        return jOthersCitation;
    }

    public void setJOthersCitation(int jOthersCitation) {
        this.jOthersCitation = jOthersCitation;
    }

    public String getJPages() {
        return jPages;
    }

    public void setJPages(String jPages) {
        this.jPages = jPages;
    }

    public String getJclassNo() {
        return jclassNo;
    }

    public void setJclassNo(String jclassNo) {
        this.jclassNo = jclassNo;
    }

    public String getJYear() {
        return jYear;
    }

    public void setJYear(String jYear) {
        this.jYear = jYear;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jNumber != null ? jNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JournalInfo)) {
            return false;
        }
        JournalInfo other = (JournalInfo) object;
        if ((this.jNumber == null && other.jNumber != null) || (this.jNumber != null && !this.jNumber.equals(other.jNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.JournalInfo[ jNumber=" + jNumber + " ]";
    }
    
}
