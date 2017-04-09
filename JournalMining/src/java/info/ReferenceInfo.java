
package info;

public class ReferenceInfo {
    private String title;
    private String number;
    private String authors;
    private String journal;
    private String year;
    private String pages;
    private String[] authorList;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    
    public String[] getAuthorList() {
        return authorList;
    }

    public void setAuthorList() {
        this.authorList = authors.split(",");
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
        return "ReferenceInfo{" + "title=" + title + ", authors=" + authors + ", journal=" + journal + ", year=" + year + ", pages=" + pages + '}';
    }
    
    
    
}
