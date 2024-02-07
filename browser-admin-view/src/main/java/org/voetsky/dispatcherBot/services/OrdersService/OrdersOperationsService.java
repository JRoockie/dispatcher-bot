package org.voetsky.dispatcherBot.services.OrdersService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.dtos.UpdateOrderDto;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClientRepository;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.song.SongRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log4j
@Service
public class OrdersOperationsService implements OrdersOperations {

    private final OrderClientRepository orderClientRepository;
    private final SongRepository songRepository;

    @Override
    public OrderClient showOrder(Long orderId) {
        return orderClientRepository.findOrderClientById(orderId);
    }

    @Override
    public List<Song> showSong(Long orderId) {
        OrderClient order = orderClientRepository.findOrderClientById(orderId);
        return songRepository.findSongsByOrderClient(order);
    }

    @Override
    public List<OrderClient> newOrders() {
        List<OrderClient> orders = orderClientRepository.findOrderClientsByIsAcceptedTrue();
        orders = orders.stream()
                .filter(order -> !order.getSuccessful())
                .filter(order -> order.getDeletedWhen() == null)
                .sorted(Comparator.comparing(OrderClient::getDate).reversed())
                .collect(Collectors.toList());
        return orders;
    }

    @Override
    public List<OrderClient> finOrders() {
        List<OrderClient> orders = orderClientRepository.findOrderClientsByIsAcceptedTrue();
        List<OrderClient> finalizedOrders = orders.stream()
                .filter(OrderClient::getSuccessful)
                .filter(order -> order.getDeletedWhen() == null)
                .sorted(Comparator.comparing(OrderClient::getDate).reversed())
                .collect(Collectors.toList());
        orders.sort(Comparator.comparing(OrderClient::getDate).reversed());
        return finalizedOrders;
    }

    @Override
    public Boolean updateOrderFalse(UpdateOrderDto request) {
        OrderClient updateOrderFalse = orderClientRepository.findById(request.id()).orElse(null);
        if (updateOrderFalse != null) {
            updateOrderFalse.setSuccessful(false);
            orderClientRepository.save(updateOrderFalse);
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateOrderTrue(UpdateOrderDto request) {
        OrderClient updateOrderTrue = orderClientRepository.findById(request.id()).orElse(null);
        if (updateOrderTrue != null) {
            updateOrderTrue.setSuccessful(true);
            orderClientRepository.save(updateOrderTrue);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteOrder(UpdateOrderDto request) {
        LocalDateTime deleteTime = LocalDateTime.now();

        OrderClient orderClient = orderClientRepository.findOrderClientById(request.id());
        if (orderClient != null) {
            orderClient.setDeletedWhen(deleteTime);
            orderClientRepository.save(orderClient);
            List<Song> songs = songRepository.findSongsByOrderClient(orderClient);

            if (!songs.isEmpty()){
                songs = songs.stream()
                        .peek(x -> x.setDeletedWhen(deleteTime))
                        .toList();
                songRepository.saveAll(songs);
            }
            return true;
        }
        return false;
    }

}
