package qa.engineeric.product.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import qa.engineeric.product.domain.ProductOrder;
import qa.engineeric.product.domain.enumeration.OrderStatus;
import qa.engineeric.product.repository.ProductOrderRepository;

/**
 * Integration tests for the {@link ProductOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductOrderResourceIT {

    private static final String DEFAULT_USER_STORE_OWNER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_STORE_OWNER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_PLACED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLACED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.COMPLETED;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PENDING;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_INVOICE_ID = 1L;
    private static final Long UPDATED_INVOICE_ID = 2L;

    private static final String DEFAULT_CUSTOMER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private MockMvc restProductOrderMockMvc;

    private ProductOrder productOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createEntity() {
        ProductOrder productOrder = new ProductOrder()
            .userStoreOwnerId(DEFAULT_USER_STORE_OWNER_ID)
            .userId(DEFAULT_USER_ID)
            .placedDate(DEFAULT_PLACED_DATE)
            .status(DEFAULT_STATUS)
            .code(DEFAULT_CODE)
            .invoiceId(DEFAULT_INVOICE_ID)
            .customer(DEFAULT_CUSTOMER);
        return productOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createUpdatedEntity() {
        ProductOrder productOrder = new ProductOrder()
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .userId(UPDATED_USER_ID)
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE)
            .invoiceId(UPDATED_INVOICE_ID)
            .customer(UPDATED_CUSTOMER);
        return productOrder;
    }

    @BeforeEach
    public void initTest() {
        productOrderRepository.deleteAll();
        productOrder = createEntity();
    }

    @Test
    void createProductOrder() throws Exception {
        int databaseSizeBeforeCreate = productOrderRepository.findAll().size();
        // Create the ProductOrder
        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isCreated());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getUserStoreOwnerId()).isEqualTo(DEFAULT_USER_STORE_OWNER_ID);
        assertThat(testProductOrder.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testProductOrder.getPlacedDate()).isEqualTo(DEFAULT_PLACED_DATE);
        assertThat(testProductOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProductOrder.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProductOrder.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testProductOrder.getCustomer()).isEqualTo(DEFAULT_CUSTOMER);
    }

    @Test
    void createProductOrderWithExistingId() throws Exception {
        // Create the ProductOrder with an existing ID
        productOrder.setId("existing_id");

        int databaseSizeBeforeCreate = productOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUserStoreOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().size();
        // set the field null
        productOrder.setUserStoreOwnerId(null);

        // Create the ProductOrder, which fails.

        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().size();
        // set the field null
        productOrder.setUserId(null);

        // Create the ProductOrder, which fails.

        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPlacedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().size();
        // set the field null
        productOrder.setPlacedDate(null);

        // Create the ProductOrder, which fails.

        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().size();
        // set the field null
        productOrder.setStatus(null);

        // Create the ProductOrder, which fails.

        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().size();
        // set the field null
        productOrder.setCode(null);

        // Create the ProductOrder, which fails.

        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCustomerIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().size();
        // set the field null
        productOrder.setCustomer(null);

        // Create the ProductOrder, which fails.

        restProductOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isBadRequest());

        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProductOrders() throws Exception {
        // Initialize the database
        productOrderRepository.save(productOrder);

        // Get all the productOrderList
        restProductOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrder.getId())))
            .andExpect(jsonPath("$.[*].userStoreOwnerId").value(hasItem(DEFAULT_USER_STORE_OWNER_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(DEFAULT_PLACED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID.intValue())))
            .andExpect(jsonPath("$.[*].customer").value(hasItem(DEFAULT_CUSTOMER)));
    }

    @Test
    void getProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.save(productOrder);

        // Get the productOrder
        restProductOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, productOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productOrder.getId()))
            .andExpect(jsonPath("$.userStoreOwnerId").value(DEFAULT_USER_STORE_OWNER_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.placedDate").value(DEFAULT_PLACED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID.intValue()))
            .andExpect(jsonPath("$.customer").value(DEFAULT_CUSTOMER));
    }

    @Test
    void getNonExistingProductOrder() throws Exception {
        // Get the productOrder
        restProductOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.save(productOrder);

        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();

        // Update the productOrder
        ProductOrder updatedProductOrder = productOrderRepository.findById(productOrder.getId()).get();
        updatedProductOrder
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .userId(UPDATED_USER_ID)
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE)
            .invoiceId(UPDATED_INVOICE_ID)
            .customer(UPDATED_CUSTOMER);

        restProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductOrder))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testProductOrder.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testProductOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testProductOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductOrder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProductOrder.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testProductOrder.getCustomer()).isEqualTo(UPDATED_CUSTOMER);
    }

    @Test
    void putNonExistingProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductOrderWithPatch() throws Exception {
        // Initialize the database
        productOrderRepository.save(productOrder);

        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();

        // Update the productOrder using partial update
        ProductOrder partialUpdatedProductOrder = new ProductOrder();
        partialUpdatedProductOrder.setId(productOrder.getId());

        partialUpdatedProductOrder
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .userId(UPDATED_USER_ID)
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .customer(UPDATED_CUSTOMER);

        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOrder))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testProductOrder.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testProductOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testProductOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductOrder.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProductOrder.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testProductOrder.getCustomer()).isEqualTo(UPDATED_CUSTOMER);
    }

    @Test
    void fullUpdateProductOrderWithPatch() throws Exception {
        // Initialize the database
        productOrderRepository.save(productOrder);

        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();

        // Update the productOrder using partial update
        ProductOrder partialUpdatedProductOrder = new ProductOrder();
        partialUpdatedProductOrder.setId(productOrder.getId());

        partialUpdatedProductOrder
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .userId(UPDATED_USER_ID)
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE)
            .invoiceId(UPDATED_INVOICE_ID)
            .customer(UPDATED_CUSTOMER);

        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOrder))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testProductOrder.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testProductOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testProductOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductOrder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProductOrder.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testProductOrder.getCustomer()).isEqualTo(UPDATED_CUSTOMER);
    }

    @Test
    void patchNonExistingProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrderMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productOrder))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOrder in the database
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.save(productOrder);

        int databaseSizeBeforeDelete = productOrderRepository.findAll().size();

        // Delete the productOrder
        restProductOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, productOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductOrder> productOrderList = productOrderRepository.findAll();
        assertThat(productOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
