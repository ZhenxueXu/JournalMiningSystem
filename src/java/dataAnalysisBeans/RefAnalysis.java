
package dataAnalysisBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Think
 * 被引统计
 */
@Named(value = "refAnalysis")
@SessionScoped
public class RefAnalysis implements Serializable {

    public RefAnalysis() {
    }
    
}
