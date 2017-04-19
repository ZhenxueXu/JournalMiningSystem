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
    , @NamedQuery(name = "JournalInfo.findByJYear", query = "SELECT j FROM JournalInfo j WHERE j.jYear = :jYear")
    , @NamedQuery(name = "JournalInfo.findByJOrgin", query = "SELECT j FROM JournalInfo j WHERE j.jOrgin = :jOrgin")
    , @NamedQuery(name = "JournalInfo.findByJPage2", query = "SELECT j FROM JournalInfo j WHERE j.jPage2 = :jPage2")})
public class JournalInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_number")
    private String jNumber;
    @Size(max = 200)
    @Column(name = "j_title")
    private String jTitle;
    @Lob
    @Size(max = 65535)
    @Column(name = "j_abstract")
    private String jAbstract;
    @Column(name = "j_citation_frequency")
    private Integer jCitationFrequency;
    @Column(name = "j_others_citation")
    private Integer jOthersCitation;
    @Size(max = 50)
    @Column(name = "j_pages")
    private String jPages;
    @Size(max = 50)
    @Column(name = "j_class_No")
    private String jclassNo;
    @Size(max = 50)
    @Column(name = "j_year")
    private String jYear;
    @Size(max = 100)
    @Column(name = "j_orgin")
    private String jOrgin;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "j_page2")
    private Double jPage2;

    public JournalInfo() {
    }

    public JournalInfo(String jNumber) {
        this.jNumber = jNumber;
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

    public Integer getJCitationFrequency() {
        return jCitationFrequency;
    }

    public void setJCitationFrequency(Integer jCitationFrequency) {
        this.jCitationFrequency = jCitationFrequency;
    }

    public Integer getJOthersCitation() {
        return jOthersCitation;
    }

    public void setJOthersCitation(Integer jOthersCitation) {
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

    public String getJOrgin() {
        return jOrgin;
    }

    public void setJOrgin(String jOrgin) {
        this.jOrgin = jOrgin;
    }

    public Double getJPage2() {
        return jPage2;
    }

    public void setJPage2(Double jPage2) {
        this.jPage2 = jPage2;
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
