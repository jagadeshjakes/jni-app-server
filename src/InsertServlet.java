import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.JSONObject;
public class InsertServlet extends HttpServlet{
  public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException {
    String id=request.getParameter("id");
    String name=request.getParameter("name");
    String percentage=request.getParameter("percentage");
    //System.out.println(id+" "+name+" "+percentage);
    int flag=0;
    String error="";
    if(id.matches("[0-9]+") && !(id.equals("0"))){
      flag++;
    }
    else{
      error=error+" Wrong input for Id;";
    }
    if(name.matches("[a-zA-Z]*")){
      flag++;
    }
    else{
      error=error+" Wrong input for Name;";
    }
    if(percentage.matches("[0-9]{1,2}(\\.[0-9]*)?")){
      flag++;
    }
    else{
      error=error+" Wrong input for percentage;";
    }
    try{
      if(flag==3){

        User user=new User();
        user.setId(Integer.parseInt(id));
        user.setName(name);
        user.setPercentage(Double.parseDouble(percentage));
        new NativeService().insert(user);
        JSONObject object= new JSONObject();
        object.put("result",true);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out=response.getWriter();
        out.println(object);
      }
      else{
      JSONObject object= new JSONObject();
      object.put("result",false);
      object.put("error",error);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter out=response.getWriter();
      out.println(object);
      }
    }
    catch(Exception ex){
      JSONObject object= new JSONObject();
      object.put("result",false);
      object.put("error",ex.toString());
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter out=response.getWriter();
      out.println(object);
    }
  }
}
