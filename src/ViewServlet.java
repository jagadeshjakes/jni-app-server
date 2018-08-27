import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class ViewServlet extends HttpServlet{
  public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {

    try{
      ArrayList<User> users=null;
      users=new NativeService().view(new User());
      JSONArray result=new JSONArray();
      JSONObject object=new JSONObject();
      for(int i=0;i<users.size();i++){
        JSONArray temp=new JSONArray();
        User user=users.get(i);
        temp.add(user.getId());
        temp.add(user.getName());
        double percentage=Math.round((double)user.getPercentage()*100)/100.0;
        temp.add(percentage);
        result.add(temp);
      }
      object.put("table",result);
      object.put("isException",false);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter out=response.getWriter();
      out.print(object.toString());
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
  }
}
