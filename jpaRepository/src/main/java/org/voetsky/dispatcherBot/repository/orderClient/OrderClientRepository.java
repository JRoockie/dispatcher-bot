package org.voetsky.dispatcherBot.repository.orderClient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderClientRepository extends JpaRepository<OrderClient, Long> {

    OrderClient findOrderClientById(Long id);

    List<OrderClient> findOrderClientsByIsAcceptedTrue();

    List<OrderClient> findOrderClientsByIsAcceptedFalse();

}
