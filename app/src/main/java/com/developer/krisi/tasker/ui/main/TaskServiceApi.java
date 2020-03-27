package com.developer.krisi.tasker.ui.main;

import com.developer.krisi.tasker.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskServiceApi {
    @GET("tasks/v2")
    Call<List<Task>> getTasks();

    @PUT("tasks/v2/{id}")
    Call<Task> update(@Path("id") String id, @Body Task taskToUpdate);

    @POST("tasks/v2")
    Call<Task> create(@Body Task taskToCreate);
}
