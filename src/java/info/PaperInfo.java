package info;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    private Set<String> fundList;
    private String[] affiliationList;

    public PaperInfo() {
    }

    public void createJournalInfo() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String[] getAuthorList() {
        setAuthorList();
        return authorList;
    }

    public void setAuthorList() {
        try {
            this.authorList = this.authors.split(",");
        } catch (Exception e) {
            System.out.println(this.number);
        }

    }

    public String[] getKeywordList() {
        setKeywordList();
        return keywordList;
    }

    public void setKeywordList() {
        this.keywordList = this.keyword.split(",");
    }

    public Set<String> getFundList() {
        setFundList();
        return fundList;
    }

    public void setFundList() {
        if (this.fund != null) {
            this.fund = this.fund.replaceAll("\\([^\\)]+\\)", "");
            this.fund = this.fund.replaceAll("[资助|资助~~]", "");
            String[] funds = this.fund.split(",");
            Set<String> temp = new HashSet<>();
            for (String item : funds) {
                if (item.contains("基金")) {
                    item = item.substring(0,item.indexOf("基金")+2);
                }
                temp.add(item);
            }
            
            this.fundList = temp;
        }

    }

    public String[] getAffiliationList() {
        setAffiliationList();
        return affiliationList;
    }

    public void setAffiliationList() {
        try {
            this.affiliationList = this.affiliation.split(",");
        } catch (Exception e) {

        }
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title.replaceAll("'", "\"");
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors.replaceAll("'", "\"");
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        affiliation = affiliation.replaceAll("'", "\"");
        this.affiliation = affiliation;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        keyword = keyword.replaceAll("'", "\"");
        this.keyword = keyword;
    }

    public String getAbstract1() {
        return abstract1;
    }

    public void setAbstract1(String abstract1) {
        abstract1 = abstract1.replaceAll("'", "\"");
        this.abstract1 = abstract1;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        fund = fund.replaceAll("'", "\"");
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
        try {
            this.year = year.substring(0, 4);
        } catch (Exception e) {
            System.out.println(number + "年有错");

        }

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

    public double tranformPages(String str) {
        double pages1 = 0;
        try {
            if (!str.equals("") && str != null) {
                String[] digit = str.split("[+-]");
                pages1 = Integer.valueOf(digit[1]) - Integer.valueOf(digit[0]);
                if (digit.length >= 3) {
                    pages1 += 0.5;
                }
            }

        } catch (Exception e) {
            pages1 = 0;
        }
        return pages1;
    }

    @Override
    public String toString() {
        return "PaperInfo{" + "title=" + title + ", authors=" + authors + ", affiliation=" + affiliation + ", keyword=" + keyword + ", abstract1=" + abstract1 + ", fund=" + fund + ", origin=" + origin + ", year=" + year + ", pages=" + pages + ", classNo=" + classNo + ", citation_frequency=" + citation_frequency + ", othersCitation=" + othersCitation + '}';
    }

    public Statement addSQL(Statement stat) throws SQLException {
        String sql;
        sql = "insert into journal_info(j_number,j_title,j_abstract,j_citation_frequency,j_others_citation,j_pages,j_class_No,j_year,j_orgin,j_page2) values('" + number + "','" + title + "','" + abstract1 + "'," + citation_frequency + "," + othersCitation + ",'" + pages + "','" + classNo + "','" + year + "','" + origin + "'," + tranformPages(pages) + ")";
        stat.addBatch(sql);
        try {
            for (String item : getAuthorList()) {
                sql = "insert into paper_author(j_number,j_author) values('" + number + "','" + item + "')";
                stat.addBatch(sql);
            }
        } catch (Exception e) {
            System.out.println(number);

        }
        try {
            for (String item : getAffiliationList()) {
                sql = "insert into paper_address(j_number,j_address) values('" + number + "','" + item + "')";
                stat.addBatch(sql);
            }

        } catch (Exception e) {

        }
        try {
            if (fund != null && !fund.equals("")) {
                for (String item : getFundList()) {
                    sql = "insert into paper_fund(j_number,j_fund) values('" + number + "','" + item + "')";
                    stat.addBatch(sql);
                }
            }
        } catch (Exception e) {

        }
        try {
            for (String item : getKeywordList()) {
                sql = "insert into paper_keywords(j_number,keyword) values('" + number + "','" + item + "')";
                stat.addBatch(sql);
            }
        } catch (Exception e) {

        }

        return stat;

    }

}
