package com.example.sdc.controller;

import com.example.sdc.module.CommentResponse;
import com.example.sdc.module.IssueData;
import com.example.sdc.module.Result;
import com.example.sdc.module.comment.CommentData;
import com.example.sdc.service.HttpService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Tag(name = "Service Desk Client", description = "")
@RestController
@RequestMapping("/sdc")
@Validated
public class SdcController {

    @Autowired
    private HttpService service;

    private static final Logger logger = LoggerFactory.getLogger(SdcController.class);

    @Operation(
            summary = "Create issue.",
            description = "IssueType Aplication(A) or Letter(L) contains only one character",
            tags = "Service Desk Client"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Issue successfully created case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = IssueData.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Some of the required fields are not filled or invalid request body case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Unknown internal server error case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    )
            }
    )

    @PostMapping(value = "/{issueType}/issue", produces = {"application/json"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createIssue(   @Parameter(name = "title", required = true)
                                         @RequestParam(name = "title") String issue_title,
                                             @Parameter(name = "description")
                                         @RequestParam(name = "description", required = false) String issue_description,
                                             @Parameter(description = "Upload files")
                                         @RequestParam(name= "files", required = false) MultipartFile[] files,
                                             @Parameter(name = "issueType",description = "Application: A or Letter: L", required = true) @Pattern(regexp = "^[A,L]{1}")
                                         @PathVariable(name = "issueType") String issueType) throws JsonProcessingException, JSONException {
        IssueData issuedata = new IssueData(issue_title, issue_description);
        ResponseEntity<?> responseEntity = service.createIssue(issuedata, issueType, files);
        return responseEntity;
    }

//        @PostMapping(value = "/{issueType}/issue1", produces = {"application/json"}, consumes = "application/json")
//    public ResponseEntity<?> createIssue1( @Parameter(description = "Issue data", required = true)
//                                             @RequestBody() IssueData issuedata,
//                                          @Parameter(name = "issueType",description = "Application: A or Letter: L", required = true) @Pattern(regexp = "^[A,L]{1}")
//                                             @PathVariable(name = "issueType") String issueType) throws JsonProcessingException, JSONException {
//        ResponseEntity<?> responseEntity = service.createIssue(issuedata, issueType);
//        return responseEntity;
//    }
    @Operation(
            summary = "Create new comment to send",
            description = "Issue_type Aplication:A or Letter:L contains only one character",
            tags = "Service Desk Client"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Issue successfully created case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Some of the required fields are not filled or invalid request body case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Unknown internal server error case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    )
            }
    )
    @PostMapping(value = "/{issueType}/comment", consumes = "multipart/form-data",  produces = "application/json")
    //checked
    public Result newComment(    @Parameter(name = "issueId", required = true)
                             @RequestParam(name = "issueId") String issue_id,
                                 @Parameter(name = "comment", required = true)
                             @RequestParam(name = "comment") String issue_comment,
                                  @Parameter(name = "files", description = "Upload files", required = false)
                             @RequestPart(name = "files") MultipartFile[] files,
                                  @Parameter(name = "issueType",description = "Application: A or Letter: L", required = true) @Pattern(regexp = "^[A,L]{1}") @NotBlank @NotNull
                             @PathVariable() String issueType) {
        CommentData commentData = new CommentData(Integer.parseInt(issue_id), issue_comment);
        return service.sendComment(commentData, issueType, files);
    }

    @Operation(
            summary = "Receive the message.",
            description = "Message to the client.",
            tags = "Service Desk Client"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Issue successfully created case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Some of the required fields are not filled or invalid request body case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Unknown internal server error case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    )
            }
    )

    @PostMapping(value = "/commentEvent", produces = {"application/json"}, consumes = "application/json")
    public String getComment(@Parameter(description = "Resived comment", required = true ) @NotNull
                                 @RequestBody CommentResponse comment){
        logger.info("Gitlabda yozilgan comment: " + comment.getComment());
        return "Comment resived";
    }

    @Operation(
            summary = "Information of issue state",
            description = "Issue_id for client",
            tags = "Service Desk Client"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Issue successfully created case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Some of the required fields are not filled or invalid request body case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Unknown internal server error case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    )
            }
    )

    @PutMapping(value = "/{issueId}/issueEvent", produces = "application/json", consumes = "application/json")
    public Result issueEvent(@Valid @Parameter(description = "Result data", required = true)
                                 @RequestBody Result result,
                             @Parameter(name = "issueId", description = "Issue_id")  @NotNull
                                @PathVariable String issueId){
        logger.info("Issue events:" + result.getMsg());
        Result result1 = new Result();
        result1.setCode(0);;
        return result1;
    }

    @Operation(
            summary = "Issue state",
            description = "Issue state is open or close",
            tags = "Service Desk Client"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Issue successfully created case."
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Some of the required fields are not filled or invalid request body case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Unknown internal server error case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    )
            }
    )

    @PutMapping(value = "{issueType}/{issueId}/reOpen", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result issueReopen(@Parameter(name = "issueType",description = "Application: A or Letter: L", required = true) @Pattern(regexp = "^[A,L]{1}") @NotBlank @NotNull
                                  @PathVariable String issueType,
                              @Parameter(name = "issueId", description = "Issue_id")  @NotNull
                                  @PathVariable int issueId){
        return service.issueReOpen(issueId, issueType);
    }

    @Operation(
            summary = "Issue state",
            description = "Issue state is open or close",
            tags = "Service Desk Client"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Issue successfully created case."
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Some of the required fields are not filled or invalid request body case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Unknown internal server error case.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class)
                            )
                    )
            }
    )
    @PutMapping(value = "allEvent", produces = MediaType.APPLICATION_JSON_VALUE)
    public String allEvent( @RequestBody List<String>  array){

        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i));
        }
        return "resived";
    }
//    @Operation(
//            summary = "Upload Multipart files",
//            description = "Issue_type Aplication:A or Letter:L contains only one character",
//            tags = "Service Desk Client"
//    )
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200",
//                            description = "Issue successfully created case.",
//                            content = @Content(
//                                    mediaType = "application/json",
//                                    schema = @Schema(implementation = Result.class)
//                            )
//                    ),
//                    @ApiResponse(responseCode = "400",
//                            description = "Some of the required fields are not filled or invalid request body case.",
//                            content = @Content(
//                                    mediaType = "application/json",
//                                    schema = @Schema(implementation = MultipartFile.class)
//                            )
//                    ),
//                    @ApiResponse(responseCode = "500",
//                            description = "Unknown internal server error case.",
//                            content = @Content(
//                                    mediaType = "application/json",
//                                    schema = @Schema(implementation = Result.class)
//                            )
//                    )
//            }
//    )
//    @PostMapping(value = "/{issueType}/multipleFiles", consumes = { "multipart/form-data" }, produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<Result> uploadMultipleFiles(@Parameter(description = "Upload files", required = true)
//                                            @RequestParam("files") MultipartFile[] files,
//                                            @Parameter(name = "issueType",description = "Application: A or Letter: L", required = true) @Pattern(regexp = "^[A,L]{1}", message = "Issue_type Aplication(A) or Letter(L) contains only one character") @NotBlank @NotNull
//                                            @PathVariable String issueType) {
//
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> {
//                    try {
//                        return service.uploadFile(file, issueType);
//                    } catch (IOException e) {
//                        throw new CustomizedRequestException(e.getMessage(), 14);
//                    }
//                })
//                .collect(Collectors.toList());
//    }
}
