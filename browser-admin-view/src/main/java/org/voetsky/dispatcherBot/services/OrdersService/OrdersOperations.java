package org.voetsky.dispatcherBot.services.OrdersService;

import org.voetsky.dispatcherBot.dtos.UpdateOrderDto;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import java.util.List;

public interface OrdersOperations {

    OrderClient showOrder(Long orderId);

    List<Song> showSong(Long orderId);

    List<OrderClient> newOrders();

    List<OrderClient> finOrders();

    Boolean updateOrderFalse(UpdateOrderDto request);

    Boolean updateOrderTrue(UpdateOrderDto request);

    Boolean deleteOrder(UpdateOrderDto request);
}
