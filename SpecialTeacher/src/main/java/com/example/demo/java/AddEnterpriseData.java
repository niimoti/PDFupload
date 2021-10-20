package com.example.demo.java;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddEnterpriseData {
    private String kName;
    private String day;
    private String contents;
    private String place;
    private String deadline;
    private String URL;
    private String explanation;
    private List<MultipartFile> tempfile;
}
