package lambda.project;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.sql.*;

public class ProjectLambda implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parse = new JSONParser();
        JSONObject responseObject = new JSONObject();
        JSONObject responseBody = new JSONObject();

        String sql = "insert into PROJECT(board_id, login_id, title, members, descriptions) values (null , ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql2="INSERT INTO PHOTO VALUE (NULL,NULL,LAST_INSERT_ID(),?,?)";
        PreparedStatement pstmt2=null;

        Integer boardId=null;//내가 세팅?
        String loginId=null;
        String title;
        String member;
        String description;
        Integer check=null;

        try{
            JSONObject reqObject = (JSONObject) parse.parse(reader);

            con= DriverManager.getConnection(System.getenv("uri"),System.getenv("username"),System.getenv("password"));
            pstmt = con.prepareStatement(sql);
            pstmt2 = con.prepareStatement(sql2);
            if (reqObject.get("pathParameters") != null) {
                JSONObject pps = (JSONObject) reqObject.get("pathParameters");
                if (pps.get("title") != null) {

                    loginId=String.valueOf(pps.get("login_id"));
                    pstmt.setString(1,loginId);
                    title= String.valueOf(pps.get("title"));
                    pstmt.setString(2,title);
                    member= String.valueOf(pps.get("member"));
                    pstmt.setString(3,member);
                    description= String.valueOf(pps.get("description"));
                    pstmt.setString(4,description);
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
                if (qps.get("board_id") != null) {

                    loginId=String.valueOf(qps.get("login_id"));
                    pstmt.setString(1,loginId);
                    title= String.valueOf(qps.get("title"));
                    pstmt.setString(2,title);
                    member= String.valueOf(qps.get("member"));
                    pstmt.setString(3,member);
                    description= String.valueOf(qps.get("description"));
                    pstmt.setString(4,description);
                    check=pstmt.executeUpdate();
                }
                if(qps.get("file_type")!=null){
                    pstmt2.setString(1, (String) qps.get("file_type"));
                    pstmt2.setString(2, (String) qps.get("file_name"));
                    pstmt2.executeUpdate();
                }
            }
            if (check.equals(1)) {
                Project project = new Project(pstmt.toString());
                responseBody.put("project", project);
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

        String sql = "update PROJECT set title=?, members=?, descriptions=? where board_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql2 = "update PHOTO set file_type=?,file_name=? where notice_id=?";
        PreparedStatement pstmt2=null;

        String fileType;
        String fileName;
        Integer boardId=null;
        String title;
        String member;
        String description;

        try{
            JSONObject reqObject = (JSONObject) parse.parse(reader);

            //mysql uri,username,password lambda에 환경변수에 넣어야한다.
            con=DriverManager.getConnection(System.getenv("uri"),System.getenv("username"),System.getenv("password"));
            pstmt = con.prepareStatement(sql);
            pstmt2 = con.prepareStatement(sql2);
            if (reqObject.get("body") != null) {

                Project project = new Project((String) reqObject.get("body"));
                Photo photo = new Photo((String) reqObject.get("body"));

                title = String.valueOf(project.getTitle());
                pstmt.setString(1, title);
                member = String.valueOf(project.getMember());
                pstmt.setString(2, member);
                description = String.valueOf(project.getDescription());
                pstmt.setString(3, description);
                boardId = Integer.parseInt(String.valueOf(project.getDescription()));
                pstmt.setInt(4, boardId);
                pstmt.executeUpdate();

                fileType = photo.getFileType();
                pstmt2.setString(1, fileType);
                fileName = photo.getFileName();
                pstmt2.setString(2, fileName);
                pstmt2.executeUpdate();

                //Product대신에 notice가 들어가야한다.
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

        String sql =  "delete from PROJECT where board_id=?";
        Connection con=null;
        PreparedStatement pstmt=null;

        Integer boardId=null;//내가 세팅?

        try{
            JSONObject reqObject = (JSONObject) parse.parse(reader);

            con= DriverManager.getConnection(System.getenv("uri"),System.getenv("username"),System.getenv("password"));
            pstmt=con.prepareStatement(sql);
            if(reqObject.get("pathParameters")!=null){
                JSONObject pps = (JSONObject) reqObject.get("pathParameters");
                if (pps.get("notice_id") != null) {

                    boardId= Integer.parseInt(String.valueOf(pps.get("board_id")));
                    pstmt.setInt(1, boardId);
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
    private void close(Connection con, Statement stmt, ResultSet rs){
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
