package com.telegram.bot.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "DontSmokeInfo")
@Table(schema = "public", name = "dontsmokeinfo")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DontSmokeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long ID;
    @Column(name = "ANNOTATION")
    private String ANNOTATION;
    @Column(name = "TYPE")
    private Integer TYPE;
    @Column(name = "INFO")
    private String INFO;
}
