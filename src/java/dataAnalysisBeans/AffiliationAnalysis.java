
package dataAnalysisBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *机构分析
 * 1、机构发文排名
 * 2、机构被引排名
 * @author Think
 */
@Named(value = "affiliationAnalysis")
@SessionScoped
public class AffiliationAnalysis implements Serializable {

    public AffiliationAnalysis() {
    }
    
}
