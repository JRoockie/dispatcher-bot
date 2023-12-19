package org.voetsky.dispatcherBot.repository.orderClient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

import java.util.List;

public interface OrderClientRepository extends JpaRepository<OrderClient, Long> {
    List<OrderClient> findOrderClientsByTgUser(TgUser tgUser);

    OrderClient findOrderClientById(Long id);

    List<OrderClient> findOrderClientsByIsAcceptedTrue();
}
