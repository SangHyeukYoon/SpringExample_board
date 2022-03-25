package com.yoon.example.springboot.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    MappingJackson2HttpMessageConverter httpMessageConverter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Data
    public class successJSON {
        final String status = "Success";
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String result = objectMapper.writeValueAsString(new successJSON());

        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        httpMessageConverter.write(result, MediaType.APPLICATION_JSON, outputMessage);
        response.setStatus(HttpStatus.OK.value());
    }

}
