package org.voetsky.dispatcherBot.services.repo.orderClientService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClientRepository;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

import java.util.List;

@Log4j
@AllArgsConstructor
@Service
public class OrderClientRepositoryService implements OrderClientRepo {

    private final OrderClientRepository orderClientRepositoryJpa;

    @Override
    public OrderClient defaultOrder(TgUser tgUser) {
        if (log.isDebugEnabled()) {
            log.debug("Init order");
        }
        OrderClient orderClient = OrderClient.builder()
                .tgUser(tgUser)
                .isAccepted(false)
                .successful(false)
                .build();
        return orderClientRepositoryJpa.save(orderClient);
    }

    @Override
    public OrderClient save(OrderClient orderClient) {
        return orderClientRepositoryJpa.save(orderClient);
    }

    @Override
    public void save(List<OrderClient> orderClients) {
        orderClientRepositoryJpa.saveAll(orderClients);
    }

    public OrderClient findOrderClientById(Long id) {
        return orderClientRepositoryJpa.findOrderClientById(id);
    }

    @Override
    public List<OrderClient> findOrderClientsByIsAcceptedFalse() {
        return orderClientRepositoryJpa.findOrderClientsByIsAcceptedFalse();
    }

}
