package com.developer.krisi.tasker.model;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TaskViewModelFactory implements ViewModelProvider.Factory {
        private Application mApplication;
        private String projectId;


        public TaskViewModelFactory(Application application, String projectId) {
            mApplication = application;
            this.projectId = projectId;
        }


        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new TaskViewModel(mApplication, projectId);
        }
}
