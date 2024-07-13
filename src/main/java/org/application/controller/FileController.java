package org.application.controller;

import org.application.constant.PathConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(PathConstants.AT + PathConstants.FILE_ROUTE)
public interface FileController {
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("description") String description);

    @GetMapping("/download/{token}")
    public ResponseEntity<String> downloadFile(@PathVariable String token);
}
