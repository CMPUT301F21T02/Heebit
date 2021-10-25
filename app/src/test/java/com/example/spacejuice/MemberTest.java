package com.example.spacejuice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

    public class MemberTest {


        private Member mockMember(){
            return new Member("SpaceJuice", "SpaceJuice");
        }

        private Member emptyMember(){
            return  new Member(" ", " ");
        }

        private ArrayList<Member> mockMemberList(){
            ArrayList<Member> memberList = new ArrayList<Member>();
            memberList.add(mockMember());
            return memberList;
        }

        @Test
        void testGetName(){
            Member member = mockMember();
            assertEquals("SpaceJuice",member.getMemberName());
            Member emptyMember = emptyMember();
            assertEquals(" ", emptyMember.getMemberName());
        }

        @Test
        void testScore(){
            Member member = mockMember();
            member.setScore(1);
            assertEquals(1,member.getScore());
        }

        @Test
        void testFollower(){
            Member member = mockMember();
            member.setFollowers(1);
            assertEquals(1,member.getFollowers());
        }

        @Test
        void testFollowing(){
            Member member = mockMember();
            member.setFollowings(1);
            assertEquals(1,member.getFollowings());
        }
    }