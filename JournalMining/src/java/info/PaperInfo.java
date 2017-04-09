
package info;

import entities.*;


public class PaperInfo {
    private String title;
    private String number;
    private String authors;
    private String affiliation;
    private String keyword;
    private String abstract1;
    private String fund;
    private String origin;
    private String year;
    private String pages;
    private String classNo;
    private String citation_frequency;
    private String othersCitation;
    private String[] authorList;
    private String[] keywordList;
    private String[] fundList;
    private String[] affiliationList;
    private JournalInfo journalInfo;
    private PaperAddress paperAddress;
    private PaperAuthor paperAuthor;
    private PaperFund paperFund;
    private PaperKeywords paperKeywords;
   
    
   
    public PaperInfo() {
    }

    public void createJournalInfo(){
        
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public JournalInfo getJournalInfo() {
        return journalInfo;
    }

    public void setJournalInfo(JournalInfo journalInfo) {
        this.journalInfo = journalInfo;
    }

    public PaperAddress getPaperAddress() {
        return paperAddress;
    }

    public void setPaperAddress(PaperAddress paperAddress) {
        this.paperAddress = paperAddress;
    }

    public PaperAuthor getPaperAuthor() {
        return paperAuthor;
    }

    public void setPaperAuthor(PaperAuthor paperAuthor) {
        this.paperAuthor = paperAuthor;
    }

    public PaperFund getPaperFund() {
        return paperFund;
    }

    public void setPaperFund(PaperFund paperFund) {
        this.paperFund = paperFund;
    }

    public PaperKeywords getPaperKeywords() {
        return paperKeywords;
    }

    public void setPaperKeywords(PaperKeywords paperKeywords) {
        this.paperKeywords = paperKeywords;
    }

    
    public String[] getAuthorList() {
        return authorList;
    }

    public void setAuthorList() {
        this.authorList = this.authors.split(",");
    }

    public String[] getKeywordList() {
        return keywordList;
    }

    public void setKeywordList() {
        this.keywordList = this.keyword.split(",");
    }

    public String[] getFundList() {
        return fundList;
    }

    public void setFundList() {
        if(this.fund!=null){
            this.fund = this.fund.replaceAll("\\([^\\)]+\\)", "");
            this.fundList = this.fund.split(",");          
        }
        
    }

    public String[] getAffiliationList() {
        return affiliationList;
    }

    public void setAffiliationList() {
        this.affiliationList = this.affiliation.split(",");
    }
    
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAbstract1() {
        return abstract1;
    }

    public void setAbstract1(String abstract1) {
        this.abstract1 = abstract1;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year.substring(0,year.indexOf(","));
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getCitation_frequency() {
        return citation_frequency;
    }

    public void setCitation_frequency(String citation_frequency) {
        this.citation_frequency = citation_frequency;
    }

    public String getOthersCitation() {
        return othersCitation;
    }

    public void setOthersCitation(String othersCitation) {
        this.othersCitation = othersCitation;
    }

    @Override
    public String toString() {
        return "PaperInfo{" + "title=" + title + ", authors=" + authors + ", affiliation=" + affiliation + ", keyword=" + keyword + ", abstract1=" + abstract1 + ", fund=" + fund + ", origin=" + origin + ", year=" + year + ", pages=" + pages + ", classNo=" + classNo + ", citation_frequency=" + citation_frequency + ", othersCitation=" + othersCitation + '}';
    }
    
    
    
}
