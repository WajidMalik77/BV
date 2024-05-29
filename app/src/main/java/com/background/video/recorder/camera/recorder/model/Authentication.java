package com.background.video.recorder.camera.recorder.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "userAuth")
public class Authentication {
    @PrimaryKey (autoGenerate = true)
    private int userId ;

    private int userPassword ;

    private String userAnswer;





    public Authentication(int userPassword, String userAnswer) {
        setUserPassword(userPassword);
        setUserAnswer(userAnswer);
    }





    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserPassword() {
        return userPassword;
    }



    public int getUserId() {
        return userId;
    }

    public void setUserPassword(int userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}

