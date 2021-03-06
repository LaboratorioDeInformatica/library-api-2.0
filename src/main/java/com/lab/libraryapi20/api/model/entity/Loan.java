package com.lab.libraryapi20.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String customer;

    @JoinColumn(name ="id_book")
    @ManyToOne
    private Book book;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column
    private LocalDate loanDate;

    @Column
    private Boolean returned;
}
