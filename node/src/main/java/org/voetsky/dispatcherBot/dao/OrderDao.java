package org.voetsky.dispatcherBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entities.OrderClient;

public interface OrderDao extends JpaRepository<OrderClient,Long> {
}
