package lambda.home;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomeLambda implements RequestStreamHandler {

    private static String dburl = System.getenv("uri");
    private static String dbUser = System.getenv("username");
    private static String dbpassword = System.getenv("password");


    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONParser parse = new JSONParser();
        JSONObject responseObject = new JSONObject();
        JSONObject responseBody = new JSONObject();

        //sql 전체 파일 저장하기+left로 사진하기
        String sql = "SELECT * FROM NOTICE AS N LEFT JOIN PHOTO AS P ON P.notice_id=N.notice_id";
        String sql2 = "SELECT * FROM PROJECT AS P LEFT JOIN PHOTO AS T ON T.board_id=P.board_id";
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs=null;
        ResultSet rs2=null;
        List<NoticePhoto> noticeList = new ArrayList<>();
        List<ProjectPhoto> projectList = new ArrayList<>();


        try{

            JSONObject reqObject = (JSONObject) parse.parse(reader);
            Class.forName("com.mysql.cj.jdbc.Driver");

            if(reqObject.get("pathParameters")!=null){
                con= DriverManager.getConnection(System.getenv("uri"),System.getenv("username"),System.getenv("password"));
                pstmt=con.prepareStatement(sql);
                pstmt2=con.prepareStatement(sql2);
                rs = pstmt.executeQuery();
                rs2 = pstmt2.executeQuery();
                JSONObject pps = (JSONObject) reqObject.get("pathParameters");
                
                if (pps.get("notice_id") == null) {
                    while(rs.next()){
                        NoticePhoto notice = new NoticePhoto(rs.getInt("notice_id"), rs.getString("login_id"),rs.getString("title"),rs.getString("descriptions"),rs.getDate("dates"),rs.getInt("photo_id"),rs.getString("file_type"),rs.getString("file_name"));
                        noticeList.add(notice);
                    }
                }
                if (pps.get("board_id") == null) {
                    while(rs2.next()){
                        ProjectPhoto project = new ProjectPhoto(rs2.getInt("board_id"), rs2.getString("login_id"),rs2.getString("title"),rs2.getString("members"),rs2.getString("descriptions"),rs2.getInt("photo_id"),rs2.getString("file_type"),rs2.getString("file_name"));
                        projectList.add(project);
                    }
                }
            }


            responseBody.put("noticeList",noticeList);
            responseBody.put("projectList",projectList);
            responseObject.put("statusCode", 200);
            responseObject.put("body", responseBody.toString());

        } catch (Exception e){
            e.printStackTrace();
        }
        writer.write(responseObject.toString());
        reader.close();
        writer.close();
        close(con,pstmt,rs);
        close(con,pstmt2,rs2);
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
