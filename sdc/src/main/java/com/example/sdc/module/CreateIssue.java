package com.example.sdc.module;

import com.fasterxml.jackson.annotation.JsonInclude;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CreateIssue {

//    @Schema(required = true, description = "Data of issue")
    @NotNull
    private IssueData issueData;

//    @Schema(required = true,  description = "Data of client")
    @NotNull
    private ClientData clientData;



}
