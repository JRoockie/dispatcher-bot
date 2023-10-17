package org.voetsky.dispatcherBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.OrderClient;

public interface OrderClientDao extends JpaRepository<OrderClient,Long> {
}
