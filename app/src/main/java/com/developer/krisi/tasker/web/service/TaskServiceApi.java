package com.developer.krisi.tasker.web.service;

import com.developer.krisi.tasker.model.Task;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskServiceApi {
    @GET("tasks/v2")
    Call<List<Task>> getTasks();

    @PUT("tasks/v2/{id}")
    Call<Task> update(@Path("id") String id, @Body Task taskToUpdate);

    @POST("tasks/v2")
    Call<Task> create(@Body Task taskToCreate);

    @GET("tasks/v2/findByProject/{projectId}")
    Call<List<Task>> findByProjectId(@Path("projectId") String projectId);

    @GET("/tasks/v2/{name}")
    Call<List<Task>> findByName(@Path("name") String name);

    @DELETE("tasks/v2/{id}")
    Call<ResponseBody> delete(@Path("id") String id);
}
