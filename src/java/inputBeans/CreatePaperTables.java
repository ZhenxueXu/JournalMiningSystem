
package inputBeans;


import JDBCUtils.JDBCUtils;
import info.PaperInfo;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.enterprise.context.SessionScoped;




@Named(value = "createPaperTables")
@SessionScoped
public class CreatePaperTables implements Serializable {

    private PaperInfo paperInfo;
    
    

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
    
    public Statement prepareSQL(Statement st,PaperInfo paper){
        
        return st;       
    }
    public void createTables(){
        Connection conn = null;
        Statement stat=null;
        ResultSet rs=null;
        try{
            conn = JDBCUtils.getConn();
            System.out.println("连接成功");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("连接失败");            
        }finally{
            JDBCUtils.close(rs, stat, conn);
            
        }
               
    }

    
    
}
