package com.example.ref.security_jwt.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    public PostMemberRes createMember(PostMemberReq postMemberReq) {
        postMemberReq.setPassword(passwordEncoder.encode(postMemberReq.getPassword()));
        return memberDao.createMember(postMemberReq);
    }

    @Transactional
    public PostMemberRes createSeller(PostMemberReq postMemberReq) {
        postMemberReq.setPassword(passwordEncoder.encode(postMemberReq.getPassword()));
        return memberDao.createSeller(postMemberReq);
    }

    public Boolean getUserEmail(String email) {
        return memberRepository.getUserEmail(email);
    }
}
