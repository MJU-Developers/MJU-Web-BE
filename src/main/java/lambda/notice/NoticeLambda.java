package lambda.notice;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.sql.*;


public class NoticeLambda implements RequestStreamHandler {

    private static String dburl = System.getenv("uri");
    private static String dbUser = System.getenv("username");
    private static String dbpassword = System.getenv("password");


    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parse = new JSONParser();
        JSONObject responseObject = new JSONObject();
        JSONObject responseBody = new JSONObject();

        String sql = "insert into NOTICE(notice_id, login_id, title, descriptions, dates) values (null, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql2="INSERT INTO PHOTO VALUE (NULL,LAST_INSERT_ID(),NULL,?,?)";
        PreparedStatement pstmt2=null;

        String loginId;
        String title;
        String description;
        Date date = new Date(System.currentTimeMillis());
        Integer check=null;
        
        //#TODO 사진과 관련하여 저장+수정할것

        try{
            JSONObject reqObject = (JSONObject) parse.parse(reader);

            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection(dburl,dbUser,dbpassword);
            pstmt = con.prepareStatement(sql);
            pstmt2 = con.prepareStatement(sql2);
            if (reqObject.get("pathParameters") != null) {
                JSONObject pps = (JSONObject) reqObject.get("pathParameters");
                if (pps.get("notice_id") != null) {

                    loginId=String.valueOf(pps.get("login_id"));
                    pstmt.setString(1,loginId);
                    title= String.valueOf(pps.get("title"));
                    pstmt.setString(2,title);
                    description= String.valueOf(pps.get("description"));
                    pstmt.setString(3,description);
                    //내가 세팅해아할 수 도있다.
                    pstmt.setDate(4,date);
                    check=pstmt.executeUpdate();
                }
                if(pps.get("file_type")!=null){
                    pstmt2.setString(1, (String) pps.get("file_type"));
                    pstmt2.setString(2, (String) pps.get("file_name"));
                    pstmt2.executeUpdate();
                }
            }
            else if (reqObject.get("queryStringParameters") != null) {
                JSONObject qps = (JSONObject) reqObject.get("queryStringParameters");
                if (qps.get("notice_id") != null) {

                    loginId=String.valueOf(qps.get("login_id"));
                    pstmt.setString(1,loginId);
                    title= String.valueOf(qps.get("title"));
                    pstmt.setString(2,title);
                    description= String.valueOf(qps.get("description"));
                    pstmt.setString(3,description);
                    pstmt.setDate(4,date);
                    check=pstmt.executeUpdate();
                }
                if(qps.get("file_type")!=null){
                    pstmt2.setString(1, (String) qps.get("file_type"));
                    pstmt2.setString(2, (String) qps.get("file_name"));
                    pstmt2.executeUpdate();
                }
            }
            if (check.equals(1)) {
                //#TODO Product대신에 notice가 들어가야한다.
                Notice notice = new Notice(pstmt.toString());
                responseBody.put("notice", notice);
                responseObject.put("statusCode", 200);
            } else {
                responseBody.put("message", "No Items Found");
                responseObject.put("statusCode", 404);
            }
            responseObject.put("body", responseBody.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.write(responseObject.toString());
        reader.close();
        writer.close();
        close(con,pstmt,null);
        close(con,pstmt2,null);
    }

    public void handlerPatchRequest(InputStream input, OutputStream output, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parse = new JSONParser();
        JSONObject responseObject = new JSONObject();
        JSONObject responseBody = new JSONObject();

        String sql = "update NOTICE set title=?, descriptions=? where notice_id=?";
        String sql2 = "update PHOTO set file_type=?,file_name=? where notice_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2=null;

        String fileType;
        String fileName;
        Integer noticeId=null;
        String title;
        String description;

        try{
            JSONObject reqObject = (JSONObject) parse.parse(reader);

            Class.forName("com.mysql.cj.jdbc.Driver");
            //mysql uri,username,password lambda에 환경변수에 넣어야한다.
            con=DriverManager.getConnection(System.getenv("uri"),System.getenv("username"),System.getenv("password"));
            pstmt = con.prepareStatement(sql);
            pstmt2 = con.prepareStatement(sql2);
            if (reqObject.get("body") != null) {

                Notice notice = new Notice((String) reqObject.get("body"));
                Photo photo = new Photo((String) reqObject.get("body"));

                title = String.valueOf(notice.getTitle());
                pstmt.setString(1, title);
                description = String.valueOf(notice.getDescription());
                pstmt.setString(2, description);
                noticeId = Integer.parseInt(String.valueOf(notice.getNoticeId()));
                pstmt.setInt(3, noticeId);
                pstmt.executeUpdate();

                fileType = photo.getFileType();
                pstmt2.setString(1, fileType);
                fileName = photo.getFileName();
                pstmt2.setString(2, fileName);
                pstmt2.executeUpdate();


                responseBody.put("message", "New Item created/update");
                responseObject.put("statusCode", 200);
                responseObject.put("body", responseBody.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.write(responseObject.toString());
        reader.close();
        writer.close();
        close(con,pstmt,null);
        close(con,pstmt2,null);
    }

    public void handleDeleteRequest(InputStream input, OutputStream output, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parse = new JSONParser();
        JSONObject responseObject = new JSONObject();
        JSONObject responseBody = new JSONObject();

        String sql =  "delete from NOTICE where notice_id=?";
        Connection con=null;
        PreparedStatement pstmt=null;

        Integer noticeId=null;

        try{
            JSONObject reqObject = (JSONObject) parse.parse(reader);


            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection(System.getenv("uri"),System.getenv("username"),System.getenv("password"));
            pstmt=con.prepareStatement(sql);
            if(reqObject.get("pathParameters")!=null){
                JSONObject pps = (JSONObject) reqObject.get("pathParameters");
                if (pps.get("notice_id") != null) {

                    noticeId= Integer.parseInt(String.valueOf(pps.get("notice_id")));
                    pstmt.setInt(1, noticeId);
                    pstmt.executeUpdate();
                }
            }


            responseBody.put("message","Item deleted");
            responseObject.put("statusCode", 200);
            responseObject.put("body", responseBody.toString());

        }catch (Exception e){
            responseObject.put("statusCode", 400);
            responseObject.put("error", e);
        }

        writer.write(responseObject.toString());
        reader.close();
        writer.close();
        close(con,pstmt,null);
    }


    private void close(Connection con, Statement stmt,ResultSet rs){
        if(rs!=null){
            try{
                rs.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(con!=null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
