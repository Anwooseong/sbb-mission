package com.mysite.domain.question.repository;

import com.mysite.domain.question.entity.Question;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    Question q1, q2;

//    @BeforeEach
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
    }

//    @AfterEach
//    void afterEach() {
//        questionRepository.deleteAll();
//    }

    @Test
    @Transactional
    void testAll() {
        setUp();
        List<Question> all = questionRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        Question q = all.get(0);
        Assertions.assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");
    }

    @Test
    @Transactional
    void testIsPresent() {
        setUp();
        Optional<Question> oq = this.questionRepository.findById(1);
        if(oq.isPresent()) {
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    @Test
    @Transactional
    void testFindBySubject() throws Exception {
        //given
        setUp();

        //when
        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");

        //then
        Assertions.assertThat(q.getId()).isNotNull();  // ID가 null이 아닌지 확인
    }

    @Test
    @Transactional
    public void testFindBySubjectLiek() throws Exception {
        //given
        setUp();

        //when
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        Question q = qList.get(0);

        //then
        assertEquals("sbb가 무엇인가요?", q.getSubject());
    }

    @Test
    @Transactional
    void testModifySubject() {
        //given
        setUp();
        //when
        List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
        System.out.println("qList.get(0).getId() = " + qList.get(0).getId());
        Optional<Question> oq = this.questionRepository.findById(qList.get(0).getId());
        assertTrue(oq.isPresent());
        Question q = oq.get();
        q.setSubject("수정된 제목");

        //then
        this.questionRepository.save(q);
        Assertions.assertThat(q.getSubject()).isEqualTo("수정된 제목");
    }

    @Test
    @Transactional
    void testCount() {
        setUp();
        assertEquals(2, this.questionRepository.count());
        Optional<Question> oq = this.questionRepository.findById(q1.getId());

        assertTrue(oq.isPresent());
        Question q = oq.get();
        this.questionRepository.delete(q);
        assertEquals(1, this.questionRepository.count());
    }
}