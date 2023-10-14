package org.voetsky.dispatcherBot.entities;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "OrderId")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long OrderId;

    String chatID;
    String phoneNumber;
    String comment;
    String price;
    boolean status;
    String Date;
}
