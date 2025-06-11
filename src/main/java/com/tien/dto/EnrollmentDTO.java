package com.tien.dto;

import com.tien.entity.Enrollment;
import com.tien.utils.EEnrollmentStt;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDTO {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private LocalDateTime registeredAt;
    private EEnrollmentStt status;
}
