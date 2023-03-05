package lambda.login;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public class LoginLambda implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parse = new JSONParser();
        JSONObject responseObject = new JSONObject();
        JSONObject responseBody = new JSONObject();

        String token=null;

        String loginId=null;
        String password = null;

        try{
            JSONObject reqObject = (JSONObject) parse.parse(reader);

            if (reqObject.get("pathParameters") != null) {
                JSONObject pps = (JSONObject) reqObject.get("pathParameters");
                if (pps.get("login_id") != null) {

                    if(pps.get("login_id").equals(System.getenv("loginId")) && pps.get("password").equals(System.getenv("password"))){
                        loginId=(String) pps.get("login_id");
                        password=(String) pps.get("password");
                        Security security = new Security();
                        token = security.generateToken(loginId, password);
                    }
                }
            }
            else if (reqObject.get("queryStringParameters") != null) {
                JSONObject qps = (JSONObject) reqObject.get("queryStringParameters");
                if (qps.get("login_id") != null) {

                    if(qps.get("login_id").equals(System.getenv("loginId")) && qps.get("password").equals(System.getenv("password"))){
                        loginId=(String) qps.get("login_id");
                        password=(String) qps.get("password");
                        Security security = new Security();
                        token = security.generateToken(loginId, password);
                    }
                }
            }

            if (token!=null) {
                //#TODO Product대신에 member가 들어가야한다.토큰을 여기서 줘야할듯
                //pstmt.setString(1,토큰값);
                responseObject.put("token",token);
                responseObject.put("statusCode", 200);
            }else {
                responseBody.put("message", "잘못된 정보입니다.");
                responseObject.put("statusCode", 400);
            }
            responseObject.put("body", responseBody.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.write(responseObject.toString());
        reader.close();
        writer.close();
    }

    public void handleOutRequest(InputStream input, OutputStream output, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parse = new JSONParser();
        JSONObject responseObject = new JSONObject();
        JSONObject responseBody = new JSONObject();

        try{
            JSONObject reqObject = (JSONObject) parse.parse(reader);

            if (reqObject.get("pathParameters") != null) {
                JSONObject pps = (JSONObject) reqObject.get("pathParameters");
                if (pps.get("login_id") != null) {
                    pps.remove("login_id");
                    pps.remove("password");
                    pps.remove("token");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.write(responseObject.toString());
        reader.close();
        writer.close();
    }
}
