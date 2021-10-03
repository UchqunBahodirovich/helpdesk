package com.example.sdc.module.comment;

import lombok.Data;

@Data
public class RequestServer {
    private int iid;
    private String comment;
    private String service_name;
    private String  issue_type;

}
