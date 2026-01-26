package com.university.cosmocats.service;

import com.university.cosmocats.domain.order.OrderStatus;
import com.university.cosmocats.dto.order.CreateOrderRequestDto;
import com.university.cosmocats.dto.order.OrderResponseDto;
import com.university.cosmocats.dto.orderitem.CreateOrderItemRequestDto;
import com.university.cosmocats.entity.CustomerEntity;
import com.university.cosmocats.entity.OrderEntity;
import com.university.cosmocats.entity.OrderItemEntity;
import com.university.cosmocats.entity.ProductEntity;
import com.university.cosmocats.exception.OrderCancellingException;
import com.university.cosmocats.exception.OrderNotFoundException;
import com.university.cosmocats.mapper.OrderMapper;
import com.university.cosmocats.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductService productService;
    private final CustomerService customerService;

    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto orderRequestDto) {
        log.info("Creating a new order: {}", orderRequestDto);

        CustomerEntity customer = customerService.findCustomerById(orderRequestDto.getCustomerId());

        List<Long> productIdsList = orderRequestDto.getOrderItemsListDto().stream()
                .map(oi -> oi.getProductId())
                .toList();

        Map<Long, ProductEntity> productMap = productService
                .getListOfProductsByIds(productIdsList).stream()
                .collect(Collectors.toMap(ProductEntity::getId, Function.identity()));

        OrderEntity order = buildOrder(customer, orderRequestDto.getOrderItemsListDto(), productMap);
        OrderEntity savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toOrderResponseDto);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long id) {
        OrderEntity orderEntity = findOrderEntityById(id);

        return orderMapper.toOrderResponseDto(orderEntity);
    }


    @Transactional
    public void cancelOrder(Long id) {
        OrderEntity orderEntity = findOrderEntityById(id);

        validateOrder(orderEntity);
        orderEntity.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(orderEntity);
    }

    @Transactional
    public void deleteOrder(Long id) {
        OrderEntity orderEntity = findOrderEntityById(id);
        orderRepository.delete(orderEntity);
    }

    protected OrderEntity findOrderEntityById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found"));
    }

    protected OrderEntity buildOrder(CustomerEntity customer,
                                     List<CreateOrderItemRequestDto> orderItemRequestDtoList,
                                     Map<Long, ProductEntity> productMap) {
        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);

        List<OrderItemEntity> orderItems = createOrderItems(order, orderItemRequestDtoList, productMap);
        orderItems.forEach(order::addOrderItem);

        BigDecimal totalAmount = calculateTotalAmount(orderItems);
        order.setTotalAmount(totalAmount);
        return order;
    }

    protected List<OrderItemEntity> createOrderItems(OrderEntity order,
                                                     List<CreateOrderItemRequestDto> orderItemRequestDtoList,
                                                     Map<Long, ProductEntity> productMap) {
        return orderItemRequestDtoList.stream()
                .map(orderItem -> createOrderItemEntity(order, orderItem, productMap.get(orderItem.getProductId())))
                .toList();

    }

    protected OrderItemEntity createOrderItemEntity(OrderEntity order,
                                                    CreateOrderItemRequestDto item,
                                                    ProductEntity product) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrder(order);
        orderItemEntity.setProduct(product);
        orderItemEntity.setQuantity(item.getQuantity());
        return orderItemEntity;
    }

    protected BigDecimal calculateTotalAmount(List<OrderItemEntity> orderItemEntityList) {
        return orderItemEntityList.stream()
                .map(orderItem -> orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    protected void validateOrder(OrderEntity order) {
        if (Objects.equals(order.getStatus(), OrderStatus.CANCELLED)
                || Objects.equals(order.getStatus(), OrderStatus.DELIVERED)) {
            throw new OrderCancellingException(" Can't cancel order: "
                    + order.getId()
                    + "Cancelling DELIVERED or CANCELLED order unavailable");
        }
    }

}
