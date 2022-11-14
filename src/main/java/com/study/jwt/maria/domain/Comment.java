package com.study.jwt.maria.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(
            length = 500,
            nullable = false
    )
    private String content; // 내용

    @Column(
            length = 100,
            nullable = false
    )
    private String writer; // 작성자

    @ManyToOne(
            targetEntity = User.class,
            fetch = FetchType.LAZY
    ) // 실제로 요청하는 순간 가져오기 위해 LAZY로 사용함.
    @JoinColumn
    private User user;

    @ManyToOne(
            targetEntity = Post.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn
    private Post post;
}
