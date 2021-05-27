package com.developer.krisi.tasker.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String projectId;
    private String displayName;

    LoggedInUserView(String projectId, String displayName) {
        this.projectId = projectId;
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }

    String getProjectId() {
        return projectId;
    }

}
