package com.background.video.recorder.camera.recorder.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.background.video.recorder.camera.recorder.model.Authentication;

import java.util.List;

@Dao
public interface AuthDao {

    @Insert
    void insertUser(Authentication authentication);


    @Update
    void updateUser(Authentication authentication);

    @Delete
    void delteUser(Authentication authentication);

    @Query("SELECT * FROM userAuth WHERE userPassword  =:password")
    boolean verifyUser(int password);

    @Query("SELECT * FROM userAuth WHERE userAnswer =:answer")
    boolean verifyAnswer(String answer);

    @Query("SELECT * FROM userAuth")
    LiveData<List<Authentication>> getAllUser();

    @Query(("DELETE FROM userAuth"))
    void deleteUser();

    @Query("SELECT COUNT(userId) FROM userAuth ")
    int getRowCount();

}
