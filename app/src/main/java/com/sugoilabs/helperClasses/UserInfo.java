package com.sugoilabs.helperClasses;

import java.util.ArrayList;

final public class UserInfo{
    public String phoneNumber;
    public String password;
    public boolean paymentStatus;
    public ArrayList<String> currentExercise;
    public ArrayList<String> currentDiet;
    public int currentGoal; // 1: Weight gain, 2 :Weight loss, 3: building muscles
    public String userName;
    public UserInfo(String username){
        userName = username;
        phoneNumber = "0000000000";
        password = "123";
        paymentStatus = true;
        currentExercise = new ArrayList<String>();
        currentExercise.add("Crunches");
        currentExercise.add("Squatting");
        currentExercise.add("Sit Ups");
        currentExercise.add("Running");
        currentExercise.add("Stretching");

        currentDiet = new ArrayList<String>();
        currentDiet.add("Breakfast : 45 calories");
        currentDiet.add("Lunch : 120 calogories");
        currentDiet.add("Dinner: 90 calories");

        currentGoal = 2;
    }
}
