package com.test.demo.RestController;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.test.demo.service.StabilityAIService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private StabilityAIService stabilityAIService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateImage(@RequestParam("file") MultipartFile file, 
                                                @RequestParam("prompt") String prompt) {
        try {
            byte[] transformedImage = stabilityAIService.generateImageFromImage(file, prompt);
            if (transformedImage != null) {
                return ResponseEntity.status(HttpStatus.OK).body(transformedImage);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}