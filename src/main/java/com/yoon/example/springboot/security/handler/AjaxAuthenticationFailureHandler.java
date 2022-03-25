package com.yoon.example.springboot.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    MappingJackson2HttpMessageConverter httpMessageConverter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Data
    public class failureJSON {
        final String status = "failure";
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String result = objectMapper.writeValueAsString(new failureJSON());

        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        httpMessageConverter.write(result, MediaType.APPLICATION_JSON, outputMessage);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
