package org.voetsky.dispatcherBot.services.repoAcess.orderClientService;

import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.TgUser;

import java.util.List;

public interface OrderClientRepo {

    public TgUser addOrder(TgUser tgUser);

    public List<OrderClient> hasUnacceptedOrders(List<OrderClient> orderList);

    OrderClient defaultOrder(TgUser tgUser);

    public OrderClient save(OrderClient orderClient);

    OrderClient findOrderClientById(Long id);

}
