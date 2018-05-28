package com.ats.barstockexchangeuser.bean;

/**
 * Created by maxadmin on 25/11/17.
 */

public class UserBean {

    private int userId;
    private String userName;
    private String mobileNo;
    private String password;
    private int isActive;
    private int delStatus;
    private String deviceToken;
    private String email;
    private String dob;

    public UserBean(String userName, String mobileNo, String password, int isActive, int delStatus, String deviceToken) {
        this.userName = userName;
        this.mobileNo = mobileNo;
        this.password = password;
        this.isActive = isActive;
        this.delStatus = delStatus;
        this.deviceToken = deviceToken;
    }

    public UserBean(String userName, String mobileNo, String password, int isActive, int delStatus, String deviceToken, String email, String dob) {
        this.userName = userName;
        this.mobileNo = mobileNo;
        this.password = password;
        this.isActive = isActive;
        this.delStatus = delStatus;
        this.deviceToken = deviceToken;
        this.email = email;
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                ", delStatus=" + delStatus +
                ", deviceToken='" + deviceToken + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }
}
