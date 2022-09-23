package com.example.ref.jpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MEMBER_ID")
    private Long id;
    private String userName;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    // 편의 메소드: 한 번에 양방향 관계를 설정하는 메소드.
    public void setTeam(Team team) {
        this.team = team;

        //무한 루프 방지
        if(!team.getMembers().contains(this)){ // team에 Members가 Member객체를 안가져야 실행된다.
            team.getMembers().add(this);
        }
    }
}
