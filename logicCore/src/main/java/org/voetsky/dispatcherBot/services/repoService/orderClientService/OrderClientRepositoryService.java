package org.voetsky.dispatcherBot.services.repoService.orderClientService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.voetsky.dispatcherBot.entity.OrderClient;
import org.voetsky.dispatcherBot.entity.Song;
import org.voetsky.dispatcherBot.entity.TgUser;
import org.voetsky.dispatcherBot.repository.OrderClientRepository;
import org.voetsky.dispatcherBot.services.repoService.songService.SongRepositoryService;
import org.voetsky.dispatcherBot.services.repoService.tgUserService.TgUserRepositoryService;

import java.util.List;
import java.util.stream.Collectors;

@Log4j
@AllArgsConstructor
@Component
public class OrderClientRepositoryService implements OrderClientRepo {

    private final OrderClientRepository orderClientRepository;
//    private final TgUserRepositoryService tgUserRepositoryService;
    private final SongRepositoryService songRepositoryService;

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

        orderClient = orderClientRepository.save(orderClient);
//        tgUser = tgUserRepositoryService.findAppUsersByTelegramUserId(tgUser.getTelegramUserId());
//        tgUser.setCurrentOrderId(orderClient.getId());
//        tgUser = tgUserRepositoryService.save(tgUser);
//
//        Song song = songRepositoryService.save(songRepositoryService.defaultSong(orderClient));
//
//        tgUser = tgUserRepositoryService.findAppUsersByTelegramUserId(tgUser.getTelegramUserId());
//        tgUser.setCurrentSongId(song.getId());
//        tgUser = tgUserRepositoryService.save(tgUser);

        return orderClientRepository.findOrderClientById(tgUser.getCurrentOrderId());
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
}
