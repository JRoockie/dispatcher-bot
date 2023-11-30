package org.voetsky.dispatcherBot.services.repoAcess.orderClientService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.TgUser;
import org.voetsky.dispatcherBot.repository.OrderClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Log4j
@AllArgsConstructor
@Component
public class OrderClientRepositoryService implements OrderClientRepo {

    private final OrderClientRepository orderClientRepository;

    @Override
    public TgUser addOrder(TgUser tgUser) {
        log.debug("BDS: adding new order");
        //todo учесть вариант если заказ не завершен, и его нужно продолжить обрабатывать

        List<OrderClient> orderList = orderClientRepository.findOrderClientsByTgUser(tgUser);
        orderList = hasUnacceptedOrders(orderList);

        if (!orderList.isEmpty()) {
            log.debug("BDS: This user have already unsuccessful order");
        } else {
            //todo написать логику создания нового ордера
            OrderClient orderClient = defaultOrder(tgUser);
            tgUser = orderClient.getTgUser();
            return tgUser;
        }
        //достать еще несуществующую коллекцию заказов присобаченную к юзеру. Заполнить ее пустой болванкой
        return null;
    }

    @Override
    public OrderClient defaultOrder(TgUser tgUser) {
        log.debug("BDS: init order");

        OrderClient orderClient = OrderClient.builder()
                .tgUser(tgUser)
                .isAccepted(false)
                .build();
        return orderClientRepository.save(orderClient);
    }

    public List<OrderClient> hasUnacceptedOrders(List<OrderClient> orderList) {
        return orderList.stream()
                .filter(order -> !order.isAccepted()) // isAccepted = false
                .collect(Collectors.toList());
    }

    @Override
    public OrderClient save(OrderClient orderClient) {
        return orderClientRepository.save(orderClient);
    }

    public OrderClient findOrderClientById(Long id){
        return orderClientRepository.findOrderClientById(id);
    }

}
