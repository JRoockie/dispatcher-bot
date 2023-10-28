package org.voetsky.dispatcherBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.TgUser;

import java.util.List;

public interface OrderClientRepository extends JpaRepository<OrderClient,Long> {
    List<OrderClient> findOrderClientsByTgUser(TgUser tgUser);
    Long findOrderClientById(Long id);


}
