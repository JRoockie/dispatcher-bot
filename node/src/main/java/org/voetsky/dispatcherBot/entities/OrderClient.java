package org.voetsky.dispatcherBot.entities;


import lombok.*;
import org.voetsky.dispatcherBot.entity.AppUser;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "orderClient_Id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
public class OrderClient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderClient_Id;

    @ManyToOne
    @JoinColumn(name = "appUserid", referencedColumnName = "appUserid")
    private AppUser appUser;

    @OneToMany(mappedBy = "orderId")
    private List<Song> songs;

    private String chatId;
    private String comment;
    private String price;
    private boolean status;
    private String date;
    private Long phoneNumber;

}
