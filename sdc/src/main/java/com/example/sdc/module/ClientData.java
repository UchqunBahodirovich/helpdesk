package com.example.sdc.module;

import com.fasterxml.jackson.annotation.JsonInclude;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ClientData {

//    @Schema(required = true,  description = "Issue turi Zayevka : 'A' Xat : 'L'.")
    @NotNull(message = "Issue_type cannot be empty")
    @NotBlank
    @Size(max = 1)
    private String issue_type;

    @NotNull(message = "Issue_type is empty")
    @NotBlank
    @Size(max = 20, message = "Service_name contains only 20 characters")
//    @Schema(required = true,  description = "Bank nomi katta harfda.")
    private String service_name;

}
