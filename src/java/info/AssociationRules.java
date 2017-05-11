
package info;

/**
 *描述关联规则
 * @author Think
 */
public class AssociationRules {
    private String rule;
    private double support;
    private double confidence;

    public AssociationRules(String rule, double support, double confidence) {
        this.rule = rule;
        this.support = support;
        this.confidence = confidence;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return  rule + " : "+ support + "  " + confidence + '}';
    }
    
}
