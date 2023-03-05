package lambda.security;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LambdaAuthorizer implements RequestHandler<APIGatewayProxyRequestEvent, Response> {

    public Response handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        Map<String, String> headers = event.getHeaders();
        String authorizationToken = headers.get("Authorization");
        String auth = "Deny";
        String sub=null;
        String secretKey = System.getenv("secretKey");
        byte[] decode = Decoders.BASE64.decode(secretKey);

        sub = String.valueOf(Jwts.parserBuilder()
                .setSigningKey(decode)
                .build()
                .parseClaimsJws(authorizationToken)
                .getBody());
        if (sub != null) {

            auth = "Allow";

        }
        Map<String, String> ctx = new HashMap<String, String>();
        ctx.put("sub", sub);

        APIGatewayProxyRequestEvent.ProxyRequestContext proxyContext = event.getRequestContext();
        APIGatewayProxyRequestEvent.RequestIdentity identity = proxyContext.getIdentity();


        String arn = String.format("arn:aws:execute-api:%s:%s:%s/%s/%s/%s",System.getenv("AWS_REGION"), proxyContext.getAccountId(),
        proxyContext.getApiId(), proxyContext.getStage(), proxyContext.getHttpMethod(), "*");

        Statement statement = Statement.builder().effect(auth).resource(arn).build();

        PolicyDocument policyDocument = PolicyDocument.builder().statements(Collections.singletonList(statement))
        .build();

        return Response.builder().principalId(identity.getAccountId()).policyDocument(policyDocument)
                .context(ctx).build();
    }
}