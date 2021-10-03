package com.example.sdc.service;


import com.example.sdc.exception.CustomizedRequestException;
import com.example.sdc.module.CreateIssue;
import com.example.sdc.module.IssueData;
import com.example.sdc.module.ClientData;
import com.example.sdc.module.Result;
import com.example.sdc.module.comment.CommentData;
import com.example.sdc.module.comment.RequestServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class HttpService {

    @Value("${service.name}")
    private String serviceName;
    @Value("${fbsds.url}")
    private String url;

    @Autowired
    RestTemplate restTemplate;


    public ResponseEntity<?> createIssue(IssueData issueData, String type, MultipartFile[] files) throws JsonProcessingException, JSONException {
        String description = issueData.getDescription();
        List<Result> markdownLists = allUploads(files, type);
        if(!markdownLists.isEmpty()){
            for(int i = 0; i < markdownLists.size(); i++)
             description = description + markdownLists.get(i).getResponseBody();
            issueData.setDescription(description);
        }

        String urlIssue = url + "/issue";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        CreateIssue issue = new CreateIssue();
        issue.setIssueData(issueData);
        ClientData clientData = new ClientData();
        clientData.setIssue_type(type);
        clientData.setService_name(serviceName);
        issue.setClientData(clientData);

        HttpEntity<CreateIssue> entity = new HttpEntity<CreateIssue>(issue, headers);

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        ResponseEntity<Result> response;
        try {
             response = restTemplate.postForEntity(urlIssue, entity, Result.class);
        }catch (HttpStatusCodeException e){
            throw new CustomizedRequestException(e.getMessage(), 9, e.getRawStatusCode());
        }
        return response;
    }
    public List<Result> allUploads(MultipartFile[] files, String issue_type){
        List<Result> markdownLists = Arrays.asList(files)
                                    .stream()
                                    .map(file -> {
                                        try {
                                            return uploadFile(file, issue_type);
                                        } catch (IOException e) {
                                            throw new CustomizedRequestException(e.getMessage(), 14);
                                        }
                                    })
                                    .collect(Collectors.toList());
        return markdownLists;
    }

    public Result uploadFile(MultipartFile file, String type) throws IOException {
        String urlUpload = url + "/uploadMultipleFiles";
//        byte[] bfile = file.getBytes();
//        String encodedText = Base64Utils.encodeToString(bfile);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        if(file.isEmpty()){
            throw new CustomizedRequestException("file empty", 15);
        }
        File ffile = convert(file);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(ffile));
//        body.add("bfile", encodedText);
//        body.add("nfile", file.getOriginalFilename());
        body.add("serviceName", serviceName);
        body.add("type", type);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);

        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(urlUpload, HttpMethod.POST, entity, String.class);
            ffile.delete();
        }catch (HttpStatusCodeException e){
            throw new CustomizedRequestException(e.getMessage(), 9, e.getRawStatusCode());
        }
        ObjectMapper mapper = new ObjectMapper();

        Result result = mapper.readValue(response.getBody(), Result.class);

        return result;
    }

    public Result sendComment(CommentData commentData, String type, MultipartFile[] files){

        String comment = commentData.getComment();
        List<Result> markdownLists = allUploads(files, type);
        if(!markdownLists.isEmpty()){
            for(int i = 0; i < markdownLists.size(); i++)
                comment = comment + markdownLists.get(i).getResponseBody();
            commentData.setComment(comment);
        }

        RequestServer requestServer = new RequestServer();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);
        requestServer.setIssue_type(type);
        requestServer.setService_name(serviceName);
        requestServer.setIid(commentData.getIid());
        requestServer.setComment(commentData.getComment());
        HttpEntity<RequestServer> entity = new HttpEntity<RequestServer>(requestServer, headers);
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        ResponseEntity<Result> response;
        try {
            response = restTemplate.exchange(url+"/comment", HttpMethod.POST, entity, Result.class);
        }catch (HttpStatusCodeException e){
            throw new CustomizedRequestException(e.getMessage(), 9, e.getRawStatusCode());
        }
        return response.getBody();
    }

    public Result issueReOpen(int id, String type){
        ClientData clientData = new ClientData();
        clientData.setService_name(serviceName);
        clientData.setIssue_type(type);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ClientData> entity = new HttpEntity<ClientData>(clientData, headers);
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        ResponseEntity<Result> response;
        try {
            response = restTemplate.exchange(url+"/"+id+"/issueReOpen", HttpMethod.PUT, entity, Result.class);
        }catch (HttpStatusCodeException e){
            throw new CustomizedRequestException(e.getMessage(), 20, e.getRawStatusCode());
        }
        return response.getBody();
    }

    public static File convert(MultipartFile file)
    {
        File convFile = new File("G:/Oqil/Projects/sdc/docFiles/" + file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convFile;

    }

}
