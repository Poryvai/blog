package com.poryvai.blog.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post {

    @Id
    @SequenceGenerator(name = "postSequence", sequenceName = "POST_SEQUENCE", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postSequence")
    private long id;
    private String title;
    private String content;
    @Column(columnDefinition = "boolean default false")
    private boolean star;

}
