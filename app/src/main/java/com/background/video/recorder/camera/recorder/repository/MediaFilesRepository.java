package com.background.video.recorder.camera.recorder.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.background.video.recorder.camera.recorder.db.MediaFileDatabase;
import com.background.video.recorder.camera.recorder.db.dao.AuthDao;
import com.background.video.recorder.camera.recorder.db.dao.MediaFilesDao;
import com.background.video.recorder.camera.recorder.model.Authentication;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.util.constant.Constants;

import java.io.File;
import java.util.List;

public class MediaFilesRepository {
    private static final String TAG = "MediaFilesRepository";
    private MediaFilesDao mediaFilesDao;
    private AuthDao authDao;
    private LiveData<List<MediaFiles>> allMediaFiles;
    private LiveData<List<MediaFiles>> allFavouriteMediaFiles;
    private LiveData<List<MediaFiles>> allLockedMediaFiles;
    private LiveData<List<Authentication>> getAllUser;
    private List<MediaFiles> deletionList;
    private int rowCount;


    public MediaFilesRepository(Application application) {
        MediaFileDatabase mediaFilesDatabase = MediaFileDatabase.getInstance(application.getApplicationContext());
        this.mediaFilesDao = mediaFilesDatabase.MediaFilesDao();
        this.authDao = mediaFilesDatabase.userAuth();
        this.allMediaFiles = mediaFilesDao.allMediaFiles(Constants.VIDEO_EXTENSION);
        this.allFavouriteMediaFiles = mediaFilesDao.allFavoritesMediaFiles(Constants.MEDIA_TYPE_FAVOURITE);
        this.allLockedMediaFiles = mediaFilesDao.lockedMediaFiles(Constants.FILE_TYPE_LOCKED);
        this.getAllUser = authDao.getAllUser();
    }

    //mediaFiles Operations
    public void insertMediaFiles(MediaFiles mediaFiles) {
        new InsertMediaFilesAsyncTask(mediaFilesDao).execute(mediaFiles);
    }

    public void updateMediaFile(int fileType, String fileName) {
        new UpdateFavFileAsyncTask(mediaFilesDao, fileName, fileType).execute();
    }
    public void renameFile(String newName , String oldName){
        new RenameFileAsyncTask(mediaFilesDao , newName , oldName).execute();
    }

    public int getRowCount() {
        new GetRowCountAsync(authDao, new GetRowCountAsync.getRowListener() {
            @Override
            public void getRowCount(int row) {
                rowCount = row;
            }
        }).execute();

        return rowCount;
    }

    public void deleteTable() {
        new DeleteAllRow(authDao).execute();
    }

    public void deleteMediaFiles(MediaFiles mediaFiles) {

        File file = new File(mediaFiles.getPath());

        new DeleteMediaFilesAsyncTask(mediaFilesDao).execute(mediaFiles);
    }

    public void deleteFilesFromStorage(List<MediaFiles> list) {
        new DeleteFilesFromStorageAsyncTask(list).execute();
    }

    public void deleteAllMediaFiles() {
        new DeleteAllMediaFilesAsyncTask(mediaFilesDao).execute();
    }

    public LiveData<List<MediaFiles>> getAllFavouriteMediaFiles() {
        return mediaFilesDao.allFavoritesMediaFiles(Constants.MEDIA_TYPE_FAVOURITE);
    }

    public LiveData<List<MediaFiles>> getAllMediaFiles() {
        return mediaFilesDao.allMediaFiles(Constants.VIDEO_EXTENSION);
    }

    public LiveData<List<MediaFiles>> getAllLockedMediaFiles() {
        return mediaFilesDao.lockedMediaFiles(Constants.FILE_TYPE_LOCKED);
    }

    public void deleteMultipleFile(List<Integer> listIds) {
        new DeleteMultipleFileAsyncTask(mediaFilesDao, listIds).execute();
    }

    public void updateMultipleFiles(int type, List<String> listName) {
        new UpdateMultipleFileAsyncTask(mediaFilesDao, listName, type).execute();
    }
    //authUser operations

    public void insertUser(Authentication authentication) {
        new AddUserAsyncTask(authDao).execute(authentication);
    }

    public void deleteUser(Authentication authentication) {
        new DeleteUserAsyncTask(authDao).execute(authentication);
    }

    public void updateUser(Authentication authentication) {
        new UpdateUserAsyncTask(authDao).execute(authentication);
    }

    public LiveData<List<Authentication>> getAllUser() {
        return getAllUser;
    }

    public boolean verifyUser(int password) {
        return authDao.verifyUser(password);
    }

    public boolean verifyAnswer(String answer) {
        return authDao.verifyAnswer(answer);
    }


    private static class InsertMediaFilesAsyncTask extends AsyncTask<MediaFiles, Void, Void> {
        private MediaFilesDao mediaFilesDao;

        public InsertMediaFilesAsyncTask(MediaFilesDao mediaFilesDao) {
            this.mediaFilesDao = mediaFilesDao;
        }

        @Override
        protected Void doInBackground(MediaFiles... mediaFiles) {
            mediaFilesDao.insert(mediaFiles[0]);
            return null;
        }
    }

    private static class DeleteMediaFilesAsyncTask extends AsyncTask<MediaFiles, Void, Void> {
        private MediaFilesDao mediaFilesDao;

