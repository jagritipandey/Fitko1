package com.sugoilabs.helperClasses;

import java.util.ArrayList;

/**
 * Created by sujeetk on 15-04-2015.
 */
public class TrainerDb {
    private static UserDb userDb = UserDb.getInstance();
    private static TrainerDb mInstance = null;
    private int numberOfTrainers = 2;
    public static ArrayList<TrainerInfo> trainerInformation;
    private String invalidText;


    public static synchronized TrainerDb getInstance(){
        if(null == mInstance){
            mInstance = new TrainerDb();
        }

        return mInstance;
    }

    public void Init()
    {
        for (int i = 0; i < numberOfTrainers; i++)
        {
            trainerInformation = new ArrayList<TrainerInfo>();
            trainerInformation.add(new TrainerInfo());
        }
    }

    public boolean CheckCredentials(String phoneNum, String pwd){
        boolean phoneNumberExists = false;
        boolean passWordExists = false;
        for(TrainerInfo userInfo : trainerInformation){
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

final class TrainerInfo
{
    public String phoneNumber;
    public String password;
    public TrainerInfo()
    {
        phoneNumber = "1111111111";
        password = "123";
    }
}

