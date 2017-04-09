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
public class PaperKeywordsPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "j_number")
    private String jNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "keyword")
    private String keyword;

    public PaperKeywordsPK() {
    }

    public PaperKeywordsPK(String jNumber, String keyword) {
        this.jNumber = jNumber;
        this.keyword = keyword;
    }

    public String getJNumber() {
        return jNumber;
    }

    public void setJNumber(String jNumber) {
        this.jNumber = jNumber;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jNumber != null ? jNumber.hashCode() : 0);
        hash += (keyword != null ? keyword.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaperKeywordsPK)) {
            return false;
        }
        PaperKeywordsPK other = (PaperKeywordsPK) object;
        if ((this.jNumber == null && other.jNumber != null) || (this.jNumber != null && !this.jNumber.equals(other.jNumber))) {
            return false;
        }
        if ((this.keyword == null && other.keyword != null) || (this.keyword != null && !this.keyword.equals(other.keyword))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PaperKeywordsPK[ jNumber=" + jNumber + ", keyword=" + keyword + " ]";
    }
    
}
