
import java.util.*;
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
public class NativeService{
  static{
    System.load("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 8.0\\webapps\\jni-app-server\\WEB-INF\\cpp\\crud.dll");
  }

  public native String insert(User user);
  public native ArrayList<User> view(User user) throws Exception;
  public native String insertList(ArrayList<User> obj,User user);

  private int id;
  private String name;
  private double percentage;


  public int getId(){
    return this.id;
  }
  public void setId(int id){
    this.id=id;
  }
  public String getName(){
    return this.name;
  }
  public void setName(String name){
    this.name=name;
  }
  public double getPercentage(){
    return this.percentage;
  }
  public void setPercentage(double percentage){
    this.percentage=percentage;
  }
}
