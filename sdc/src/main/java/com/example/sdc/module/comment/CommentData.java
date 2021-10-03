package com.example.sdc.module.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CommentData {

//        @Schema(required = true,  description = "Id of issue")
        @NotNull
        private int iid;

//        @Schema(required = true,  description = "Comment")
        @NotNull
        private String comment;
}
