package org.voetsky.dispatcherBot.services.repo.orderClientService;

import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

import java.util.List;

public interface OrderClientRepo {

    TgUser addOrder(TgUser tgUser);

    List<OrderClient> hasUnacceptedOrders(List<OrderClient> orderList);

    OrderClient defaultOrder(TgUser tgUser);

    OrderClient save(OrderClient orderClient);

    OrderClient findOrderClientById(Long id);

}
