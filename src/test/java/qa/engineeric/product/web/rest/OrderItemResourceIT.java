package qa.engineeric.product.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static qa.engineeric.product.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import qa.engineeric.product.IntegrationTest;
import qa.engineeric.product.domain.OrderItem;
import qa.engineeric.product.domain.Product;
import qa.engineeric.product.domain.ProductOrder;
import qa.engineeric.product.domain.enumeration.OrderItemStatus;
import qa.engineeric.product.repository.OrderItemRepository;

/**
 * Integration tests for the {@link OrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderItemResourceIT {

    private static final String DEFAULT_USER_STORE_OWNER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_STORE_OWNER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);

    private static final OrderItemStatus DEFAULT_STATUS = OrderItemStatus.AVAILABLE;
    private static final OrderItemStatus UPDATED_STATUS = OrderItemStatus.OUT_OF_STOCK;

    private static final String DEFAULT_PAYMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_NUMBER = 1;
    private static final Integer UPDATED_ORDER_NUMBER = 2;

    private static final String ENTITY_API_URL = "/api/order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private MockMvc restOrderItemMockMvc;

    private OrderItem orderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createEntity() {
        OrderItem orderItem = new OrderItem()
            .userStoreOwnerId(DEFAULT_USER_STORE_OWNER_ID)
            .userId(DEFAULT_USER_ID)
            .quantity(DEFAULT_QUANTITY)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .status(DEFAULT_STATUS)
            .paymentId(DEFAULT_PAYMENT_ID)
            .orderNumber(DEFAULT_ORDER_NUMBER);
        // Add required entity
        Product product;
        product = ProductResourceIT.createEntity();
        product.setId("fixed-id-for-tests");
        orderItem.setProduct(product);
        // Add required entity
        ProductOrder productOrder;
        productOrder = ProductOrderResourceIT.createEntity();
        productOrder.setId("fixed-id-for-tests");
        orderItem.setOrder(productOrder);
        return orderItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createUpdatedEntity() {
        OrderItem orderItem = new OrderItem()
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .userId(UPDATED_USER_ID)
            .quantity(UPDATED_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .status(UPDATED_STATUS)
            .paymentId(UPDATED_PAYMENT_ID)
            .orderNumber(UPDATED_ORDER_NUMBER);
        // Add required entity
        Product product;
        product = ProductResourceIT.createUpdatedEntity();
        product.setId("fixed-id-for-tests");
        orderItem.setProduct(product);
        // Add required entity
        ProductOrder productOrder;
        productOrder = ProductOrderResourceIT.createUpdatedEntity();
        productOrder.setId("fixed-id-for-tests");
        orderItem.setOrder(productOrder);
        return orderItem;
    }

    @BeforeEach
    public void initTest() {
        orderItemRepository.deleteAll();
        orderItem = createEntity();
    }

    @Test
    void createOrderItem() throws Exception {
        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();
        // Create the OrderItem
        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isCreated());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getUserStoreOwnerId()).isEqualTo(DEFAULT_USER_STORE_OWNER_ID);
        assertThat(testOrderItem.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testOrderItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrderItem.getTotalPrice()).isEqualByComparingTo(DEFAULT_TOTAL_PRICE);
        assertThat(testOrderItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrderItem.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
        assertThat(testOrderItem.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
    }

    @Test
    void createOrderItemWithExistingId() throws Exception {
        // Create the OrderItem with an existing ID
        orderItem.setId("existing_id");

        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUserStoreOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setUserStoreOwnerId(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setUserId(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setQuantity(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setTotalPrice(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setStatus(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPaymentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setPaymentId(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setOrderNumber(null);

        // Create the OrderItem, which fails.

        restOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemRepository.save(orderItem);

        // Get all the orderItemList
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId())))
            .andExpect(jsonPath("$.[*].userStoreOwnerId").value(hasItem(DEFAULT_USER_STORE_OWNER_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(DEFAULT_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)));
    }

    @Test
    void getOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.save(orderItem);

        // Get the orderItem
        restOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItem.getId()))
            .andExpect(jsonPath("$.userStoreOwnerId").value(DEFAULT_USER_STORE_OWNER_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.paymentId").value(DEFAULT_PAYMENT_ID))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER));
    }

    @Test
    void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.save(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem
        OrderItem updatedOrderItem = orderItemRepository.findById(orderItem.getId()).get();
        updatedOrderItem
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .userId(UPDATED_USER_ID)
            .quantity(UPDATED_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .status(UPDATED_STATUS)
            .paymentId(UPDATED_PAYMENT_ID)
            .orderNumber(UPDATED_ORDER_NUMBER);

        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testOrderItem.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderItem.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderItem.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testOrderItem.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
    }

    @Test
    void putNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.save(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .quantity(UPDATED_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .status(UPDATED_STATUS)
            .orderNumber(UPDATED_ORDER_NUMBER);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testOrderItem.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderItem.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderItem.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
        assertThat(testOrderItem.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
    }

    @Test
    void fullUpdateOrderItemWithPatch() throws Exception {
        // Initialize the database
        orderItemRepository.save(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem using partial update
        OrderItem partialUpdatedOrderItem = new OrderItem();
        partialUpdatedOrderItem.setId(orderItem.getId());

        partialUpdatedOrderItem
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .userId(UPDATED_USER_ID)
            .quantity(UPDATED_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .status(UPDATED_STATUS)
            .paymentId(UPDATED_PAYMENT_ID)
            .orderNumber(UPDATED_ORDER_NUMBER);

        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testOrderItem.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderItem.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrderItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderItem.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        assertThat(testOrderItem.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
    }

    @Test
    void patchNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();
        orderItem.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orderItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.save(orderItem);

        int databaseSizeBeforeDelete = orderItemRepository.findAll().size();

        // Delete the orderItem
        restOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
