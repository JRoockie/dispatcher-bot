package org.voetsky.dispatcherBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entities.Order;

public interface OrderService extends JpaRepository<Order,Long> {
}
