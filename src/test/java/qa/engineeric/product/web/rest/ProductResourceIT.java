package qa.engineeric.product.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static qa.engineeric.product.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;
import qa.engineeric.product.IntegrationTest;
import qa.engineeric.product.domain.Product;
import qa.engineeric.product.repository.ProductRepository;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_USER_STORE_OWNER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_STORE_OWNER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NAME_IN_ARABIC = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME_IN_ARABIC = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_DESCRIPTION_IN_ARABIC = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DESCRIPTION_IN_ARABIC = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity() {
        Product product = new Product()
            .userStoreOwnerId(DEFAULT_USER_STORE_OWNER_ID)
            .productName(DEFAULT_PRODUCT_NAME)
            .productNameInArabic(DEFAULT_PRODUCT_NAME_IN_ARABIC)
            .productDescription(DEFAULT_PRODUCT_DESCRIPTION)
            .productDescriptionInArabic(DEFAULT_PRODUCT_DESCRIPTION_IN_ARABIC)
            .price(DEFAULT_PRICE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .imageUrl(DEFAULT_IMAGE_URL)
            .keywords(DEFAULT_KEYWORDS)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity() {
        Product product = new Product()
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .productNameInArabic(UPDATED_PRODUCT_NAME_IN_ARABIC)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productDescriptionInArabic(UPDATED_PRODUCT_DESCRIPTION_IN_ARABIC)
            .price(UPDATED_PRICE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .imageUrl(UPDATED_IMAGE_URL)
            .keywords(UPDATED_KEYWORDS)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);
        return product;
    }

    @BeforeEach
    public void initTest() {
        productRepository.deleteAll();
        product = createEntity();
    }

    @Test
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getUserStoreOwnerId()).isEqualTo(DEFAULT_USER_STORE_OWNER_ID);
        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProduct.getProductNameInArabic()).isEqualTo(DEFAULT_PRODUCT_NAME_IN_ARABIC);
        assertThat(testProduct.getProductDescription()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductDescriptionInArabic()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION_IN_ARABIC);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testProduct.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProduct.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testProduct.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testProduct.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testProduct.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId("existing_id");

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUserStoreOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setUserStoreOwnerId(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductName(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkProductNameInArabicIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductNameInArabic(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setPrice(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.save(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId())))
            .andExpect(jsonPath("$.[*].userStoreOwnerId").value(hasItem(DEFAULT_USER_STORE_OWNER_ID)))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].productNameInArabic").value(hasItem(DEFAULT_PRODUCT_NAME_IN_ARABIC)))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productDescriptionInArabic").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION_IN_ARABIC)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }

    @Test
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.save(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId()))
            .andExpect(jsonPath("$.userStoreOwnerId").value(DEFAULT_USER_STORE_OWNER_ID))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.productNameInArabic").value(DEFAULT_PRODUCT_NAME_IN_ARABIC))
            .andExpect(jsonPath("$.productDescription").value(DEFAULT_PRODUCT_DESCRIPTION))
            .andExpect(jsonPath("$.productDescriptionInArabic").value(DEFAULT_PRODUCT_DESCRIPTION_IN_ARABIC))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.keywords").value(DEFAULT_KEYWORDS))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewProduct() throws Exception {
        // Initialize the database
        productRepository.save(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        updatedProduct
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .productNameInArabic(UPDATED_PRODUCT_NAME_IN_ARABIC)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productDescriptionInArabic(UPDATED_PRODUCT_DESCRIPTION_IN_ARABIC)
            .price(UPDATED_PRICE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .imageUrl(UPDATED_IMAGE_URL)
            .keywords(UPDATED_KEYWORDS)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProduct.getProductNameInArabic()).isEqualTo(UPDATED_PRODUCT_NAME_IN_ARABIC);
        assertThat(testProduct.getProductDescription()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductDescriptionInArabic()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION_IN_ARABIC);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testProduct.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProduct.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProduct.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testProduct.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testProduct.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, product.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.save(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .productDescriptionInArabic(UPDATED_PRODUCT_DESCRIPTION_IN_ARABIC)
            .price(UPDATED_PRICE)
            .dateModified(UPDATED_DATE_MODIFIED);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getUserStoreOwnerId()).isEqualTo(DEFAULT_USER_STORE_OWNER_ID);
        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProduct.getProductNameInArabic()).isEqualTo(DEFAULT_PRODUCT_NAME_IN_ARABIC);
        assertThat(testProduct.getProductDescription()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductDescriptionInArabic()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION_IN_ARABIC);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testProduct.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProduct.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testProduct.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testProduct.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testProduct.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.save(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .productNameInArabic(UPDATED_PRODUCT_NAME_IN_ARABIC)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productDescriptionInArabic(UPDATED_PRODUCT_DESCRIPTION_IN_ARABIC)
            .price(UPDATED_PRICE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .imageUrl(UPDATED_IMAGE_URL)
            .keywords(UPDATED_KEYWORDS)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProduct.getProductNameInArabic()).isEqualTo(UPDATED_PRODUCT_NAME_IN_ARABIC);
        assertThat(testProduct.getProductDescription()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductDescriptionInArabic()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION_IN_ARABIC);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testProduct.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProduct.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProduct.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testProduct.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testProduct.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, product.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.save(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
