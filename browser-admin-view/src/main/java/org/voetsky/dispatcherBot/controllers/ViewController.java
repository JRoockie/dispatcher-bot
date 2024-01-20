package org.voetsky.dispatcherBot.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.voetsky.dispatcherBot.dtos.UpdateOrderDto;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.services.OrdersService.OrdersOperations;

import java.util.List;

@Log4j
@AllArgsConstructor
@RestController
//@RequestMapping("/bot")
public class ViewController {
    private final OrdersOperations ordersOperations;

    @GetMapping("/")
    public String allOrders() {
        return "обработанные и необработанные заявки. \nЗдесь эндпоинты на http://localhost:8086/orders/fin (обработанные) и на необработанные на http://localhost:8086/orders/new";
    }

    @GetMapping("orders/{orderId}")
    public ResponseEntity<OrderClient> showOrder(@PathVariable("orderId") Long orderId) {
        OrderClient order = ordersOperations.showOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("songs/{orderId}")
    public ResponseEntity<List<Song>> showSong(@PathVariable("orderId") Long orderId) {
        List<Song> songs = ordersOperations.showSong(orderId);
        return ResponseEntity.ok(songs);
    }

    @GetMapping("orders/new")
    public ResponseEntity<List<OrderClient>> newOrders() {
        List<OrderClient> orders = ordersOperations.newOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/fin")
    public ResponseEntity<List<OrderClient>> finOrders() {
        List<OrderClient> finalizedOrders = ordersOperations.finOrders();
        return ResponseEntity.ok(finalizedOrders);
    }

    @PostMapping("/updateOrderClientFalse")
    public ResponseEntity<HttpStatus> updateOrderFalse(@RequestBody UpdateOrderDto request) {
        Boolean success = ordersOperations.updateOrderFalse(request);
        if (success) {
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updateOrderClientTrue")
    public ResponseEntity<HttpStatus> updateOrderTrue(@RequestBody UpdateOrderDto request) {
        Boolean success = ordersOperations.updateOrderTrue(request);
        if (success) {
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/deleteOrder")
    public ResponseEntity<HttpStatus> deleteOrder(@RequestBody UpdateOrderDto request) {
        Boolean success = ordersOperations.deleteOrder(request);
        if (success) {
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
    }
}
