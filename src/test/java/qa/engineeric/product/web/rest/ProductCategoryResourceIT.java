package qa.engineeric.product.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import qa.engineeric.product.IntegrationTest;
import qa.engineeric.product.domain.ProductCategory;
import qa.engineeric.product.domain.enumeration.CategoryStatus;
import qa.engineeric.product.repository.ProductCategoryRepository;
import qa.engineeric.product.service.ProductCategoryService;

/**
 * Integration tests for the {@link ProductCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductCategoryResourceIT {

    private static final String DEFAULT_USER_STORE_OWNER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_STORE_OWNER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_CATEGORY_NAME_IN_ARABIC = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CATEGORY_NAME_IN_ARABIC = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_CATEGORY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CATEGORY_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final CategoryStatus DEFAULT_STATUS = CategoryStatus.AVAILABLE;
    private static final CategoryStatus UPDATED_STATUS = CategoryStatus.RESTRICTED;

    private static final String DEFAULT_WEB_KEY = "AAAAAAAAAA";
    private static final String UPDATED_WEB_KEY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepositoryMock;

    @Mock
    private ProductCategoryService productCategoryServiceMock;

    @Autowired
    private MockMvc restProductCategoryMockMvc;

    private ProductCategory productCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategory createEntity() {
        ProductCategory productCategory = new ProductCategory()
            .userStoreOwnerId(DEFAULT_USER_STORE_OWNER_ID)
            .productCategoryName(DEFAULT_PRODUCT_CATEGORY_NAME)
            .productCategoryNameInArabic(DEFAULT_PRODUCT_CATEGORY_NAME_IN_ARABIC)
            .productCategoryDescription(DEFAULT_PRODUCT_CATEGORY_DESCRIPTION)
            .productCategoryDescriptionInArabic(DEFAULT_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC)
            .sortOrder(DEFAULT_SORT_ORDER)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED)
            .status(DEFAULT_STATUS)
            .webKey(DEFAULT_WEB_KEY);
        return productCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategory createUpdatedEntity() {
        ProductCategory productCategory = new ProductCategory()
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .productCategoryName(UPDATED_PRODUCT_CATEGORY_NAME)
            .productCategoryNameInArabic(UPDATED_PRODUCT_CATEGORY_NAME_IN_ARABIC)
            .productCategoryDescription(UPDATED_PRODUCT_CATEGORY_DESCRIPTION)
            .productCategoryDescriptionInArabic(UPDATED_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS)
            .webKey(UPDATED_WEB_KEY);
        return productCategory;
    }

    @BeforeEach
    public void initTest() {
        productCategoryRepository.deleteAll();
        productCategory = createEntity();
    }

    @Test
    void createProductCategory() throws Exception {
        int databaseSizeBeforeCreate = productCategoryRepository.findAll().size();
        // Create the ProductCategory
        restProductCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isCreated());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getUserStoreOwnerId()).isEqualTo(DEFAULT_USER_STORE_OWNER_ID);
        assertThat(testProductCategory.getProductCategoryName()).isEqualTo(DEFAULT_PRODUCT_CATEGORY_NAME);
        assertThat(testProductCategory.getProductCategoryNameInArabic()).isEqualTo(DEFAULT_PRODUCT_CATEGORY_NAME_IN_ARABIC);
        assertThat(testProductCategory.getProductCategoryDescription()).isEqualTo(DEFAULT_PRODUCT_CATEGORY_DESCRIPTION);
        assertThat(testProductCategory.getProductCategoryDescriptionInArabic()).isEqualTo(DEFAULT_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC);
        assertThat(testProductCategory.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testProductCategory.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testProductCategory.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
        assertThat(testProductCategory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProductCategory.getWebKey()).isEqualTo(DEFAULT_WEB_KEY);
    }

    @Test
    void createProductCategoryWithExistingId() throws Exception {
        // Create the ProductCategory with an existing ID
        productCategory.setId("existing_id");

        int databaseSizeBeforeCreate = productCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUserStoreOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productCategoryRepository.findAll().size();
        // set the field null
        productCategory.setUserStoreOwnerId(null);

        // Create the ProductCategory, which fails.

        restProductCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkProductCategoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productCategoryRepository.findAll().size();
        // set the field null
        productCategory.setProductCategoryName(null);

        // Create the ProductCategory, which fails.

        restProductCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkProductCategoryNameInArabicIsRequired() throws Exception {
        int databaseSizeBeforeTest = productCategoryRepository.findAll().size();
        // set the field null
        productCategory.setProductCategoryNameInArabic(null);

        // Create the ProductCategory, which fails.

        restProductCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProductCategories() throws Exception {
        // Initialize the database
        productCategoryRepository.save(productCategory);

        // Get all the productCategoryList
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategory.getId())))
            .andExpect(jsonPath("$.[*].userStoreOwnerId").value(hasItem(DEFAULT_USER_STORE_OWNER_ID)))
            .andExpect(jsonPath("$.[*].productCategoryName").value(hasItem(DEFAULT_PRODUCT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].productCategoryNameInArabic").value(hasItem(DEFAULT_PRODUCT_CATEGORY_NAME_IN_ARABIC)))
            .andExpect(jsonPath("$.[*].productCategoryDescription").value(hasItem(DEFAULT_PRODUCT_CATEGORY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productCategoryDescriptionInArabic").value(hasItem(DEFAULT_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].webKey").value(hasItem(DEFAULT_WEB_KEY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.save(productCategory);

        // Get the productCategory
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productCategory.getId()))
            .andExpect(jsonPath("$.userStoreOwnerId").value(DEFAULT_USER_STORE_OWNER_ID))
            .andExpect(jsonPath("$.productCategoryName").value(DEFAULT_PRODUCT_CATEGORY_NAME))
            .andExpect(jsonPath("$.productCategoryNameInArabic").value(DEFAULT_PRODUCT_CATEGORY_NAME_IN_ARABIC))
            .andExpect(jsonPath("$.productCategoryDescription").value(DEFAULT_PRODUCT_CATEGORY_DESCRIPTION))
            .andExpect(jsonPath("$.productCategoryDescriptionInArabic").value(DEFAULT_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.webKey").value(DEFAULT_WEB_KEY));
    }

    @Test
    void getNonExistingProductCategory() throws Exception {
        // Get the productCategory
        restProductCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.save(productCategory);

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Update the productCategory
        ProductCategory updatedProductCategory = productCategoryRepository.findById(productCategory.getId()).get();
        updatedProductCategory
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .productCategoryName(UPDATED_PRODUCT_CATEGORY_NAME)
            .productCategoryNameInArabic(UPDATED_PRODUCT_CATEGORY_NAME_IN_ARABIC)
            .productCategoryDescription(UPDATED_PRODUCT_CATEGORY_DESCRIPTION)
            .productCategoryDescriptionInArabic(UPDATED_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS)
            .webKey(UPDATED_WEB_KEY);

        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testProductCategory.getProductCategoryName()).isEqualTo(UPDATED_PRODUCT_CATEGORY_NAME);
        assertThat(testProductCategory.getProductCategoryNameInArabic()).isEqualTo(UPDATED_PRODUCT_CATEGORY_NAME_IN_ARABIC);
        assertThat(testProductCategory.getProductCategoryDescription()).isEqualTo(UPDATED_PRODUCT_CATEGORY_DESCRIPTION);
        assertThat(testProductCategory.getProductCategoryDescriptionInArabic()).isEqualTo(UPDATED_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC);
        assertThat(testProductCategory.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testProductCategory.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testProductCategory.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testProductCategory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductCategory.getWebKey()).isEqualTo(UPDATED_WEB_KEY);
    }

    @Test
    void putNonExistingProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductCategoryWithPatch() throws Exception {
        // Initialize the database
        productCategoryRepository.save(productCategory);

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Update the productCategory using partial update
        ProductCategory partialUpdatedProductCategory = new ProductCategory();
        partialUpdatedProductCategory.setId(productCategory.getId());

        partialUpdatedProductCategory
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .productCategoryNameInArabic(UPDATED_PRODUCT_CATEGORY_NAME_IN_ARABIC)
            .productCategoryDescriptionInArabic(UPDATED_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC)
            .status(UPDATED_STATUS)
            .webKey(UPDATED_WEB_KEY);

        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testProductCategory.getProductCategoryName()).isEqualTo(DEFAULT_PRODUCT_CATEGORY_NAME);
        assertThat(testProductCategory.getProductCategoryNameInArabic()).isEqualTo(UPDATED_PRODUCT_CATEGORY_NAME_IN_ARABIC);
        assertThat(testProductCategory.getProductCategoryDescription()).isEqualTo(DEFAULT_PRODUCT_CATEGORY_DESCRIPTION);
        assertThat(testProductCategory.getProductCategoryDescriptionInArabic()).isEqualTo(UPDATED_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC);
        assertThat(testProductCategory.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testProductCategory.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testProductCategory.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
        assertThat(testProductCategory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductCategory.getWebKey()).isEqualTo(UPDATED_WEB_KEY);
    }

    @Test
    void fullUpdateProductCategoryWithPatch() throws Exception {
        // Initialize the database
        productCategoryRepository.save(productCategory);

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Update the productCategory using partial update
        ProductCategory partialUpdatedProductCategory = new ProductCategory();
        partialUpdatedProductCategory.setId(productCategory.getId());

        partialUpdatedProductCategory
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .productCategoryName(UPDATED_PRODUCT_CATEGORY_NAME)
            .productCategoryNameInArabic(UPDATED_PRODUCT_CATEGORY_NAME_IN_ARABIC)
            .productCategoryDescription(UPDATED_PRODUCT_CATEGORY_DESCRIPTION)
            .productCategoryDescriptionInArabic(UPDATED_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS)
            .webKey(UPDATED_WEB_KEY);

        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testProductCategory.getProductCategoryName()).isEqualTo(UPDATED_PRODUCT_CATEGORY_NAME);
        assertThat(testProductCategory.getProductCategoryNameInArabic()).isEqualTo(UPDATED_PRODUCT_CATEGORY_NAME_IN_ARABIC);
        assertThat(testProductCategory.getProductCategoryDescription()).isEqualTo(UPDATED_PRODUCT_CATEGORY_DESCRIPTION);
        assertThat(testProductCategory.getProductCategoryDescriptionInArabic()).isEqualTo(UPDATED_PRODUCT_CATEGORY_DESCRIPTION_IN_ARABIC);
        assertThat(testProductCategory.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testProductCategory.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testProductCategory.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testProductCategory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductCategory.getWebKey()).isEqualTo(UPDATED_WEB_KEY);
    }

    @Test
    void patchNonExistingProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.save(productCategory);

        int databaseSizeBeforeDelete = productCategoryRepository.findAll().size();

        // Delete the productCategory
        restProductCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
