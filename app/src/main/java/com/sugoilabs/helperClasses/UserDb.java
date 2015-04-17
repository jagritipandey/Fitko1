package com.sugoilabs.helperClasses;

import java.util.ArrayList;

/**
 * Created by sujeetk on 13-04-2015.
 */
public class UserDb {
    public static int numberOfUsers = 6;
    private boolean initialized = false;
    private static UserDb mInstance= null;
    public static ArrayList<UserInfo> userInformation;
    private String invalidText;
    protected UserDb(){}

    ArrayList<String> userfirstnames = new ArrayList<String>();

    public static synchronized UserDb getInstance(){
        if(null == mInstance){
            mInstance = new UserDb();
        }

        return mInstance;
    }

    public void Init()
    {
        if(initialized == true)
        {
            return;
        }
        userfirstnames.add("Peter");
        userfirstnames.add("Steward");
        userfirstnames.add("Andrew");
        userfirstnames.add("John");
        userfirstnames.add("Mary");
        userfirstnames.add("Adam");
        userInformation = new ArrayList<UserInfo>();
        for (int i = 0; i < numberOfUsers ; i++)
        {
            userInformation.add(new UserInfo(userfirstnames.get(i)));
        }

    }

    public UserInfo GetUserInfo(int index)
    {
        if (userInformation.size() <= index)
        {
            //toast
        }
        return userInformation.get(index);
    }

    public String getInvalidText(){
        return invalidText;
    }


    public boolean CheckCredentials(String phoneNum, String pwd){
        boolean phoneNumberExists = false;
        boolean passWordExists = false;
        for(UserInfo userInfo : userInformation){
            if (userInfo.phoneNumber.equalsIgnoreCase(phoneNum)){
                phoneNumberExists = true;
                if (passWordExists)
                    return true;
            }
            if (userInfo.password.equalsIgnoreCase(pwd)){
                passWordExists = true;
                if (phoneNumberExists)
                    return true;
            }
        }
        if (!phoneNumberExists)
            invalidText = "The Phone number doesn't exist";
        else if (!passWordExists)
            invalidText = "The Password is incorrect";
        return false;
    }
}

