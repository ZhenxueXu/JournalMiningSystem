
package dataAnalysisBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;


@Named(value = "highRefAffiliation")
@SessionScoped
public class HighRefAffiliation implements Serializable {
    private int minTimes = 20;

    public HighRefAffiliation() {
    }
    
}
