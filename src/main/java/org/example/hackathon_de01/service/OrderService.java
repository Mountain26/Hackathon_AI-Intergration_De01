package org.example.hackathon_de01.service;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_de01.dto.CreateOrderRequest;
import org.example.hackathon_de01.dto.CreateOrderResponse;
import org.example.hackathon_de01.dto.OrderItemRequest;
import org.example.hackathon_de01.model.constant.OrderStatus;
import org.example.hackathon_de01.model.entity.Customer;
import org.example.hackathon_de01.model.entity.Order;
import org.example.hackathon_de01.model.entity.OrderItem;
import org.example.hackathon_de01.model.entity.Product;
import org.example.hackathon_de01.repository.CustomerRepository;
import org.example.hackathon_de01.repository.OrderItemRepository;
import org.example.hackathon_de01.repository.OrderRepository;
import org.example.hackathon_de01.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm không được rỗng");
        }

        Customer customer = customerRepository.findByPhone(request.customerPhone())
                .orElseGet(() -> customerRepository.save(new Customer(null, request.customerName(), request.customerPhone(), null, request.address())));

        if (customer.getFullName() == null || customer.getFullName().isBlank()) {
            customer.setFullName(request.customerName());
        }
        if (customer.getAddress() == null || customer.getAddress().isBlank()) {
            customer.setAddress(request.address());
        }
        customer = customerRepository.save(customer);

        List<Product> products = new ArrayList<>();
        for (OrderItemRequest item : request.items()) {
            Product product = productRepository.findByNameIgnoreCase(item.productName())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm: " + item.productName()));
            int quantity = item.quantity() == null ? 0 : item.quantity();
            if (quantity <= 0) {
                throw new IllegalArgumentException("Số lượng không hợp lệ cho sản phẩm: " + item.productName());
            }
            if (product.getStock() < quantity) {
                throw new IllegalArgumentException("Sản phẩm " + product.getName() + " không đủ tồn kho. Còn " + product.getStock());
            }
            products.add(product);
        }

        Order order = new Order(null, customer, LocalDateTime.now(), OrderStatus.PENDING, BigDecimal.ZERO, "Đặt hàng qua AI Chatbot");
        order = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;
        List<String> summaries = new ArrayList<>();
        for (int i = 0; i < request.items().size(); i++) {
            OrderItemRequest item = request.items().get(i);
            Product product = products.get(i);
            int quantity = item.quantity();
            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            total = total.add(lineTotal);
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
            orderItemRepository.save(new OrderItem(null, order, product, quantity, product.getPrice()));
            summaries.add(product.getName() + " x" + quantity + " = " + lineTotal);
        }

        order.setTotalAmount(total);
        order.setStatus(OrderStatus.CONFIRMED);
        order = orderRepository.save(order);

        return new CreateOrderResponse(order.getId(), customer.getPhone(), total, order.getStatus().name(), summaries);
    }
}
