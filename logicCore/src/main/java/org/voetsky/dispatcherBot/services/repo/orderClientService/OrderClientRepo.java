package org.voetsky.dispatcherBot.services.repo.orderClientService;

import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

public interface OrderClientRepo {

    OrderClient defaultOrder(TgUser tgUser);

    OrderClient save(OrderClient orderClient);

    OrderClient findOrderClientById(Long id);

}
