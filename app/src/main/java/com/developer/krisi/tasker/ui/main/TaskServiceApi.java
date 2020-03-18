package com.developer.krisi.tasker.ui.main;

import com.developer.krisi.tasker.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskServiceApi {
    @GET("tasks")
    Call<List<Task>> getTasks();
}
