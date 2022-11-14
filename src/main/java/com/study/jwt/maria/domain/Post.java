package com.study.jwt.maria.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(
            nullable = false
    )
    private String title; // 제목

    @Column(
            nullable = false
    )
    private String content; // 내용

    @Column(
            nullable = false
    )
    private String writer; // 작성자

    @Column(
            nullable = false
    )
    private int view = 0; // 조회수

    @ManyToOne(
            targetEntity = User.class,
            fetch = FetchType.LAZY
    ) // 실제로 요청하는 순간 가져오기 위해 LAZY로 사용함.
    @JoinColumn(name = "username")
    private User user;

    @OneToMany(
            targetEntity = Comment.class,
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @OrderBy("id DESC")
    private List<Comment> comment; // 댓글

    @OneToMany(
            targetEntity = PostLike.class,
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<PostLike> likes; // 좋아요
}
