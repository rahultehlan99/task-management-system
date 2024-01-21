package com.example.taskmanagementsystem.enums;

public enum TaskStatus {
    TODO, DONE, IN_PROGRESS;

    public static boolean checkIfExists(String status){
        try{
            TaskStatus.valueOf(status);
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
