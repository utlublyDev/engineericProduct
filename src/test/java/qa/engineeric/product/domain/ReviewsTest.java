package qa.engineeric.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import qa.engineeric.product.web.rest.TestUtil;

class ReviewsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reviews.class);
        Reviews reviews1 = new Reviews();
        reviews1.setId("id1");
        Reviews reviews2 = new Reviews();
        reviews2.setId(reviews1.getId());
        assertThat(reviews1).isEqualTo(reviews2);
        reviews2.setId("id2");
        assertThat(reviews1).isNotEqualTo(reviews2);
        reviews1.setId(null);
        assertThat(reviews1).isNotEqualTo(reviews2);
    }
}
