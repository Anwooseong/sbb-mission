package com.mysite.domain.answer.repository;

import com.mysite.domain.answer.entity.Answer;
import com.mysite.domain.question.entity.Question;
import com.mysite.domain.question.repository.QuestionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AnswerRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    Question q1, q2;
    Answer a;

    void setUp() {
        q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장


        q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장

        a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(q2);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);

    }

    @Test
    @Transactional
    void saveAnswer() {
        setUp();
        Optional<Question> oq = this.questionRepository.findById(q1.getId());
        assertTrue(oq.isPresent());

        Answer findAnswer = answerRepository.findById(a.getId()).get();
        Assertions.assertThat(a).isEqualTo(findAnswer);
    }

    @Test
    @Transactional
    void confirmAnswer() {
        setUp();
        Optional<Answer> oa = this.answerRepository.findById(a.getId());
        assertTrue(oa.isPresent());
        Answer a = oa.get();
        assertEquals(2, a.getQuestion().getId());
    }

    @Test
    @Transactional
    void testJpa() {
        setUp();
        Optional<Question> oq = this.questionRepository.findById(q2.getId());
        assertTrue(oq.isPresent());
        Question q = oq.get();
        System.out.println("q.getId() = " + q.getId());

        List<Answer> answerList = q.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }
}