package rs.com.siriusxi.devtech.gw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Slf4j
public class GlobalExceptionHandlerFilter extends ZuulFilter {

    private static final String THROWABLE_KEY = "throwable";
    private static final String contentType = MediaType.APPLICATION_JSON_VALUE;
    private static final String generalMessage = "Something went wrong, please try again!";

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {

        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();

        if (INTERNAL_SERVER_ERROR.value() == ctx.getResponse().getStatus()) {
            modifyResponse(ctx, INTERNAL_SERVER_ERROR);
        }else if(NOT_FOUND.value() == ctx.getResponse().getStatus()){
            modifyResponse(ctx,"Resource not found!", NOT_FOUND);
        }else if (BAD_REQUEST.value() == ctx.getResponse().getStatus())
            modifyResponse(ctx,"Bad Request!", BAD_REQUEST);
        return null;
    }

    private void modifyResponse(RequestContext ctx, HttpStatus status) {
        modifyResponse(ctx,null, status);
    }

    private void modifyResponse(RequestContext ctx, String message, HttpStatus status) {
        // remove error code to prevent further error handling in follow up filters
        ctx.remove(THROWABLE_KEY);

        // Set response content type
        ctx.getResponse().setContentType(contentType);

        // populate context with new response values
        final String body = getPreviousResponseBody(ctx.getResponseDataStream());

        ctx.setResponseBody(Optional.ofNullable(message).orElseGet(()->{
            if(isJsonMessage(body))
                return body;
            else
                return generalMessage;
        }));

        // can set any error code as excepted
        ctx.setResponseStatusCode(status.value());

        log.error("Zuul failure detected: "+ ctx.getResponse().getStatus()+", Previous " +
                "message: "+ ctx.getResponseBody());
    }

    private String getPreviousResponseBody(InputStream is) {
        String message = null;
        try {
            message =  CharStreams.toString(new InputStreamReader(is,
                    StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.warn("Error reading body",e);
        } finally{
            try {
                is.close();
            } catch (IOException e) {
                log.error("Error closing response body",e);
            }
        }
        return message;
    }

    private boolean isJsonMessage(String message){
        boolean isValidJason = false;
        try{
            new ObjectMapper().readTree(message);
            isValidJason = true;

        } catch(IOException  e){
            log.error("Is not a valid JSON message",e);
        }
        return isValidJason;
    }
}
