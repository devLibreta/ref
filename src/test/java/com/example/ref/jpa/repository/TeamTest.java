package com.example.ref.jpa.repository;

import com.example.ref.jpa.entity.Member;
import com.example.ref.jpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TeamTest {

    TeamRepository teamRepository;
    MemberRepository memberRepository;

    @Autowired
    public TeamTest(TeamRepository teamRepository, MemberRepository memberRepository) {
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
    }
    @Test
    public void insertDataWithTeam(){
        Team team = new Team();
        team.setName("TeamName");
        Member member1 = new Member();
        member1.setUserName("MemberName");
        member1.setTeam(team); // 연관관계의 주인에게 값 설정
//        team.getMembers().add(member1); // 역방향으로 연관관계 설정. 할 필요가 없다.

        memberRepository.save(member1);
        System.out.println("===================================================");
        System.out.println(member1.getUserName()); // MemberName
        System.out.println(member1.getTeam().getName()); // TeamName
        System.out.println("===================================================");
        System.out.println(memberRepository.findById(1L));
    }
}
