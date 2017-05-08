
package dataAnalysisBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *引用期刊的关联分析
 * @author Think
 */
@Named(value = "refJournalCluster")
@SessionScoped
public class RefJournalAssociation implements Serializable {
    
    public RefJournalAssociation() {
    }
    
}
 