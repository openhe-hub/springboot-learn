package com.example.admin.controller;

import com.example.admin.bean.ImgBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@Slf4j
public class FormController {
    @GetMapping("/form_layouts")
    public String form_layouts() {
        return "form/form_layouts";
    }

    @PostMapping("/upload")
    public String upload(ImgBean imgBean) throws IOException {
        MultipartFile headerImg = imgBean.getHeaderImg();
        MultipartFile[] photos = imgBean.getPhotos();
        // save single file
        if (!headerImg.isEmpty()) {
            headerImg.transferTo(new File("D:\\" + headerImg.getOriginalFilename()));
        }
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                photo.transferTo(new File("D:\\group\\" + photo.getOriginalFilename()));
            }
        }
        return "main";
    }
}
