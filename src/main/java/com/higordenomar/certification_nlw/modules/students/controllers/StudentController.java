package com.higordenomar.certification_nlw.modules.students.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.higordenomar.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.higordenomar.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.higordenomar.certification_nlw.modules.students.services.StudentCertificationAnswersService;
import com.higordenomar.certification_nlw.modules.students.services.VerifyIfHasCertificationService;

@RestController
@RequestMapping("/students")
public class StudentController {
  @Autowired
  private VerifyIfHasCertificationService verifyIfHasCertificationService;

  @Autowired
  private StudentCertificationAnswersService studentCertificationAnswersService;

  @PostMapping("verifyIfHasCertification")
  public String verifyIfHasCertification(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO) {
    var result = this.verifyIfHasCertificationService.execute(verifyHasCertificationDTO);

    if (result) {
      return "Usuário já fez a prova";
    }

    System.out.println(verifyHasCertificationDTO);
    return "Usuário pode fazer a prova";
  }

  @PostMapping("certification/answer")
  public ResponseEntity<Object> certificationAnswer(
      @RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO) {
    try {
      var result = studentCertificationAnswersService.execute(studentCertificationAnswerDTO);

      return ResponseEntity.ok().body(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
