package com.background.video.recorder.camera.recorder.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.background.video.recorder.camera.recorder.repository.MediaFilesRepository;
import com.background.video.recorder.camera.recorder.model.Authentication;
import com.background.video.recorder.camera.recorder.model.MediaFiles;
import com.background.video.recorder.camera.recorder.util.constant.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaFilesViewModel extends AndroidViewModel {

    private MediaFilesRepository repository;
    private LiveData<List<MediaFiles>> allMediaFiles;
    private LiveData<List<MediaFiles>> allFavouriteMediaFiles;
    private LiveData<List<MediaFiles>> allLockedMediaFiles;
    public MutableLiveData<ArrayList<Bitmap>> videoThumbnails;
    private LiveData<List<Authentication>> getAllUser;
    private int rowCount;





    public MediaFilesViewModel(@NonNull Application application) {
        super(application);
        repository = new MediaFilesRepository(application);
        allMediaFiles = repository.getAllMediaFiles();

//        if (allMediaFiles.getValue() != null){
//            for (int i = 1; i < allMediaFiles.getValue().size(); i++) {
//                if (allMediaFiles.getValue().get(i).getName().equals(allMediaFiles.getValue().get(i - 1).getName())){
//                    allMediaFiles.getValue().remove(i);
//                }
//            }
//        }
//
        allFavouriteMediaFiles = repository.getAllFavouriteMediaFiles();
        allLockedMediaFiles = repository.getAllLockedMediaFiles();
        videoThumbnails = new MutableLiveData<>();
        getAllUser = repository.getAllUser();
        this.rowCount = repository.getRowCount();
    }

    public LiveData<List<MediaFiles>> getAllFavouriteMediaFiles() {
        return allFavouriteMediaFiles;
    }

    public LiveData<List<MediaFiles>> getAllLockedMediaFiles() {
        return allLockedMediaFiles;
    }

    public LiveData<List<MediaFiles>> getAllMediaFiles() {
        return allMediaFiles;
    }

    public int getRowCount(){
        return rowCount;
    }

    public void insertMediaFiles(MediaFiles mediaFiles) {
        repository.insertMediaFiles(mediaFiles);
    }

    public void deleteMediaFiles(MediaFiles mediaFiles) {
        repository.deleteMediaFiles(mediaFiles);
    }

    public void deleteAllMediaFiles() {
        repository.deleteAllMediaFiles();
    }

    public void updateFavFile(int fileType, String fileName){
        repository.updateMediaFile(fileType,fileName);
    }
    public void renameFile(String newName , String oldName){
        repository.renameFile(newName , oldName);
    }
    public void deleteMultipleFiles(List<Integer> listIds){
        repository.deleteMultipleFile(listIds);
    }
    public void deleteFilesFromStorage(List<MediaFiles> list){
        List<MediaFiles> deletionList = new ArrayList<>();
        deletionList.addAll(list);
        repository.deleteFilesFromStorage(deletionList);
    }
    public void updateMultipleFiles(List<String> nameList,int type){
        repository.updateMultipleFiles(type,nameList);
    }

    // auth user operations

    public boolean verifyUser(int password) {
        return repository.verifyUser(password);
    }

    public LiveData<List<Authentication>> getGetAllUser() {
        return getAllUser;
    }

    public boolean verifyAnswer(String answer) {
        return repository.verifyAnswer(answer);
    }

    public void updateUser(Authentication authentication) {
        repository.updateUser(authentication);
    }

    public void deleteUser(Authentication authentication) {
        repository.deleteUser(authentication);
    }

    public void insertUser(Authentication authentication) {
        repository.insertUser(authentication);
    }
    public void deleteAllTable(){
        repository.deleteTable();
    }




}
