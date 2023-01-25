package com.example.admin.bean;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImgBean {
    String email;
    String username;
    MultipartFile headerImg;
    MultipartFile[] photos;
}
