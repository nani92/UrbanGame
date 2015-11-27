package com.example.nataliajastrzebska.urbangame;

import java.util.ArrayList;

/**
 * Created by Przemysław on 2015-10-20.
 */
public class GameTask {

    private String question;
    private ArrayList<String> answers;
    private int correctAnswer;
    private String answer;
    private TaskType taskType;
    private String taskTypeString;

    public GameTask() {
        answers = new ArrayList<String>();
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public String getTaskTypeString() {
        return taskTypeString;
    }

    public void setTaskType(String taskTypeString) {
        this.taskTypeString = taskTypeString;
        if(taskTypeString.equals("abcd")){
            taskType = TaskType.ABCD;
            return;
        }
        if(taskTypeString.equals("ThinkAndAnswer")){
            taskType = TaskType.THINKandANSWER;
            return;
        }
    }

    @Override
    public String toString() {
        return "GameTask{" +
                "question='" + question + '\'' +
                ", answers=" + answers +
                ", correctAnswer=" + correctAnswer +
                '}';
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
