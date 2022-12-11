package com.project.integration.web.formmodel;

import com.project.integration.web.annotation.CheckFileExtension;
import com.project.integration.web.annotation.CheckFileSize;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderForm {
  @Size(min = 2, message = "Name is too short")
  @Size(max = 45, message = "Name is too long")
  String firstName;

  @Size(min = 4, message = "Surname is too short")
  @Size(max = 45, message = "Surname is too long")
  String lastName;

  @NotBlank String phone;
  @NotBlank String email;
  @NotNull @CheckFileExtension() @CheckFileSize() MultipartFile document;
}
