package qna.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import qna.exception.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Lob
    private String contents;

    @ManyToOne
    private User writer;

    private boolean deleted = false;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @Embedded
    private Answers answers = new Answers();

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }



    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    //연관관계 편의 메서드(양방향 참조 동기화)
    public void addAnswer(Answer answer) {
        answer.toQuestion(this); //Answer -> Question
        answers.add(answer);  //Questions -> Answer
        //this : static 함수가 아니기 때문에 object.함수메서드로 불러와야 하는데 이 때 this가 object
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public Long getWriterId() {
        return writer.getId();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer.getId() +
                ", deleted=" + deleted +
                '}';
    }


    public void isQuestionLoginUser(User loninUser){
       if(!this.isOwner(loninUser)){
           throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
       }
    }

    public DeleteHistory delete (){
        this.deleted = true;
        return new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now());
    }

    public List<DeleteHistory> deleteQuestion(User loginUser){
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        Question question = this.writeBy(loginUser);

        question.isQuestionLoginUser(loginUser);


        deleteHistories.add(question.delete());


    }

//    public void deleteAnswers(User loginUser){
//        for (Answer answer : answers) {
//            answer.checkOwnerAndDelete(loginUser);
//        }

//    }

}
