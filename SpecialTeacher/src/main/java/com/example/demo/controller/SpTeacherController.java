package com.example.demo.controller;

import com.example.demo.java.AddEnterpriseData;
import com.example.demo.model.InformationSessionModel;
import com.example.demo.repository.InformationSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class SpTeacherController {

    //-----------repository------------

    private final InformationSessionRepository informationSessionRepository;

    @Autowired
    public SpTeacherController(InformationSessionRepository informationSessionRepository) {this.informationSessionRepository = informationSessionRepository;}

    //---------------------------------

    //---------- fileSave--------------
    private String getExtension(String filename) {
        int dot = filename.lastIndexOf(".");
        if (dot > 0) {
            return filename.substring(dot).toLowerCase();
        }
        return "";
    }

    private String getUploadFileName(String fileName) {

        File file = new File(fileName);
        String basename = file.getName();
        String woext = basename.substring(0,basename.lastIndexOf('.'));

        return woext + "_" +
                DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
                        .format(LocalDateTime.now())
                + getExtension(fileName);
    }

    private void createDirectory() {
        Path path = Paths.get("C:/upload/files");
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (Exception e) {
                //????????????????????????
            }
        }
    }

    private String savefile(MultipartFile file) {
        String filename = getUploadFileName(file.getOriginalFilename());
        Path uploadfile = Paths.get("C:/upload/files/" + filename);
        try (var os = Files.newOutputStream(uploadfile, StandardOpenOption.CREATE)) {
            byte[] bytes = file.getBytes();
            os.write(bytes);

            return filename;
        } catch (IOException e) {
            //????????????????????????
            return "";
        }
    }

    private String savefiles(List<MultipartFile> multipartFiles) {
        createDirectory();
        String fileName = "";
        for (MultipartFile file : multipartFiles) {
           fileName = savefile(file);
        }
        return fileName;
    }
    //---------------------------------

    //????????????????????????????????????????????????????????????????????????????????????
    @RequestMapping("/spC")
    public String sp(){
        return "spC/spMenu";
    }
    //????????????????????????
    @GetMapping("addInformationSession")
    public String addIS() {
        return "spC/addInformationSession";
    }



    //?????????????????????????????????????????????model????????????????????????
    @PostMapping("/ISok")
    public String isok(
            Model model,
            AddEnterpriseData addData
            ) {

        if (addData.getTempfile() == null || addData.getTempfile().isEmpty()) {
            //?????????????????????
            //???????????????????????????????????????????????????(??????????`)
        }
        assert addData.getTempfile() != null;
        String fileName = savefiles(addData.getTempfile());

        InformationSessionModel spModel = new InformationSessionModel();
        spModel.setKName(addData.getKName());
        spModel.setDay(addData.getDay());
        spModel.setContents(addData.getContents());
        spModel.setPlace(addData.getPlace());
        spModel.setDeadline(addData.getDeadline());
        spModel.setURL(addData.getURL());
        spModel.setExplanation(addData.getExplanation());
        spModel.setTempfile(fileName);

        informationSessionRepository.save(spModel);

        return "spC/spMenu";
    }

    @GetMapping("ISSearch")
    public String ketsuANA(
            @ModelAttribute InformationSessionModel informationSessionModel,
            Model model
    ) {
        model.addAttribute("ketsu", informationSessionRepository.findAll());
        return "/spC/ISSearch";
    }

}
