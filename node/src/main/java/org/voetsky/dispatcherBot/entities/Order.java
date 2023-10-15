package org.voetsky.dispatcherBot.entities;


import lombok.*;
import org.voetsky.dispatcherBot.entity.AppUser;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "orderId")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String chatID;
    private String comment;
    private String price;
    private boolean status;
    private String date;
    private Long phoneNumber;

}
