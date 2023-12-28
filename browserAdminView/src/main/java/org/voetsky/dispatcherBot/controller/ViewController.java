package org.voetsky.dispatcherBot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClientRepository;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.song.SongRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j
@AllArgsConstructor
@RestController
public class ViewController {
    private final OrderClientRepository orderClientRepository;
    private final SongRepository songRepository;

    @GetMapping("/")
    public List<OrderClient> allOrders(Model model) {
        return orderClientRepository.findOrderClientsByIsAcceptedTrue();
    }

    @GetMapping("orders/{orderId}")
    public OrderClient showOrder(@PathVariable("orderId") Long orderId, Model model) {
        return orderClientRepository.findOrderClientById(orderId);
    }

    @GetMapping("songs/{songId}")
    public Map<String, Object> showSong(@PathVariable("songId") Long songId, Model model) {
        Map<String, Object> response = new HashMap<>();
        Song song = songRepository.findSongById(songId);
        OrderClient order = orderClientRepository.findOrderClientById(song.getOrderClient().getId());
        List<Song> allOrderSongs = songRepository.findSongsByOrderClient(order);

        response.put("song", song);
        response.put("allOrderSongs", allOrderSongs);

        return response;
    }

    @GetMapping("orders/new")
    public List<OrderClient> newOrders(Model model) {
        List<OrderClient> orders = orderClientRepository.findOrderClientsByIsAcceptedTrue();

        return orders.stream()
                .filter(order -> !order.getSuccessful())
                .sorted(Comparator.comparing(OrderClient::getDate)
                        .reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("/orders/fin")
    public List<OrderClient> finOrders(Model model) {
        List<OrderClient> orders = orderClientRepository.findOrderClientsByIsAcceptedTrue();
        List<OrderClient> finalizedOrders = orders.stream()
                .filter(OrderClient::getSuccessful)
                .sorted(Comparator.comparing(OrderClient::getDate)
                        .reversed())
                .collect(Collectors.toList());
        orders.sort(Comparator.comparing(OrderClient::getDate).reversed());

        return finalizedOrders;
    }

    @PostMapping("/updateOrderClientFalse")
    public OrderClient updateOrderFalse(@RequestParam("orderId") Long orderId) {
        OrderClient updateOrderFalse = orderClientRepository.findById(orderId).orElse(null);
        if (updateOrderFalse != null) {
            updateOrderFalse.setSuccessful(false);
            orderClientRepository.save(updateOrderFalse);
        }
        return updateOrderFalse;
    }

    @PostMapping("/updateOrderClientTrue")
    public OrderClient updateOrderTrue(@RequestParam("orderId") Long orderId) {
        OrderClient updateOrderTrue = orderClientRepository.findById(orderId).orElse(null);
        if (updateOrderTrue != null) {
            updateOrderTrue.setSuccessful(true);
            orderClientRepository.save(updateOrderTrue);
        }
        return updateOrderTrue;
    }

    @PostMapping("/deleteOrder")
    public String deleteOrder(@RequestParam("orderId") Long orderId) {
        orderClientRepository.deleteById(orderId);
        return "redirect:/";
    }
}
