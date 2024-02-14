package com.higordenomar.certification_nlw.modules.students.services;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.higordenomar.certification_nlw.modules.questions.repositories.QuestionRepository;
import com.higordenomar.certification_nlw.modules.students.dto.StudentCertificationAnswerDTO;
import com.higordenomar.certification_nlw.modules.students.dto.VerifyHasCertificationDTO;
import com.higordenomar.certification_nlw.modules.students.entities.AnswerCertificationsEntity;
import com.higordenomar.certification_nlw.modules.students.entities.CertificationStudentEntity;
import com.higordenomar.certification_nlw.modules.students.entities.StudentEntity;
import com.higordenomar.certification_nlw.modules.students.repositories.CertificationStudentRepository;
import com.higordenomar.certification_nlw.modules.students.repositories.StudentRepository;

@Service
public class StudentCertificationAnswersService {
  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private CertificationStudentRepository certificationStudentRepository;

  @Autowired
  private VerifyIfHasCertificationService verifyIfHasCertificationService;

  public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {
    var hasCertification = verifyIfHasCertificationService.execute(
        new VerifyHasCertificationDTO(dto.getEmail(), dto.getTechnology()));

    if (hasCertification) {
      throw new Exception("Você já possui esta certificação.");
    }

    var questionsEntities = questionRepository.findByTechnology(dto.getTechnology());
    var answersCertifications = new ArrayList<AnswerCertificationsEntity>();

    AtomicInteger correctAnswers = new AtomicInteger(0);

    dto.getQuestionsAnswers()
        .stream().forEach(questionAnswer -> {
          var questionEntity = questionsEntities
              .stream()
              .filter(question -> question.getId().equals(questionAnswer.getQuestionID()))
              .findFirst().get();

          var correctAlternative = questionEntity.getAlternatives().stream()
              .filter(alternative -> alternative.isCorrect()).findFirst().get();

          if (correctAlternative.getId().equals(questionAnswer.getAlternativeID())) {
            questionAnswer.setCorrect(true);
            correctAnswers.incrementAndGet();
          } else {
            questionAnswer.setCorrect(false);
          }

          var answersCertificationsEntity = AnswerCertificationsEntity.builder()
              .answerID(questionAnswer.getAlternativeID())
              .questionID(questionAnswer.getQuestionID())
              .isCorrect(questionAnswer.isCorrect()).build();

          answersCertifications.add(answersCertificationsEntity);
        });

    var student = studentRepository.findByEmail(dto.getEmail());
    UUID studentID;

    if (student.isEmpty()) {
      var newStudent = StudentEntity.builder().email(dto.getEmail()).build();
      var studentCreated = studentRepository.save(newStudent);
      studentID = studentCreated.getId();
    } else {
      studentID = student.get().getId();
    }

    var certificationStudentEntity = CertificationStudentEntity.builder()
        .technology(dto.getTechnology())
        .studentID(studentID)
        .grade(correctAnswers.get())
        .build();

    var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

    answersCertifications.stream().forEach(certification -> {
      certification.setCertificationID(certificationStudentEntity.getId());
      certification.setCertificationStudentEntity(certificationStudentEntity);
    });

    certificationStudentEntity.setAnswerCertificationsEntity(answersCertifications);

    certificationStudentRepository.save(certificationStudentEntity);

    return certificationStudentCreated;
  }
}
