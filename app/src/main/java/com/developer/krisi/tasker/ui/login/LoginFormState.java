package com.developer.krisi.tasker.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isUsernameValid;
    private boolean isPasswordValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isUsernameValid = false;
        this.isPasswordValid = false;
    }

    LoginFormState(boolean isUsernameValid, boolean isPasswordValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isUsernameValid = isUsernameValid;
        this.isPasswordValid = isPasswordValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    public boolean isPasswordValid() {
        return isPasswordValid;
    }

    public boolean isUsernameValid() {
        return isUsernameValid;
    }
}
