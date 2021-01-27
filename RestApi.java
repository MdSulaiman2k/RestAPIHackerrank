import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.net.* ;
import  org.json.* ;
import java.util.regex.*;
import java.util.stream.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class RestApi{

   public String returnjsonObject( String query){
     StringBuffer response = new StringBuffer() ;
    try{
     
     URL ur = new URL(query) ;
     HttpURLConnection   con = (HttpURLConnection) ur.openConnection() ;
     BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
     String res ; 
     while((res = in.readLine()) != null){
        response.append(res) ;
      } 
    }catch(Exception err){
       System.out.println(err) ;
    }
      return response.toString() ;
  }


  public  int getTotalGoals(String team , int year, String com){
    int total =0 ;
    try {
      for(int k = 1 ; k <= 2 ; k++ ){
        String t = "team" + k  ;
        String query ;
        query = "https://jsonmock.hackerrank.com/api/football_matches?competition="+com+"&year="+year+"&" + t + "="+team+"&page="+ 1 ; 
        JSONObject json = new JSONObject(returnjsonObject(query)) ;
        int cnt = json.getInt("total_pages") ;
        for(int i = 1 ; i <= cnt ; ){
        JSONArray jsonarray = new JSONArray(json.getJSONArray("data").toString()) ;
        for(int j =0 ; j < jsonarray.length() ; j++){
          total   += Integer.parseInt(jsonarray.getJSONObject(j).getString("team" +k+ "goals"));
        }
         query = "https://jsonmock.hackerrank.com/api/football_matches?year="+year+"&" + t + "="+team+"&page="+ (++i) ;
        json = new JSONObject(returnjsonObject(query)) ;
   }
      }
   }catch(Exception err){
     System.out.println(err.getMessage());
   }


   return total ;
  }

  public String WinnigTeam(String query , String name ){
       String teamname = "" ;
       int i = 1 ; 
       JSONObject json = new JSONObject(ExcuteQueary(query +"&page=" + i++)) ;
       int total = json.getInt("total_pages") ;
       json = new JSONObject(ExcuteQueary(query +"&page=" + total)) ;
       JSONArray jsonarray = new JSONArray(json.getJSONArray("data").toString()) ;
       int length =  jsonarray.length() ;
       int total1 =  new JSONObject(jsonarray.get(length-1).toString()).getInt("team1goals") ;
       int total2 =  new JSONObject(jsonarray.get(length-1).toString()).getInt("team2goals") ;
       teamname = (total1 > total2)?new JSONObject(jsonarray.get(length-1).toString()).getString("team1") : new JSONObject(jsonarray.get(length-1).toString()).getString("team2") ;
       return teamname ;
  }

  public BufferedReader getConnection(String query) throws Exception{
      URL url = new URL(query) ;
      HttpURLConnection con = (HttpURLConnection) url.openConnection() ;
      return new BufferedReader(new InputStreamReader(con.getInputStream())) ;
  }

  
  public String ExcuteQueary(String query){
    StringBuffer response = new StringBuffer() ;
    try{
    BufferedReader in = getConnection( query) ;
    String res ;
    while((res = in.readLine()) != null){
      response.append(res) ;
    }
    }catch(Exception err){
      System.out.println(err.getMessage());
    }
    return response.toString() ;

  }

  
  
  public int getWinnerTotalGoals(String name , int year){
     name = name.replaceAll(" " , "%20") ;
     String query = "https://jsonmock.hackerrank.com/api/football_matches?competition="+name + "&year=" + year ;
     String teamname = WinnigTeam(query , name ) ;
     int totalgoals = getTotalGoals(teamname, year, name) ;
     return totalgoals ;
     
  }


}