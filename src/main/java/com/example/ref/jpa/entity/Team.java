package com.example.ref.jpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team", cascade = CascadeType.PERSIST)
    private List<Member> members = new ArrayList<>();

    // 편의 메소드: 한 번에 양방향 관계를 설정하는 메소드.
    public void addMember(Member member) {
        this.members.add(member);
        if (member.getTeam() !=this){
            member.setTeam(this);
        }
    }

}
