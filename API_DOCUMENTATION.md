# API Documentation

This document provides a detailed overview of the API endpoints for the Smart Dormitory Management System.

## Table of Contents

- [Authentication](#authentication)
- [Student Profile](#student-profile)
- [User](#user)
- [Upload](#upload)

---

## Authentication

**Controller:** `AuthController.java`
**Base Path:** `/api/v1/auth`

### 1. User Login

- **Method:** `POST`
- **Endpoint:** `/api/v1/auth/login`
- **Summary:** Authenticate user and return JWT tokens.
- **Request Body:** `LoginRequest.java`
  ```json
  {
    "usernameOrEmail": "string",
    "password": "string"
  }
  ```
- **Response Body:** `AuthResponse.java`
  ```json
  {
    "accessToken": "string",
    "refreshToken": "string",
    "tokenType": "Bearer"
  }
  ```

### 2. Refresh Token

- **Method:** `POST`
- **Endpoint:** `/api/v1/auth/refresh-token`
- **Summary:** Get new access and refresh tokens.
- **Request Body:** `RefreshTokenRequest.java`
  ```json
  {
    "refreshToken": "string"
  }
  ```
- **Response Body:** `AuthResponse.java`
  ```json
  {
    "accessToken": "string",
    "refreshToken": "string",
    "tokenType": "Bearer"
  }
  ```

### 3. User Logout

- **Method:** `POST`
- **Endpoint:** `/api/v1/auth/logout`
- **Summary:** Logout user and revoke refresh token.
- **Request Body:** None
- **Response Body:** `void`

### 4. Change Password

- **Method:** `POST`
- **Endpoint:** `/api/v1/auth/change-password`
- **Summary:** Change password for the current authenticated user.
- **Request Body:** `ChangePasswordRequest.java`
  ```json
  {
    "oldPassword": "string",
    "newPassword": "string"
  }
  ```
- **Response Body:** `void`

### 5. Forgot Password

- **Method:** `POST`
- **Endpoint:** `/api/v1/auth/forgot-password`
- **Summary:** Request password reset via email.
- **Request Body:** `ForgotPasswordRequest.java`
  ```json
  {
    "email": "string"
  }
  ```
- **Response Body:** `void`

### 6. Reset Password

- **Method:** `POST`
- **Endpoint:** `/api/v1/auth/reset-password`
- **Summary:** Reset user password using a valid token.
- **Request Body:** `ResetPasswordRequest.java`
  ```json
  {
    "token": "string",
    "newPassword": "string"
  }
  ```
- **Response Body:** `void`

---

## Student Profile

**Controller:** `StudentController.java`
**Base Path:** `/api/v1/students`

### 1. Get Current Student Profile

- **Method:** `GET`
- **Endpoint:** `/api/v1/students/me`
- **Summary:** Retrieves the profile details of the authenticated student.
- **Request Body:** None
- **Response Body:** `StudentProfileResponse.java`
  ```json
  {
    "studentId": "uuid",
    "studentCode": "string",
    "fullName": "string",
    "cccd": "string",
    "email": "string",
    "phone": "string",
    "faculty": "string",
    "academicYear": "string",
    "fatherName": "string",
    "fatherPhone": "string",
    "motherName": "string",
    "motherPhone": "string",
    "emergencyContact": "string",
    "permanentAddress": "string",
    "avatarUrl": "string",
    "status": "string"
  }
  ```

### 2. Update Current Student Profile

- **Method:** `PATCH`
- **Endpoint:** `/api/v1/students/me`
- **Summary:** Updates only the fields provided in the request body.
- **Request Body:** `UpdateProfileRequest.java`
  ```json
  {
    "email": "string",
    "phone": "string",
    "fatherName": "string",
    "fatherPhone": "string",
    "motherName": "string",
    "motherPhone": "string",
    "emergencyContact": "string",
    "permanentAddress": "string",
    "avatarUrl": "string"
  }
  ```
- **Response Body:** `StudentProfileResponse.java`
  ```json
  {
    "studentId": "uuid",
    "studentCode": "string",
    "fullName": "string",
    "cccd": "string",
    "email": "string",
    "phone": "string",
    "faculty": "string",
    "academicYear": "string",
    "fatherName": "string",
    "fatherPhone": "string",
    "motherName": "string",
    "motherPhone": "string",
    "emergencyContact": "string",
    "permanentAddress": "string",
    "avatarUrl": "string",
    "status": "string"
  }
  ```

---

## User

**Controller:** `UserController.java`
**Base Path:** `/api/v1/users`

### 1. Get Current User

- **Method:** `GET`
- **Endpoint:** `/api/v1/users/me`
- **Summary:** Get profile information of the currently authenticated user.
- **Request Body:** None
- **Response Body:** `MeResponse.java`
  ```json
  {
    "accountId": "uuid",
    "username": "string",
    "email": "string",
    "role": "string",
    "status": "string"
  }
  ```

---

## Upload

**Controller:** `UploadController.java`
**Base Path:** `/api/v1/uploads`

### 1. Upload Avatar

- **Method:** `POST`
- **Endpoint:** `/api/v1/uploads/avatar`
- **Summary:** Upload an image file for an avatar.
- **Request Body:** `MultipartFile`
- **Response Body:** `UploadResponse.java`
  ```json
  {
    "url": "string"
  }
  ```
