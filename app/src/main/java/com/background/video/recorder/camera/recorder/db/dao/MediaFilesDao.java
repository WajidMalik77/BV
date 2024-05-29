package com.background.video.recorder.camera.recorder.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.background.video.recorder.camera.recorder.model.MediaFiles;

import java.util.List;

@Dao
public interface MediaFilesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MediaFiles mediaFiles);

    @Update
    void update(MediaFiles mediaFiles);

    @Delete
    void delete(MediaFiles mediaFiles);

    @Query("DELETE FROM MediaFiles")
    void deleteAllAtOnce();

    @Query("SELECT * FROM MediaFiles WHERE extension = :extension ORDER BY id DESC")
    LiveData<List<MediaFiles>> allMediaFiles(String extension);

    @Query("SELECT * FROM MediaFiles WHERE type = :fav")
    LiveData<List<MediaFiles>> allFavoritesMediaFiles(int fav);

    @Query("SELECT * FROM MediaFiles WHERE locked = :locked")
    LiveData<List<MediaFiles>> lockedMediaFiles(boolean locked);

    @Query("UPDATE MediaFiles SET type = :type WHERE name = :name ")
    void updateMediaFileFav(int type, String name);

    @Query("DELETE FROM MediaFiles WHERE id IN (:idList)")
    void deleteMultipleFiles(List<Integer> idList);

    @Query("UPDATE MEDIAFILES SET type = :type WHERE name IN (:listName)")
    void updateMultipleFiles(int type, List<String> listName);

    @Query("UPDATE MediaFiles SET name =:newName WHERE name = :oldName ")
    void renameFile(String newName , String oldName);
}
