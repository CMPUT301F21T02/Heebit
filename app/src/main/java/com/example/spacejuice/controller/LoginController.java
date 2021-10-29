package com.example.spacejuice.controller;

import com.example.spacejuice.MainActivity;
import com.example.spacejuice.Member;

import java.util.ArrayList;

public class LoginController {
    public ArrayList<Member> MemberDataList = new ArrayList<Member>();

    public LoginController(){
        //change to firestore load every member into MemberDataList
        Member test_member = new Member("Bradley", "handsome001");  //testmember
        MemberDataList.add(test_member);
        test_member = new Member("1234", "5678");
        MemberDataList.add(test_member);
    }
    public boolean login(String userName, String password){
        for (int i=0; i<MemberDataList.size(); i++){
            Member member = MemberDataList.get(i);
            if (member.getMemberName().equals(userName) && member.verifyPassword(password)){
                    return true;

            }
        }
        return false;
    }

    public void signUp(String userName, String password){
        Member new_Member = new Member(userName, password);
        MainActivity.setUser(new_Member);
    }






}
