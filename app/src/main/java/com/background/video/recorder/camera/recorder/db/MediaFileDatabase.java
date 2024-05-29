package com.background.video.recorder.camera.recorder.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.background.video.recorder.camera.recorder.db.dao.AuthDao;
import com.background.video.recorder.camera.recorder.db.dao.MediaFilesDao;
import com.background.video.recorder.camera.recorder.model.Authentication;
import com.background.video.recorder.camera.recorder.model.MediaFiles;

@Database(entities = {MediaFiles.class, Authentication.class}, version = 1)
public abstract class MediaFileDatabase extends RoomDatabase {
    private static MediaFileDatabase instance;

    public abstract MediaFilesDao MediaFilesDao();

    public abstract AuthDao userAuth();



    public static synchronized MediaFileDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MediaFileDatabase.class
                    , "Background Video Recorder Database")
                    .build();
        }
        return instance;
    }


}

