package qna.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();


    public void add(Answer answer){
        this.answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }


    public List<DeleteHistory> delete(User loginUser){
        ArrayList<DeleteHistory> deleteHistories = new ArrayList<DeleteHistory>();

        for (Answer answer : answers) {
            answer.checkOwnerAndDelete(loginUser);
            }
        return deleteHistories;
    }


}
