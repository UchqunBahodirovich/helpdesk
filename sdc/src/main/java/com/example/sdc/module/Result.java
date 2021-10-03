package com.example.sdc.module;

import com.fasterxml.jackson.annotation.JsonInclude;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Result {

//    @Schema(required = true,  description = "Code of error.")
    private int code;

//    @Schema(required = true,  description = "Message for client.")
    @NotNull
    private String msg;

    private Object responseBody;

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
