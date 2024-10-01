package com.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CusNoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CusNoteDTO.class);
        CusNoteDTO cusNoteDTO1 = new CusNoteDTO();
        cusNoteDTO1.setId(1L);
        CusNoteDTO cusNoteDTO2 = new CusNoteDTO();
        assertThat(cusNoteDTO1).isNotEqualTo(cusNoteDTO2);
        cusNoteDTO2.setId(cusNoteDTO1.getId());
        assertThat(cusNoteDTO1).isEqualTo(cusNoteDTO2);
        cusNoteDTO2.setId(2L);
        assertThat(cusNoteDTO1).isNotEqualTo(cusNoteDTO2);
        cusNoteDTO1.setId(null);
        assertThat(cusNoteDTO1).isNotEqualTo(cusNoteDTO2);
    }
}
