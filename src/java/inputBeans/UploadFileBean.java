package inputBeans;

import JDBCUtils.JDBCUtils;
import info.PaperInfo;
import info.ReferenceInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.UploadedFile;
import org.primefaces.event.FileUploadEvent;

@Named(value = "uploadFileBean")
@SessionScoped
public class UploadFileBean implements Serializable {

    private UploadedFile file;
    private String filename;
    private String tempStr;
    private PaperInfo paper;
    private ReferenceInfo reference;
    private List<File> files = new ArrayList<File>();

    public UploadFileBean() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
//数据上传到服务器文件夹下

    public void handleFileUpload(FileUploadEvent event) throws Exception {
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        filename = event.getFile().getFileName();
        String newFilePath = extContext.getRealPath("//files") + "//" + filename;
        File result = new File(newFilePath);
        try {
            FileOutputStream fileoutstream = new FileOutputStream(result);
            byte[] buffer = new byte[100000];
            int bulk;
            InputStream inputstream = event.getFile().getInputstream();
            while (true) {
                bulk = inputstream.read(buffer);
                if (bulk < 0) {
                    break;
                }
                fileoutstream.write(buffer, 0, bulk);
                fileoutstream.flush();
            }
            fileoutstream.close();
            inputstream.close();
            FacesMessage message = new FacesMessage("成功！", event.getFile().getFileName() + " 已经上传.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            files.add(result);

        } catch (IOException e) {
            e.printStackTrace();
            FacesMessage error = new FacesMessage("文件没有上传");
            FacesContext.getCurrentInstance().addMessage(null, error);
        }
    }

    public String fileToDB() throws Exception {
        BufferedReader reader = null;
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        int type = 0;
        String number = null;
        int i = 1;
        //数据库连接
        try {
            conn = JDBCUtils.getConn();
            conn.setAutoCommit(false);
            stat = conn.createStatement();
            int r_number = 0;
            for (File bfile : files) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(bfile), "UTF-8");
                reader = new BufferedReader(isr);
                String pattern1 = "\\d";                             //匹配文章信息
                Pattern r1 = Pattern.compile(pattern1);
                Matcher m1 = null;
                while ((tempStr = reader.readLine()) != null) {

                    if (tempStr.contains("参考文献如下")) {
                        r_number = 1;
                        type = 0;
                    }
                    if (tempStr.contains("引证文献如下")) {
                        r_number = 1;
                        type = 1;
                    }
                    if (tempStr.startsWith("[")) {     //处理参考文献
                        splitRerence(tempStr, number, type, r_number);
                        stat = reference.addSQL(stat);
                        r_number++;
                    }
                    if (!tempStr.equals("")) {
                        m1 = r1.matcher(tempStr.substring(0, 1));
                        if (m1.matches()) {      //处理文章信息
                            number = "j" + i++;
                            splitInfo(tempStr, number);
                            stat = paper.addSQL(stat);
                        }
                    }
                    if (i % 200 == 0) {
                        stat.executeBatch();
                    }
                }
                stat.executeBatch();
                reader.close();
            }
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        baiduMap.CreateMapInformation.createInformation();
        if (files.size() <= 0) {
            FacesMessage error = new FacesMessage("没有上传文件");
            FacesContext.getCurrentInstance().addMessage(null, error);
            return null;

        }
        return "infopage.xhtml";
    }

    /*
    *论文基本信息按字段划分
     */
    public void splitInfo(String info, String number) throws Exception {
        String splitInfo[] = tempStr.split("\\$\\$");
        paper = new PaperInfo();
        paper.setNumber(number);
        if (splitInfo.length > 1) {
            for (int i = 0; i < splitInfo.length; i = i + 1) {
                switch (splitInfo[i]) {
                    case "题名":
                        paper.setTitle(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "作者":
                        paper.setAuthors(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "单位":
                        paper.setAffiliation(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "中文关键词":
                        paper.setKeyword(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "关键词":
                        paper.setKeyword(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "中文摘要":
                        paper.setAbstract1(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "摘要":
                        paper.setAbstract1(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "基金":
                        paper.setFund(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "来源":
                        paper.setOrigin(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "年卷期":
                        paper.setYear(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "页":
                        paper.setPages(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "ISSN":
                        break;
                    case "CN":
                        break;
                    case "语种":
                        break;
                    case "分类号":
                        paper.setClassNo(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "DOI":
                        break;
                    case "被引频次":
                        paper.setCitation_frequency(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                    case "他引频次":
                        paper.setOthersCitation(splitInfo[i + 1].substring(0, splitInfo[i + 1].length() - 1));
                        break;
                }
            }
            //System.out.println(paper.toString());
        }
    }

    /*
    *参考文献的处理
     */
    public void splitRerence(String info, String number, int type, int rnumber) {
        reference = new ReferenceInfo();
        reference.setNumber(number);
        reference.setR_number("r" + type + rnumber);
        reference.setType(type);
        int start;
        int end;
        try {
            start = info.indexOf("]");                                           //参考文献开始位置
            end = info.indexOf("].", start);                                     //标题结束位置，倒着解析避免国外作者名字中的小数点
            String str = info.substring(start, end);
            end = start + str.lastIndexOf(".");
            reference.setAuthors(info.substring(start + 1, end).trim());
            start = end + 1;
            end = info.indexOf("].", start);
            reference.setTitle(info.substring(start, end + 1).trim());
            start = end + 2;
            end = info.indexOf(",", start);
            reference.setJournal(info.substring(start, end).trim());
            try {
                start = end + 1;
                end = info.indexOf(",", start);
                reference.setYear(info.substring(start, end).trim());
                start = info.indexOf(":", end + 1);
                if (start > 0) {
                    end = info.lastIndexOf(".");
                    if (end - start > 1) {
                        reference.setPages(info.substring(start + 1, end).trim());
                    }
                }
            } catch (Exception e) {
                reference.setYear("null");
                reference.setPages("null");

            }
        } catch (Exception e) {
            System.out.println("解析错误数据第" + number + "篇论文，第" + rnumber + "篇参考文献");

        }

        //System.out.println(reference.toString());
    }
}
