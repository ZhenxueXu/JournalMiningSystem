
package inputBeans;

import entities.*;
import info.PaperInfo;
import javax.inject.Named;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



@Named(value = "createPaperTables")
@SessionScoped
public class CreatePaperTables implements Serializable {
    private PaperInfo paperInfo;
    private JournalInfo journalInfo;
    private PaperAddress paperAddress;
    private PaperAuthor paperAuthor;
    private PaperFund paperFund;
    private PaperKeywords paperKeywords;
    
    

    public CreatePaperTables() {
    }
    
    public CreatePaperTables(PaperInfo paperInfo) {
        this.paperInfo = paperInfo;
        
    }

    public void setPaperInfo(PaperInfo paperInfo) {
        this.paperInfo = paperInfo;
    }

    public PaperInfo getPaperInfo() {
        return paperInfo;
    }
 
   /*
   @EJB
   private PaperAddressFacade addressFacade = new PaperAddressFacade();
   @EJB
   private PaperAuthorFacade authorFacade = new PaperAuthorFacade();
   @EJB
   private PaperFundFacade fundFacade = new PaperFundFacade();
   @EJB
   private PaperKeywordsFacade keywordFacade = new PaperKeywordsFacade();

    

    public PaperAddressFacade getAddressFacade() {
        return addressFacade;
    }

    public PaperAuthorFacade getAuthorFacade() {
        return authorFacade;
    }

    public PaperFundFacade getFundFacade() {
        return fundFacade;
    }

    public PaperKeywordsFacade getKeywordFacade() {
        return keywordFacade;
    }
   */

    
    
    public void createTables() {
        journalInfo = new JournalInfo(paperInfo.getNumber(),paperInfo.getTitle(),paperInfo.getAbstract1(),Integer.valueOf(paperInfo.getCitation_frequency()),Integer.valueOf(paperInfo.getOthersCitation()),paperInfo.getPages(),paperInfo.getClassNo(),paperInfo.getYear());
      
        System.out.println("xxxx  " + journalInfo.getJNumber() + journalInfo.getJTitle());
           
        /*      
        for (String affiliationList : this.paperInfo.getAffiliationList()) {
            this.paperAddress = new PaperAddress(this.paperInfo.getNumber(), affiliationList);
            getAddressFacade().create(this.paperAddress);
        }
       
        for (String item : this.paperInfo.getAuthorList() ){
            this.paperAuthor = new PaperAuthor(this.paperInfo.getNumber(),item);
            getAuthorFacade().create(this.paperAuthor);
        }
        for (String item : this.paperInfo.getKeywordList() ){
            this.paperKeywords = new PaperKeywords(this.paperInfo.getNumber(),item);
            getKeywordFacade().create(this.paperKeywords);
        }
        if(this.paperInfo.getFund()!=null){
            for (String item : this.paperInfo.getFundList()) {
                this.paperFund = new PaperFund(this.paperInfo.getNumber(), item);
                getFundFacade().create(this.paperFund);
            }     
            
        }*/
           
    }

    
    
}
