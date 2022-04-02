package com.developer.krisi.tasker.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer existingProjectError;
    @Nullable
    private Integer newProjectNameError;
    private boolean isProjectIdValid;
    private boolean isNewProjectNameValid;

    LoginFormState(@Nullable Integer existingProjectError, @Nullable Integer newProjectNameError) {
        this.existingProjectError = existingProjectError;
        this.newProjectNameError = newProjectNameError;
        this.isProjectIdValid = false;
        this.isNewProjectNameValid = false;
    }

    LoginFormState(boolean isProjectIdValid, boolean isNewProjectNameValid) {
        this.existingProjectError = null;
        this.newProjectNameError = null;
        this.isProjectIdValid = isProjectIdValid;
        this.isNewProjectNameValid = isNewProjectNameValid;
    }

    @Nullable
    Integer getExistingProjectError() {
        return existingProjectError;
    }

    @Nullable
    Integer getNewProjectNameError() {
        return newProjectNameError;
    }

    public boolean isNewProjectNameValid() {
        return isNewProjectNameValid;
    }

    public boolean isProjectIdValid() {
        return isProjectIdValid;
    }
}
