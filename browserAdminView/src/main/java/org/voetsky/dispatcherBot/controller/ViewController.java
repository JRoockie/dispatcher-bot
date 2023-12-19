package org.voetsky.dispatcherBot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClientRepository;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.song.SongRepository;

import java.util.List;

@Log4j
@AllArgsConstructor
@Controller
@RequestMapping("/")
public class ViewController {

    private final OrderClientRepository orderClientRepository;
    private final SongRepository songRepository;

    @GetMapping()
    public String allOrders(Model model) {
        List<OrderClient> orders = orderClientRepository.findOrderClientsByIsAcceptedTrue();
        model.addAttribute("orders", orders);
        return "orders/orders";
    }

    //    @RequestMapping("/order")
    @GetMapping("orders/{orderId}")
    public String showOrder(@PathVariable("orderId") Long orderId, Model model) {
        OrderClient order = orderClientRepository.findOrderClientById(orderId);
        List<Song> songs = songRepository.findSongsByOrderClient(order);
        model.addAttribute("order", order);
        model.addAttribute("songs", songs);
        return "orders/show";
    }

    @GetMapping("songs/{songId}")
    public String showSong(@PathVariable("songId") Long songId, Model model) {
        Song song = songRepository.findSongById(songId);
        model.addAttribute("song", song);
        return "songs/show";
    }

    @GetMapping("/orders/new")
    public String newOrders(Model model) {
        List<OrderClient> orders = orderClientRepository.findOrderClientsBySuccessfulIsFalse();
        model.addAttribute("orders", orders);
        return "orders/incomingOrders";
    }

    @GetMapping("/orders/fin")
    public String finOrders(Model model) {
        List<OrderClient> orders = orderClientRepository.findOrderClientsBySuccessfulIsTrue();
        model.addAttribute("orders", orders);
        return "orders/finalizedOrders";
    }

    @PostMapping("/updateOrderClientFalse")
    public String updateOrderFalse(@RequestParam("orderId") Long orderId) {
        OrderClient orderClient = orderClientRepository.findById(orderId).orElse(null);
        if (orderClient != null) {
            orderClient.setSuccessful(false);
            orderClientRepository.save(orderClient);
        }
        return String.format("redirect:/orders/%s", orderId);
    }

    @PostMapping("/updateOrderClientTrue")
    public String updateOrderTrue(@RequestParam("orderId") Long orderId) {
        OrderClient orderClient = orderClientRepository.findById(orderId).orElse(null);
        if (orderClient != null) {
            orderClient.setSuccessful(true);
            orderClientRepository.save(orderClient);
        }
        return String.format("redirect:/orders/%s", orderId);
    }

    @PostMapping("/deleteOrder")
    public String deleteOrder(@RequestParam("orderId") Long orderId) {
        orderClientRepository.deleteById(orderId);
        return "redirect:/";
    }


}
