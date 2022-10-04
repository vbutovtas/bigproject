package com.project.integration.web.formmodel;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderForm {
   @NotBlank
   String firstName;
   @NotBlank
   String lastName;
   @NotBlank
   String phone;
   @NotBlank
   String email;
   @NotNull
   MultipartFile document;
}
