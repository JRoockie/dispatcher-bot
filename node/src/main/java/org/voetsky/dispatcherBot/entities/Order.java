package org.voetsky.dispatcherBot.entities;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = {"orderId", "userId"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String chatID;
    private String comment;
    private String price;
    private boolean status;
    private String date;
}
