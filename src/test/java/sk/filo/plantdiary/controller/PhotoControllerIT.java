package sk.filo.plantdiary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.filo.plantdiary.BaseIntegrationTest;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.so.CreatePhotoSO;
import sk.filo.plantdiary.service.so.PhotoThumbnailSO;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Files;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PhotoControllerIT extends BaseIntegrationTest {

    @Test
    public void photoTest() throws Exception {
        super.setAuthentication("user");

        // test upload photo on non existing plant
        CreatePhotoSO createPhotoSO = easyRandom.nextObject(CreatePhotoSO.class);
        createPhotoSO.getPlant().setId(100000L);

        File photoFile = new File(getClass().getClassLoader().getResource("photo.jpg").getFile());
        byte[] testFile = Files.readAllBytes(photoFile.toPath());

        MockMultipartFile photo
                = new MockMultipartFile(
                "photo",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                testFile
        );

        MockMultipartFile jsonCreatePhotoSO
                = new MockMultipartFile(
                "createPhotoSO",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapToJson(createPhotoSO).getBytes()
        );

        mvc.perform(MockMvcRequestBuilders.multipart("/api/photo")
                .file(photo)
                .file(jsonCreatePhotoSO))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_NOT_FOUND.name()));

        // test upload photo on existing plant
        createPhotoSO.getPlant().setId(1L);
        jsonCreatePhotoSO
                = new MockMultipartFile(
                "createPhotoSO",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapToJson(createPhotoSO).getBytes()
        );
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.multipart("/api/photo")
                .file(photo)
                .file(jsonCreatePhotoSO))
                .andExpect(status().isCreated())
                .andReturn();

        PhotoThumbnailSO photoThumbnailSO = mapFromJson(mvcResult.getResponse().getContentAsString(), PhotoThumbnailSO.class);
        assertThat(photoThumbnailSO).usingRecursiveComparison().ignoringFields("id", "thumbnail", "plant.name").isEqualTo(createPhotoSO);
        assertThat(photoThumbnailSO.getId()).isNotNull();
        assertThat(photoThumbnailSO.getThumbnail()).isNotNull();
        assertThat(photoThumbnailSO.getThumbnail()).hasSize(22044);

        // TODO more tests
    }
}
