import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.io.IOUtils;
/*import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletRequestContext;*/


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
//import org.apache.tomcat.util.http.fileupload.disk.*;
//import org.apache.tomcat.util.http.fileupload.servlet.*;
//import org.apache.tomcat.util.http.fileupload.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class InsertFileServlet extends HttpServlet{
  public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
    File file ;
    int maxFileSize = 1*1024*1024;
    int maxMemSize = 8*1024;
    String contentType = request.getContentType();
    if ((contentType.indexOf("multipart/form-data") >= 0)) {

       DiskFileItemFactory factory = new DiskFileItemFactory();
       factory.setSizeThreshold(maxMemSize);
       factory.setRepository(new File("E:\\temp\\"));
       System.out.println(factory.getRepository());
       factory.setFileCleaningTracker(new FileCleaningTracker());
       FileCleaningTracker tracker=factory.getFileCleaningTracker();
       if(tracker!=null){
         System.out.println("rep");
         tracker.track(factory.getRepository(), factory);
       }
       else{
         System.out.println("null");
       }
       //factory.setFileCleaningTracker(tracker);
       ServletFileUpload upload = new ServletFileUpload(factory);
       upload.setSizeMax( maxFileSize );
       JSONObject result=new JSONObject();
       try{
          List fileItems = upload.parseRequest(request);
          Iterator i = fileItems.iterator();
          while ( i.hasNext () )
          {
            FileItem fi = (FileItem)i.next();
            if ( !fi.isFormField () )  {
              InputStream is=fi.getInputStream();
              BufferedReader reader=new BufferedReader(new InputStreamReader(is));
              String line;
              //-----
              ArrayList <User> objects=new ArrayList<User>();
              JSONArray errors=new JSONArray();
              int row=0;
              while((line=reader.readLine())!=null){
                row++;
                int flag=0;
                String error="Row:"+String.valueOf(row);
                String[] splited=line.split(",");

                if(splited[0].matches("[0-9]+") && !(splited[0].equals("0"))){
                  flag++;
                }
                else{
                  error=error+" Wrong input for Id;";
                }
                if(splited[1].matches("[A-Z][a-zA-Z]*")){
                  flag++;
                }
                else{
                  error=error+" Wrong input for Name;";
                }
                if(splited[2].matches("[0-9]{1,2}(\\.[0-9]*)?")){
                  flag++;
                }
                else{
                  error=error+" Wrong input for percentage;";
                }
                if(flag==3){
                  User user=new User();
                  user.setId(Integer.parseInt(splited[0]));
                  user.setName(splited[1]);
                  user.setPercentage(Double.parseDouble(splited[2]));
                  objects.add(user);
                }
                else{
                  errors.add(error);
                }
              }
              new NativeService().insertList(objects,new User());
              result.put("isException",false);
              result.put("errors",errors);
              result.put("total",row);
              result.put("success",objects.size());
              result.put("error",errors.size());
              //----
            }
          }
          response.setContentType("application/json");
        	response.setCharacterEncoding("UTF-8");
          PrintWriter out=response.getWriter();
          out.print(result);
        }
        catch(Exception ex){
          System.out.println(ex);
          JSONObject object=new JSONObject();
          object.put("isException",true);
          object.put("exception",ex.toString());
          response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8");
          PrintWriter out=response.getWriter();
          out.print(object.toString());
        }
        System.out.println("files:"+tracker.getDeleteFailures().toString());
        /*File folder = new File("E:\\temp\\");
        File[] files = folder.listFiles();
        for(int i=0;i<files.length;i++){
          System.out.println(files[i].delete());
        }*/
    }
    else{
      PrintWriter out=response.getWriter();
      out.print("File upload Failure");
     }
  }
}
