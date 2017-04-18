/*
 * 第五模块，科研作者分析
	作者影响力(h指数)
	核心作者（普赖斯公式）
	作者发文排名
	作者被引排名

 */
package dataAnalysisBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author Think
 */
@Named(value = "authorAnalysis")
@SessionScoped
public class AuthorAnalysis implements Serializable {

    public AuthorAnalysis() {
    }
     String 
             sql= "select j_author as 作者 ,count(*) a from paper_author group by j_author having count(*)>=(select  0.749*sqrt(count(*)) as 普赖斯指数 from paper_author  group by j_author order by 普赖斯指数 DESC limit 1) order by a DESC";
}
