package com.example.sdc.module;

//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor

public class IssueData {

//    @Schema(required = true,  description = "Issue name")
    @NotNull(message = "Title cannot be empty.")
    @Size(max = 30 , message = "Title conteins only 30 characters")
    private String title;

//    @Schema(required = true,  description = "Issue discription")
    private String description;
}
