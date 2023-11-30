package org.voetsky.dispatcherBot.services.repoAcess.orderClientService;

import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

import java.util.List;

public interface OrderClientRepo {

    public TgUser addOrder(TgUser tgUser);

    public List<OrderClient> hasUnacceptedOrders(List<OrderClient> orderList);

    OrderClient defaultOrder(TgUser tgUser);

    public OrderClient save(OrderClient orderClient);

    OrderClient findOrderClientById(Long id);

}
