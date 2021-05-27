package com.developer.krisi.tasker.web.service;

import com.developer.krisi.tasker.data.model.TasksProject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProjectServiceApi {
    @GET("projects/v1")
    Call<List<TasksProject>> getProjects();

    @GET("projects/v1/{id}")
    Call<TasksProject> findById(@Path("id") String id);

    @POST("projects/v1")
    Call<TasksProject> create(@Body TasksProject projectToCreate);
}
