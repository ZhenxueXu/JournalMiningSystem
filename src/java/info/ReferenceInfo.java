
package info;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ReferenceInfo {
    private String title;
    private String number;
    private String r_number;
    private String authors;
    private String journal;
    private String year;
    private String pages;
    private String[] authorList;
    private int type;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getAuthorList() {
        setAuthorList();
        return authorList;
    }

    public void setAuthorList() {
        this.authorList = authors.split(",");
        Set temp = new HashSet()  ;
        temp.addAll(Arrays.asList(this.authorList));
        Object [] object= temp.toArray();
        String[] str = new String[object.length] ;
        for(int i=0; i<object.length;i++){
            str[i] =(String) object[i];
        }
        this.authorList = str;       
    }

    public String getR_number() {
        return r_number;
    }

    public void setR_number(String r_number) {
        this.r_number = r_number;
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

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "ReferenceInfo{" + "title=" + title + ", number=" + number + ", r_number=" + r_number + ", authors=" + authors + ", journal=" + journal + ", year=" + year + ", pages=" + pages + ", type=" + type + '}';
    }
 
    
    public Statement addSQL(Statement stat) throws SQLException{
        if (title != null && !title.equals("") && authors != null && !authors.equals("")) {
            String sql = "insert into paper_references(j_number,r_number,r_title,r_journal,r_year,r_pages,r_type) values('" + number + "','" + r_number + "','" + title + "','" + journal + "','" + year + "','" + pages + "','" + type + "')";
            stat.addBatch(sql);
            for (String item : getAuthorList()) {
                sql = "insert into references_author values('" + number + "','" + r_number + "','" + item + "')";
                stat.addBatch(sql);
            }
        }
        return stat;
        
    }
    
    
}