        public DeleteMediaFilesAsyncTask(MediaFilesDao mediaFilesDao) {
            this.mediaFilesDao = mediaFilesDao;
        }

        @Override
        protected Void doInBackground(MediaFiles... mediaFiles) {
            mediaFilesDao.delete(mediaFiles[0]);
            return null;
        }
    }

    private static class DeleteAllMediaFilesAsyncTask extends AsyncTask<Void, Void, Void> {
        private MediaFilesDao mediaFilesDao;

        public DeleteAllMediaFilesAsyncTask(MediaFilesDao mediaFilesDao) {
            this.mediaFilesDao = mediaFilesDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mediaFilesDao.deleteAllAtOnce();
            return null;
        }
    }

    // Authentication classes
    private static class AddUserAsyncTask extends AsyncTask<Authentication, Void, Void> {
        private AuthDao authDao;

        public AddUserAsyncTask(AuthDao authDao) {
            this.authDao = authDao;
        }

        @Override
        protected Void doInBackground(Authentication... authentications) {
            authDao.insertUser(authentications[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<Authentication, Void, Void> {
        private AuthDao authDao;

        public UpdateUserAsyncTask(AuthDao authDao) {
            this.authDao = authDao;
        }

        @Override
        protected Void doInBackground(Authentication... authentications) {
            authDao.updateUser(authentications[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<Authentication, Void, Void> {
        private AuthDao authDao;

        public DeleteUserAsyncTask(AuthDao authDao) {
            this.authDao = authDao;
        }

        @Override
        protected Void doInBackground(Authentication... authentications) {
            authDao.delteUser(authentications[0]);
            return null;
        }
    }

    private static class RenameFileAsyncTask extends AsyncTask<Void , Void , Void>{
        private String newName, oldName;
        private MediaFilesDao mediaFilesDao;

        public RenameFileAsyncTask(MediaFilesDao mediaFilesDao, String newName, String oldName) {
            this.newName = newName;
            this.mediaFilesDao = mediaFilesDao;
            this.oldName = oldName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mediaFilesDao.renameFile(newName , oldName);
            return null;
        }
    }

    private static class UpdateFavFileAsyncTask extends AsyncTask<Void, Void, Void> {
        private MediaFilesDao mediaFilesDao;
        private String fileName;
        private int fileType;


        public UpdateFavFileAsyncTask(MediaFilesDao mediaFilesDao, String fileName, int fileType) {
            this.mediaFilesDao = mediaFilesDao;
            this.fileName = fileName;
            this.fileType = fileType;
        }

        @Override
        protected Void doInBackground(Void... authentications) {
            mediaFilesDao.updateMediaFileFav(fileType, fileName);

            return null;
        }
    }

    private static class DeleteMultipleFileAsyncTask extends AsyncTask<Void, Void, Void> {
        private MediaFilesDao mediaFilesDao;
        private List<Integer> idList;

        public DeleteMultipleFileAsyncTask(MediaFilesDao mediaFilesDao, List<Integer> idList) {
            this.mediaFilesDao = mediaFilesDao;
            this.idList = idList;

        }

        @Override
        protected Void doInBackground(Void... authentications) {
            mediaFilesDao.deleteMultipleFiles(idList);
            return null;
        }
    }

    private class DeleteFilesFromStorageAsyncTask extends AsyncTask<Void, Void, Void> {
        private List<MediaFiles> files;
        private boolean deleted;

        public DeleteFilesFromStorageAsyncTask(List<MediaFiles> files) {
            this.files = files;
        }

        @Override
        protected Void doInBackground(Void... lists) {
            for (int i = 0; i < files.size(); i++) {
                File file = new File(files.get(i).getPath());
                try {
                    deleted = file.delete();
                } catch (Exception e) {
                    e.getCause();
                }
            }

            Log.e(TAG, "doInBackground: ");

            return null;
        }


        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Log.e(TAG, "onPostExecute: " + deleted);
        }
    }

    private static class UpdateMultipleFileAsyncTask extends AsyncTask<Void, Void, Void> {
        private MediaFilesDao mediaFilesDao;
        private List<String> listName;
        private int fileType;

        public UpdateMultipleFileAsyncTask(MediaFilesDao mediaFilesDao, List<String> listName, int fileType) {
            this.mediaFilesDao = mediaFilesDao;
            this.listName = listName;
            this.fileType = fileType;
        }

        @Override
        protected Void doInBackground(Void... authentications) {
            mediaFilesDao.updateMultipleFiles(fileType, listName);
            return null;
        }
    }

    private static class GetRowCountAsync extends AsyncTask<Void, Void, Void> {
        private AuthDao authDao;
        int row;
        private getRowListener getRowListener;

        public interface getRowListener {
            void getRowCount(int row);
        }

        public GetRowCountAsync(AuthDao authDao, GetRowCountAsync.getRowListener getRowListener) {
            this.authDao = authDao;
            this.getRowListener = getRowListener;
        }

        @Override
        protected Void doInBackground(Void... authentications) {
            row = authDao.getRowCount();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            getRowListener.getRowCount(row);
        }
    }

    private static class DeleteAllRow extends AsyncTask<Void, Void, Void> {
        private AuthDao authDao;


        public DeleteAllRow(AuthDao authDa) {
            this.authDao = authDa;

        }

        @Override
        protected Void doInBackground(Void... authentications) {
            authDao.deleteUser();
            return null;
        }


    }
}
