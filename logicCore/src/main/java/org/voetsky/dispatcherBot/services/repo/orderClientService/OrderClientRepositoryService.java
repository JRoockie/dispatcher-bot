package org.voetsky.dispatcherBot.services.repo.orderClientService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClientRepository;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

@Log4j
@AllArgsConstructor
@Service
public class OrderClientRepositoryService implements OrderClientRepo {

    private final OrderClientRepository orderClientRepository;

    @Override
    public OrderClient defaultOrder(TgUser tgUser) {
        if (log.isDebugEnabled()) {
            log.debug("Init order");
        }
        OrderClient orderClient = OrderClient.builder()
                .tgUser(tgUser)
                .isAccepted(false)
                .build();
        return orderClientRepository.save(orderClient);
    }

    @Override
    public OrderClient save(OrderClient orderClient) {
        return orderClientRepository.save(orderClient);
    }

    public OrderClient findOrderClientById(Long id) {
        return orderClientRepository.findOrderClientById(id);
    }

}
