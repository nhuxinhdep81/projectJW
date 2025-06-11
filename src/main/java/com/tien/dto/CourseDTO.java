package com.tien.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Integer id;
    private String name;
    private Integer duration;
    private String instructor;
    private LocalDate createAt;
    private String image;
    private MultipartFile imageFile;
}
