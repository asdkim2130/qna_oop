package qna.domain;

import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.DORAEMON);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SPONGEBOB);

    @Test
    public void delete_다른_사람이_올린_질문(){
        //when
        Q1.isOwner(UserTest.DORAEMON);

        assertThatThrownBy(() -> Q1.isQuestionLoginUser(UserTest.SPONGEBOB))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void question_delete_성공(){
        //when
        Q1.isOwner(UserTest.DORAEMON);

        //given
        DeleteHistory question삭제 = Q1.delete();
        DeleteHistory question삭제결과 = new DeleteHistory(ContentType.QUESTION, Q1.getId(), Q1.getWriter(), LocalDateTime.now());

        assertThat(question삭제).isEqualTo(question삭제결과);
    }











}

