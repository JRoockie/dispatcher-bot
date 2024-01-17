package org.voetsky.dispatcherBot.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.voetsky.dispatcherBot.dtos.UpdateOrderDto;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClientRepository;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.song.SongRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
@AllArgsConstructor
@RestController
//@RequestMapping("/bot")
public class ViewController {
    private final OrderClientRepository orderClientRepository;
    private final SongRepository songRepository;

    @GetMapping("/")
    public String allOrders() {
        return "обработанные и необработанные заявки. \nЗдесь эндпоинты на http://localhost:8086/orders/fin (обработанные) и на необработанные на http://localhost:8086/orders/new";
    }

    @GetMapping("orders/{orderId}")
    public ResponseEntity<OrderClient> showOrder(@PathVariable("orderId") Long orderId) {
        OrderClient order = orderClientRepository.findOrderClientById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("songs/{orderId}")
    public ResponseEntity<List<Song>> showSong(@PathVariable("orderId") Long orderId) {
        OrderClient order = orderClientRepository.findOrderClientById(orderId);
        return ResponseEntity.ok(songRepository.findSongsByOrderClient(order));
    }

    @GetMapping("orders/new")
    public ResponseEntity<List<OrderClient>> newOrders() {
        List<OrderClient> orders = orderClientRepository.findOrderClientsByIsAcceptedTrue();
        orders = orders.stream().filter(order -> !order.getSuccessful()).sorted(Comparator.comparing(OrderClient::getDate).reversed()).collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/fin")
    public ResponseEntity<List<OrderClient>> finOrders() {
        List<OrderClient> orders = orderClientRepository.findOrderClientsByIsAcceptedTrue();
        List<OrderClient> finalizedOrders = orders.stream().filter(OrderClient::getSuccessful).sorted(Comparator.comparing(OrderClient::getDate).reversed()).collect(Collectors.toList());
        orders.sort(Comparator.comparing(OrderClient::getDate).reversed());
        return ResponseEntity.ok(finalizedOrders);
    }

    @PostMapping("/updateOrderClientFalse")
    public ResponseEntity<HttpStatus> updateOrderFalse(@RequestBody UpdateOrderDto request) {
        OrderClient updateOrderFalse = orderClientRepository.findById(request.id()).orElse(null);
        if (updateOrderFalse != null) {
            updateOrderFalse.setSuccessful(false);
            orderClientRepository.save(updateOrderFalse);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updateOrderClientTrue")
    public ResponseEntity<HttpStatus> updateOrderTrue(@RequestBody UpdateOrderDto request) {
        OrderClient updateOrderTrue = orderClientRepository.findById(request.id()).orElse(null);
        if (updateOrderTrue != null) {
            updateOrderTrue.setSuccessful(true);
            orderClientRepository.save(updateOrderTrue);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/deleteOrder")
    public ResponseEntity<HttpStatus> deleteOrder(@RequestBody UpdateOrderDto request) {
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
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }
}
