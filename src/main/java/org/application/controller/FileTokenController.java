package org.application.controller;

import org.application.constant.PathConstants;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PathConstants.AT + PathConstants.FILE_TOKEN_ROUTE)
public interface FileTokenController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> createPrivateToken (@RequestParam("title") String title,
                                               @RequestParam("duration") long duration);
}
