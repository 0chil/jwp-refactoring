package kitchenpos.order;

import kitchenpos.menu.MenuRepository;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableRepository;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = toOrderLineItems(orderRequest.getOrderLineItems());

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        //
        validateFull(orderTable);
        //
        Order order = new Order(orderRequest.getOrderTableId(), orderLineItems);
        return orderRepository.save(order);
    }

    //
    private void validateFull(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블에서 주문할 수 없습니다");
        }
    }
    //

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemsRequests) {
        if (Objects.nonNull(orderLineItemsRequests)) {
            return orderLineItemsRequests.stream()
                    .map(this::toOrderLineItem)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private OrderLineItem toOrderLineItem(OrderLineItemRequest request) {
        menuRepository.findById(request.getMenuId()).orElseThrow(IllegalArgumentException::new);
        return new OrderLineItem(
                request.getMenuId(),
                request.getQuantity()
        );
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderStatus == OrderStatus.COOKING) {
            savedOrder.cook();
        }
        if (orderStatus == OrderStatus.MEAL) {
            savedOrder.serve();
        }
        if (orderStatus == OrderStatus.COMPLETION) {
            savedOrder.complete();
        }
        return savedOrder;
    }
}