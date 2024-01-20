package org.voetsky.dispatcherBot.services.repo.orderClientService;

import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

import java.util.List;

public interface OrderClientRepo {

    OrderClient defaultOrder(TgUser tgUser);

    OrderClient save(OrderClient orderClient);

    void save(List<OrderClient> orderClients);

    OrderClient findOrderClientById(Long id);


    List<OrderClient> findOrderClientsByIsAcceptedFalse();

}
