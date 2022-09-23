package com.example.ref.jpa.controller;

import com.example.ref.jpa.entity.Member;
import com.example.ref.jpa.entity.Team;
import com.example.ref.jpa.repository.MemberRepository;
import com.example.ref.jpa.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/checklist")
@Log4j2
public class JpaController {
    TeamRepository teamRepository;
    MemberRepository memberRepository;

    @Autowired
    public JpaController(TeamRepository teamRepository, MemberRepository memberRepository) {
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
    }

    @PostMapping(value = {"test"})
    public ResponseEntity<String> inputTest(@RequestBody String str){
        log.info("==========테스트 ====");
        // 비즈니스 로직;
        Team team = new Team();
        team.setName(str);
        Member member = new Member();
        member.setTeam(team);
        memberRepository.save(member);

        return new ResponseEntity<>(team.getName(), HttpStatus.OK);
    }
}
