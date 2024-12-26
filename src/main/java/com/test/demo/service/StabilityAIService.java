package com.test.demo.service;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StabilityAIService {

    @Value("${spring.ai.stabilityai.api-key}")
    private String apiKey; 

    private static final String API_URL = "https://api.stability.ai/v2beta/stable-image/control/structure";

    public byte[] generateImageFromImage(MultipartFile imageFile, String prompt) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Accept", "image/*"); 
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", imageFile.getResource()); 
        body.add("prompt", prompt);                 
        body.add("width", "512");                   
        body.add("height", "512");                  

     
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        
        RestTemplate restTemplate = new RestTemplate();

        try {
        
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    API_URL, HttpMethod.POST, entity, byte[].class);

        
            if (response.getBody() != null) {
                byte[] imageBytes = response.getBody();
   
                saveImageToFile(imageBytes, "generated_image.png");
                return imageBytes;  
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void saveImageToFile(byte[] imageBytes, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageBytes);
            System.out.println("Image saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}