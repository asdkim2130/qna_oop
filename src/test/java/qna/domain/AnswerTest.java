package qna.domain;

import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

import java.time.LocalDateTime;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.DORAEMON, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SPONGEBOB, QuestionTest.Q1, "Answers Contents2");

    @Test
    public void 다른_사람이_쓴_답변이_존재(){
        //when
        A1.isOwner(UserTest.DORAEMON);

        assertThatThrownBy(() -> A1.isAnswerLoginUser(UserTest.SPONGEBOB))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void answer_삭제_성공(){
        //when
        A1.isOwner(UserTest.DORAEMON);

        //given
        DeleteHistory answer삭제 = A1.delete();
        DeleteHistory answer삭제결과 = new DeleteHistory(ContentType.ANSWER, A1.getId(), A1.getWriter(), LocalDateTime.now());

        assertThat(answer삭제).isEqualTo(answer삭제결과);

    }
}
